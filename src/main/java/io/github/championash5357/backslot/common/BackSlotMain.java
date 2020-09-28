/*
 * Entity Armor Models
 * Copyright (C) 2020 ChampionAsh5357
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation version 3.0 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.championash5357.backslot.common;

import io.github.championash5357.backslot.api.common.capability.CapabilityInstances;
import io.github.championash5357.backslot.client.ClientConfigHolder;
import io.github.championash5357.backslot.client.ClientReference;
import io.github.championash5357.backslot.common.capability.CapabilityProviderSerializable;
import io.github.championash5357.backslot.common.capability.backslot.CapabilityBackSlot;
import io.github.championash5357.backslot.common.init.BSCapabilities;
import io.github.championash5357.backslot.common.init.BSContainerTypes;
import io.github.championash5357.backslot.common.network.BSNetwork;
import io.github.championash5357.backslot.common.network.server.SUpdateBackSlotMessage;
import io.github.championash5357.backslot.data.client.BSLanguageProvider;
import io.github.championash5357.backslot.data.client.BSTransformationProvider;
import io.github.championash5357.backslot.server.dedicated.DedicatedServerReference;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(BackSlotMain.ID)
public class BackSlotMain {

	public static final String ID = "backslot";
	public static final ISidedReference SIDED_SYSTEM = DistExecutor.safeRunForDist(() -> ClientReference::new, () -> DedicatedServerReference::new);
	public static final SimpleChannel NETWORK = BSNetwork.network();
	
	private static final String[] LOCALE_CODES = new String[] {
			"en_us"
	};
	
	public BackSlotMain() {
		IEventBus mod = FMLJavaModLoadingContext.get().getModEventBus(),
				forge = MinecraftForge.EVENT_BUS;
		
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfigHolder.CLIENT_SPEC);
		
		addRegistries(mod);
		mod.addListener(this::setup);
		SIDED_SYSTEM.setup(mod, forge);
		forge.addGenericListener(Entity.class, this::attach);
		forge.addListener(EventPriority.LOWEST, this::entityJoin);
		forge.addListener(this::playerJoin);
		forge.addListener(EventPriority.LOWEST, this::dimensionChanged);
		forge.addListener(this::playerDimensionChanged);
		forge.addListener(EventPriority.LOWEST, this::livingDeath);
		forge.addListener(this::onEntityTrack);
		forge.addListener(this::clonePlayer);
		forge.addListener(this::respawnPlayer);
		mod.addListener(this::data);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		BSCapabilities.register();
	}
	
	private void attach(final AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof LivingEntity) event.addCapability(CapabilityBackSlot.BACK_SLOT, new CapabilityProviderSerializable<>(CapabilityInstances.BACK_SLOT_CAPABILITY, null).attachListeners(event::addListener));
	}
	
	private void dimensionChanged(final EntityTravelToDimensionEvent event) {
		if(event.getEntity() instanceof LivingEntity && !event.getEntity().world.isRemote && !event.isCanceled()) {
			sendPacket((LivingEntity) event.getEntity());
		}
	}
	
	private void playerDimensionChanged(final PlayerEvent.PlayerChangedDimensionEvent event) {
		if(event.getPlayer().isServerWorld()) {
			sendPacket(event.getEntityLiving());
		}
	}
	
	private void clonePlayer(final PlayerEvent.Clone event) {
		if(!event.isWasDeath() || event.getPlayer().world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) || event.getOriginal().isSpectator()) {
			event.getOriginal().getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(originalBackSlot -> {
				event.getPlayer().getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(newBackSlot -> {
					newBackSlot.setBackStack(originalBackSlot.getBackStack());
				});
			});
		}
	}
	
	private void respawnPlayer(final PlayerEvent.PlayerRespawnEvent event) {
		if(event.getPlayer().isServerWorld()) {
			sendPacket(event.getEntityLiving());
		}
	}
	
	private void livingDeath(final LivingDeathEvent event) {
		if(!event.isCanceled() && event.getEntityLiving().isServerWorld()) {
			LivingEntity entity = event.getEntityLiving();
			if(entity instanceof PlayerEntity && (entity.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) || entity.isSpectator())) return;
			else if(!(entity instanceof PlayerEntity) && !entity.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) return;
			else event.getEntityLiving().getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(backSlot -> {
				InventoryHelper.spawnItemStack(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), backSlot.getBackStack());
			});
		}
	}
	
	private void playerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
		if(event.getPlayer().isServerWorld()) {
			sendPacket(event.getEntityLiving());
		}
	}
	
	private void entityJoin(final EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof LivingEntity && !event.getWorld().isRemote && !event.isCanceled()) {
			sendPacket((LivingEntity) event.getEntity());
		}
	}
	
	private void onEntityTrack(final PlayerEvent.StartTracking event) {
		if(event.getTarget() instanceof LivingEntity && event.getPlayer().isServerWorld()) {
			sendPacket((ServerPlayerEntity) event.getPlayer(), (LivingEntity) event.getTarget());
		}
	}
	
	private void sendPacket(ServerPlayerEntity entity, LivingEntity tracked) {
		tracked.getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(backSlot -> {
			BackSlotMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> entity), new SUpdateBackSlotMessage(tracked.getEntityId(), backSlot.getBackStack()));
		});
	}
	
	private void sendPacket(LivingEntity entity) {
		entity.getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(backSlot -> {
			BackSlotMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SUpdateBackSlotMessage(entity.getEntityId(), backSlot.getBackStack()));
		});
	}
	
	private void addRegistries(final IEventBus mod) {
		BSContainerTypes.CONTAINER_TYPES.register(mod);
	}
	
	private void data(final GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		if(event.includeClient()) {
			addLanguageProviders(gen);
			gen.addProvider(new BSTransformationProvider(gen));
		}
	}
	
	private void addLanguageProviders(final DataGenerator gen) {
		for(String locale : LOCALE_CODES) gen.addProvider(new BSLanguageProvider(gen, locale));
	}
}
