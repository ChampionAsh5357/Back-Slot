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

package io.github.championash5357.backslot.api.common.capability;

import javax.annotation.Nonnull;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * A capability which allow the user to attach a back 
 * slot to all living entities. This is because all 
 * living entities already have an item handler capability 
 * attached. This is pretty much a carbon copy of {@link IItemHandler} 
 * with the exception of holding a single item.
 * */
public interface IBackSlot {

	/**
	 * Sets the back stack slot.
	 * 
	 * @param stack
	 * 			The associated {@link ItemStack}
	 * */
	void setBackStack(@Nonnull ItemStack stack);

	/**
	 * Gets the stack in the back slot.
	 * 
	 * @return The associated {@link ItemStack}
	 * */
	@Nonnull
	ItemStack getBackStack();

	/**
	 * <p>
	 * Inserts an ItemStack into the back slot and returns the remainder.
	 * The ItemStack <em>should not</em> be modified in this function!
	 * </p>
	 *
	 * @param stack    ItemStack to insert. This must not be modified by the item handler.
	 * @param simulate If true, the insertion is only simulated
	 * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return an empty ItemStack).
	 *         May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
	 *         The returned ItemStack can be safely modified after.
	 **/
	@Nonnull
	ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate);

	/**
	 * Extracts an ItemStack from the back slot.
	 * <p>
	 * The returned value must be empty if nothing is extracted,
	 * otherwise its stack size must be less than or equal to {@code amount} and {@link ItemStack#getMaxStackSize()}.
	 * </p>
	 *
	 * @param amount   Amount to extract (may be greater than the current stack's max limit)
	 * @param simulate If true, the extraction is only simulated
	 * @return ItemStack extracted from the slot, must be empty if nothing can be extracted.
	 *         The returned ItemStack can be safely modified after, so item handlers should return a new or copied stack.
	 **/
	@Nonnull
	ItemStack extractItem(int amount, boolean simulate);

	/**
	 * Retrieves the maximum stack size allowed to exist in the back slot.
	 *
	 * @return The maximum stack size allowed in the back.
	 */
	int getSlotLimit();

	/**
	 * <p>
	 * This function re-implements the vanilla function {@link IInventory#isItemValidForSlot(int, ItemStack)}.
	 * It should be used instead of simulated insertions in cases where the contents and state of the inventory are
	 * irrelevant, mainly for the purpose of automation and logic (for instance, testing if a minecart can wait
	 * to deposit its items into a full inventory, or if the items in the minecart can never be placed into the
	 * inventory and should move on).
	 * </p>
	 * <ul>
	 * <li>isItemValid is false when insertion of the item is never valid.</li>
	 * <li>When isItemValid is true, no assumptions can be made and insertion must be simulated case-by-case.</li>
	 * <li>The actual items in the inventory, its fullness, or any other state are <strong>not</strong> considered by isItemValid.</li>
	 * </ul>
	 * @param stack   Stack to test with for validity
	 *
	 * @return true if the ItemStack can be inserted, not considering the current state of the inventory.
	 *         false if the ItemStack can never inserted in any situation.
	 */
	boolean isItemValid(@Nonnull ItemStack stack);
}
