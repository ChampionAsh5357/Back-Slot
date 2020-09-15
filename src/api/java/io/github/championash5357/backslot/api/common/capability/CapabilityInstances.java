package io.github.championash5357.backslot.api.common.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Holds a list of public capability instances used by this mod.
 * */
public class CapabilityInstances {

	/**{@link IBackSlot} instance, should be present when loaded.*/
	@CapabilityInject(IBackSlot.class)
	public static final Capability<IBackSlot> BACK_SLOT_CAPABILITY = null;
}
