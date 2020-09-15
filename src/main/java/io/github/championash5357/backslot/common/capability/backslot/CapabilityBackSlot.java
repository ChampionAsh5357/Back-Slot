package io.github.championash5357.backslot.common.capability.backslot;

import io.github.championash5357.backslot.api.common.capability.IBackSlot;
import io.github.championash5357.backslot.common.BackSlotMain;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityBackSlot {
	
	public static final ResourceLocation BACK_SLOT = new ResourceLocation(BackSlotMain.ID, "back_slot");
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IBackSlot.class, new IStorage<IBackSlot>() {

			@Override
			public INBT writeNBT(Capability<IBackSlot> capability, IBackSlot instance, Direction side) {
				return instance.getBackStack().write(new CompoundNBT());
			}

			@Override
			public void readNBT(Capability<IBackSlot> capability, IBackSlot instance, Direction side, INBT nbt) {
				if(!(nbt instanceof CompoundNBT)) return;
				instance.setBackStack(ItemStack.read((CompoundNBT) nbt));
			}
		}, BackSlot::new);
	}
}
