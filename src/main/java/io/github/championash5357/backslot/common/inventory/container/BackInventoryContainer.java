package io.github.championash5357.backslot.common.inventory.container;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import io.github.championash5357.backslot.api.common.capability.CapabilityInstances;
import io.github.championash5357.backslot.api.common.inventory.slot.BackItemSlot;
import io.github.championash5357.backslot.common.BackSlotMain;
import io.github.championash5357.backslot.common.init.BSContainerTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BackInventoryContainer extends RecipeBookContainer<CraftingInventory> {

	private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS, PlayerContainer.EMPTY_ARMOR_SLOT_LEGGINGS, PlayerContainer.EMPTY_ARMOR_SLOT_CHESTPLATE, PlayerContainer.EMPTY_ARMOR_SLOT_HELMET};
	private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	public static final ResourceLocation EMPTY_BACK_SLOT = new ResourceLocation(BackSlotMain.ID, "item/empty_back_slot");
	private final CraftingInventory craftMatrix = new CraftingInventory(this, 2, 2);
	private final CraftResultInventory craftResult = new CraftResultInventory();
	private final PlayerEntity player;
	
	public BackInventoryContainer(int id, PlayerInventory inventory) {
		super(BSContainerTypes.BACK_INVENTORY.get(), id);
		this.player = inventory.player;
		this.addSlot(new CraftingResultSlot(inventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));

		for(int i = 0; i < 2; ++i) {
			for(int j = 0; j < 2; ++j) {
				this.addSlot(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
			}
		}

		for(int k = 0; k < 4; ++k) {
			final EquipmentSlotType equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
			this.addSlot(new Slot(inventory, 39 - k, 8, 8 + k * 18) {

				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack) {
					return stack.canEquip(equipmentslottype, player);
				}

				@Override
				public boolean canTakeStack(PlayerEntity playerIn) {
					ItemStack itemstack = this.getStack();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
				}

				@Override
				public Pair<ResourceLocation, ResourceLocation> getBackground() {
					return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, ARMOR_SLOT_TEXTURES[equipmentslottype.getIndex()]);
				}
			});
		}

		for(int l = 0; l < 3; ++l) {
			for(int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
			}
		}

		for(int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
		}

		player.getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(backSlot -> {
			this.addSlot(new BackItemSlot(backSlot, 77, 62) {
				@Override
				public Pair<ResourceLocation, ResourceLocation> getBackground() {
					return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, EMPTY_BACK_SLOT);
				}
			});
		});
	}

	@Override
	public void fillStackedContents(RecipeItemHelper itemHelperIn) {
		this.craftMatrix.fillStackedContents(itemHelperIn);
	}

	@Override
	public void clear() {
		this.craftResult.clear();
		this.craftMatrix.clear();
	}

	@Override
	public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
		return recipeIn.matches(this.craftMatrix, this.player.world);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		updateCraftingResult(this.windowId, this.player.world, this.player, this.craftMatrix, this.craftResult);
	}

	protected static void updateCraftingResult(int id, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
		if (!world.isRemote) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
			if (optional.isPresent()) {
				ICraftingRecipe icraftingrecipe = optional.get();
				if (inventoryResult.canUseRecipe(world, serverplayerentity, icraftingrecipe)) {
					itemstack = icraftingrecipe.getCraftingResult(inventory);
				}
			}

			inventoryResult.setInventorySlotContents(0, itemstack);
			serverplayerentity.connection.sendPacket(new SSetSlotPacket(id, 0, itemstack));
		}
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.craftResult.clear();
		if (!playerIn.world.isRemote) {
			this.clearContainer(playerIn, playerIn.world, this.craftMatrix);
		}
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemstack);
			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 1 && index < 5) {
				if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 5 && index < 9) {
				if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (equipmentslottype.getSlotType() == EquipmentSlotType.Group.ARMOR && !this.inventorySlots.get(8 - equipmentslottype.getIndex()).getHasStack()) {
				int i = 8 - equipmentslottype.getIndex();
				if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.inventorySlots.get(45).getHasStack()) {
				if (!this.mergeItemStack(itemstack1, 45, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 9 && index < 36) {
				if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 36 && index < 45) {
				if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}

	@Override
	public int getOutputSlot() {
		return 0;
	}

	@Override
	public int getWidth() {
		return this.craftMatrix.getWidth();
	}

	@Override
	public int getHeight() {
		return this.craftMatrix.getHeight();
	}

	@Override
	public int getSize() {
		return 5;
	}

	@Override
	public RecipeBookCategory func_241850_m() {
		return RecipeBookCategory.CRAFTING;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
}
