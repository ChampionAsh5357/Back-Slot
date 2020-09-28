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

package io.github.championash5357.backslot.api.common.inventory.slot;

import javax.annotation.Nonnull;

import io.github.championash5357.backslot.api.common.capability.IBackSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class BackItemSlot extends Slot {

	private static IInventory emptyInventory = new Inventory(0);
	private final IBackSlot backSlot;

	public BackItemSlot(IBackSlot backSlot, int xPosition, int yPosition) {
		super(emptyInventory, 0, xPosition, yPosition);
		this.backSlot = backSlot;
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		if (stack.isEmpty())
			return false;
		return this.getBackSlot().isItemValid(stack);
	}

	@Override
	@Nonnull
	public ItemStack getStack() {
		return this.getBackSlot().getBackStack();
	}

	@Override
	public void putStack(@Nonnull ItemStack stack) {
		this.getBackSlot().setBackStack(stack);
		this.onSlotChanged();
	}

	@Override
	public void onSlotChange(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {}

	@Override
	public int getSlotStackLimit() {
		return this.getBackSlot().getSlotLimit();
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack) {
		ItemStack maxAdd = stack.copy();
		int maxInput = stack.getMaxStackSize();
		maxAdd.setCount(maxInput);

		IBackSlot handler = this.getBackSlot();
		ItemStack currentStack = handler.getBackStack();
		handler.setBackStack(ItemStack.EMPTY);

		ItemStack remainder = handler.insertItem(maxAdd, true);

		handler.setBackStack(currentStack);

		return maxInput - remainder.getCount();
	}
	
	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		return !this.getBackSlot().extractItem(1, true).isEmpty();
	}
	
	@Override
	@Nonnull
	public ItemStack decrStackSize(int amount) {
		return this.getBackSlot().extractItem(amount, false);
	}

	public IBackSlot getBackSlot() {
		return backSlot;
	}
	
	@Override
	public boolean isSameInventory(Slot other) {
		return other instanceof IBackSlot && other == this.getBackSlot();
	}
}
