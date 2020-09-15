package io.github.championash5357.backslot.server;

import io.github.championash5357.backslot.api.common.capability.CapabilityInstances;
import io.github.championash5357.backslot.common.BackSlotMain;
import io.github.championash5357.backslot.common.inventory.container.BackInventoryContainer;
import io.github.championash5357.backslot.common.network.client.CProcessActionMessage;
import io.github.championash5357.backslot.common.network.server.SUpdateBackSlotMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

public class ServerHandler {

	public static void handle(CProcessActionMessage message, ServerPlayerEntity player) {
		switch(message.getAction()) {
		case 1:
			if(!player.isSpectator()) {
				player.getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(backSlot -> {
					ItemStack stack = backSlot.getBackStack();
					backSlot.setBackStack(player.getHeldItem(Hand.MAIN_HAND));
					player.setHeldItem(Hand.MAIN_HAND, stack);
					player.resetActiveHand();
					if(!ItemStack.areItemsEqual(stack, backSlot.getBackStack())) BackSlotMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SUpdateBackSlotMessage(player.getEntityId(), backSlot.getBackStack()));
				});
			}
			break;
		case 2:
			NetworkHooks.openGui(player, new INamedContainerProvider() {

				@Override
				public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
					return new BackInventoryContainer(id, inventory);
				}

				@Override
				public ITextComponent getDisplayName() {
					return StringTextComponent.EMPTY;
				}
			});
			break;
		case 3:
			player.closeContainer();
		default:
			break;
		}
	}

}
