package io.github.championash5357.backslot.common.init;

import io.github.championash5357.backslot.common.BackSlotMain;
import io.github.championash5357.backslot.common.inventory.container.BackInventoryContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BSContainerTypes {

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, BackSlotMain.ID);
	
	public static final RegistryObject<ContainerType<BackInventoryContainer>> BACK_INVENTORY = CONTAINER_TYPES.register("back_inventory", () -> new ContainerType<>(BackInventoryContainer::new));
}
