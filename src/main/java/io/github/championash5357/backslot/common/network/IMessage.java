package io.github.championash5357.backslot.common.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IMessage {
	
	void encode(PacketBuffer buffer);
	
	boolean handle(Supplier<NetworkEvent.Context> ctx);
}
