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

package io.github.championash5357.backslot.api.client;

import io.github.championash5357.backslot.api.client.renderer.BackSlotTransformationManager;

/**
 * Stores all client side resource managers.
 * */
public class ClientManagers {

	private static final ClientManagers INSTANCE = new ClientManagers();
	private final BackSlotTransformationManager transformationManager = new BackSlotTransformationManager();
	
	/**
	 * Gets the transformation manager for the back slot.
	 * 
	 * @return The back slot transformation manager.
	 * */
	public static BackSlotTransformationManager getTransformationManager() {
		return INSTANCE.transformationManager;
	}
}
