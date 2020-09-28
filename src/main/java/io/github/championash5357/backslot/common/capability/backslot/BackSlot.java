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

package io.github.championash5357.backslot.common.capability.backslot;

import javax.annotation.Nonnull;

import io.github.championash5357.backslot.api.common.capability.IBackSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class BackSlot implements IBackSlot {

	private ItemStack stack;
	
	public BackSlot() {
		this(ItemStack.EMPTY);
	}
	
	public BackSlot(@Nonnull ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public void setBackStack(@Nonnull ItemStack stack) {
		this.stack = stack;
		onContentsChanged();
	}

	@Override
	@Nonnull
	public ItemStack getBackStack() {
		return stack;
	}

	public void onContentsChanged() {}

	@Override
	@Nonnull
	public ItemStack insertItem(ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
            return ItemStack.EMPTY;
            
        if (!isItemValid(stack))
            return stack;

        ItemStack existing = this.stack;

        int limit = getStackLimit(stack);

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                this.stack = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
            }
            else
            {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged();
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
	}

	protected int getStackLimit(@Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(), stack.getMaxStackSize());
    }
	
	@Override
	@Nonnull
	public ItemStack extractItem(int amount, boolean simulate) {
		if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existing = this.stack;

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
                this.stack = ItemStack.EMPTY;
                onContentsChanged();
                return existing;
            }
            else
            {
                return existing.copy();
            }
        }
        else
        {
            if (!simulate)
            {
                this.stack = ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract);
                onContentsChanged();
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
	}

	@Override
	public int getSlotLimit() {
		return 64;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return true;
	}
}
