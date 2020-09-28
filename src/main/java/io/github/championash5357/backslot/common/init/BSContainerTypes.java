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
