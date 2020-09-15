package io.github.championash5357.backslot.common.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityProviderSerializable<HANDLER> extends CapabilityProviderSimple<HANDLER> implements INBTSerializable<INBT> {

	public CapabilityProviderSerializable(Capability<HANDLER> capability, @Nullable Direction direction) {
		super(capability, capability.getDefaultInstance(), direction);
	}
	
	public CapabilityProviderSerializable(Capability<HANDLER> capability, @Nullable HANDLER instance, @Nullable Direction direction) {
		super(capability, instance, direction);
	}

	@Override
	public INBT serializeNBT() {
		return this.getInstance() == null ? null : this.getCapability().writeNBT(this.getInstance(), this.getDirection());
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		if(this.getInstance() == null) return;
		this.getCapability().readNBT(this.getInstance(), this.getDirection(), nbt);
	}
}
