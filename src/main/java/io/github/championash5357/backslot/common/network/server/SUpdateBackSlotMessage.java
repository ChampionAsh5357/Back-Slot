package io.github.championash5357.backslot.common.network.server;

import java.util.function.Supplier;

import io.github.championash5357.backslot.client.ClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SUpdateBackSlotMessage {

	private int entityID;
	private ItemStack stack;

	public SUpdateBackSlotMessage(int entityID, ItemStack stack) {
		this.entityID = entityID;
		this.stack = stack;
	}

	public int getEntityID() {
		return entityID;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public static void encode(SUpdateBackSlotMessage message, PacketBuffer buffer) {
		buffer.writeVarInt(message.entityID);
		buffer.writeItemStack(message.stack);
	}

	public static SUpdateBackSlotMessage decode(PacketBuffer buffer) {
		return new SUpdateBackSlotMessage(buffer.readVarInt(), buffer.readItemStack());
	}

	public static boolean handle(SUpdateBackSlotMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handle(message, LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide()))));
		return true;
	}
}
