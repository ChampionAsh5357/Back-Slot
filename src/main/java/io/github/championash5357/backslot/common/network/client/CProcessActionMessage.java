package io.github.championash5357.backslot.common.network.client;

import java.util.function.Supplier;

import io.github.championash5357.backslot.server.ServerHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CProcessActionMessage {

	private byte action;

	public CProcessActionMessage(byte action) {
		this.action = action;
	}
	
	public byte getAction() {
		return action;
	}

	public static void encode(CProcessActionMessage message, PacketBuffer buffer) {
		buffer.writeByte(message.action);
	}

	public static CProcessActionMessage decode(PacketBuffer buffer) {
		return new CProcessActionMessage(buffer.readByte());
	}

	public static boolean handle(CProcessActionMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> ServerHandler.handle(message, ctx.get().getSender()));
		return true;
	}

	public static class Actions {
		public static final byte SWAP_BACK_SLOT = 0b1;
		public static final byte OPEN_BACK_INVENTORY = 0b10;
		public static final byte OPEN_PLAYER_INVENTORY = 0b11;
	}
}
