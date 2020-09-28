/*
 * Back Slot
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

package io.github.championash5357.backslot.common.network.client;

import java.util.function.Supplier;

import io.github.championash5357.backslot.common.network.IMessage;
import io.github.championash5357.backslot.server.ServerHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CProcessActionMessage implements IMessage {

	private byte action;

	public CProcessActionMessage(byte action) {
		this.action = action;
	}
	
	public byte getAction() {
		return action;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeByte(this.action);
	}

	public static CProcessActionMessage decode(PacketBuffer buffer) {
		return new CProcessActionMessage(buffer.readByte());
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> ServerHandler.handle(this, ctx.get().getSender()));
		return true;
	}

	public static class Actions {
		public static final byte SWAP_BACK_SLOT = 0b1;
		public static final byte OPEN_BACK_INVENTORY = 0b10;
		public static final byte OPEN_PLAYER_INVENTORY = 0b11;
	}
}
