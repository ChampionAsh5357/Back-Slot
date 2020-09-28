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

package io.github.championash5357.backslot.common.network.server;

import java.util.function.Supplier;

import io.github.championash5357.backslot.client.ClientHandler;
import io.github.championash5357.backslot.common.network.IMessage;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SUpdateBackSlotMessage implements IMessage {

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
	
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(this.entityID);
		buffer.writeItemStack(this.stack);
	}

	public static SUpdateBackSlotMessage decode(PacketBuffer buffer) {
		return new SUpdateBackSlotMessage(buffer.readVarInt(), buffer.readItemStack());
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handle(this, LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide()))));
		return true;
	}
}
