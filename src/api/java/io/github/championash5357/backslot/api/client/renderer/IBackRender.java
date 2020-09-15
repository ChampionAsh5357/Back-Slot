package io.github.championash5357.backslot.api.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

/**
 * Allows the user to specify custom offsets for a specific item.
 * Do note that the original offsets are still applied by default.
 * */
@FunctionalInterface
public interface IBackRender {

	/**
	 * Sets the offset of the matrix stack.
	 * 
	 * @param stackIn
	 * 			The {@link MatrixStack} being modified
	 * */
	void offset(MatrixStack stackIn);
}