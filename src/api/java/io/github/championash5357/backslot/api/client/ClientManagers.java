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
