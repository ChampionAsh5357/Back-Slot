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

package io.github.championash5357.backslot.api.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.extensions.IForgeTransformationMatrix;

/**
 * A basic implementation of {@link IBackRender} using 
 * {@link TransformationMatrix}es to store the values. 
 * Allows for an easy way to grab and translate the back 
 * item similarly to how its done for block/item models.
 * */
public class BackRender implements IBackRender {

	private final TransformationMatrix transformationMatrix;

	/**
	 * A constructor taking in an array of transformation matrixes. 
	 * Used for grabbing from a file.
	 * 
	 * @param transformationMatrixes
	 * 			An array of matrixes
	 * */
	public BackRender(TransformationMatrix transformationMatrix) {
		this.transformationMatrix = transformationMatrix;
	}

	@Override
	public void offset(MatrixStack stackIn) {
		modifyStack(transformationMatrix, stackIn);
	}

	/**
	 * Translates the matrix stack similarly to {@link IForgeTransformationMatrix#push(MatrixStack)} 
	 * without the extra push.
	 * */
	private void modifyStack(TransformationMatrix matrix, MatrixStack stack) {
		Vector3f trans = matrix.getTranslation();
		stack.translate(trans.getX(), trans.getY(), trans.getZ());

		stack.rotate(matrix.getRotationLeft());

		Vector3f scale = matrix.getScale();
		stack.scale(scale.getX(), scale.getY(), scale.getZ());

		stack.rotate(matrix.getRightRot());
	}
}
