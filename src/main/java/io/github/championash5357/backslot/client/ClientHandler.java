package io.github.championash5357.backslot.client;

import java.util.Optional;

import io.github.championash5357.backslot.api.common.capability.CapabilityInstances;
import io.github.championash5357.backslot.common.network.server.SUpdateBackSlotMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class ClientHandler {

	public static void handle(SUpdateBackSlotMessage message, Optional<World> opt) {
		opt.ifPresent(world -> {
			Entity entity = world.getEntityByID(message.getEntityID());
			if(entity != null && entity instanceof LivingEntity) {
				entity.getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(backSlot -> backSlot.setBackStack(message.getStack()));
			}
		});
	}

}
