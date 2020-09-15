package io.github.championash5357.backslot.common.network;

import io.github.championash5357.backslot.common.BackSlotMain;
import io.github.championash5357.backslot.common.network.client.CProcessActionMessage;
import io.github.championash5357.backslot.common.network.server.SUpdateBackSlotMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class BSNetwork {

	public static final ResourceLocation CHANNEL = new ResourceLocation(BackSlotMain.ID, "network");
	public static final String VERSION = new ResourceLocation(BackSlotMain.ID, "1").toString();
	
	public static SimpleChannel network() {
		int id = 0;
		final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL)
				.clientAcceptedVersions(version -> true)
				.serverAcceptedVersions(version -> true)
				.networkProtocolVersion(() -> VERSION)
				.simpleChannel();
		
		channel.messageBuilder(SUpdateBackSlotMessage.class, ++id, NetworkDirection.PLAY_TO_CLIENT)
		.encoder(SUpdateBackSlotMessage::encode)
		.decoder(SUpdateBackSlotMessage::decode)
		.consumer(SUpdateBackSlotMessage::handle)
		.add();
		
		channel.messageBuilder(CProcessActionMessage.class, ++id, NetworkDirection.PLAY_TO_SERVER)
		.encoder(CProcessActionMessage::encode)
		.decoder(CProcessActionMessage::decode)
		.consumer(CProcessActionMessage::handle)
		.add();
		
		return channel;
	}
}
