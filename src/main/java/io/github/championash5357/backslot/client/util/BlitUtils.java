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

package io.github.championash5357.backslot.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

public class BlitUtils {

	private int blitOffset;

	public static void reverseBlit(MatrixStack matrixStack, int x, int y, int reverseBlitOffset, int width, int height, TextureAtlasSprite sprite) {
		innerReverseBlit(matrixStack.getLast().getMatrix(), x, x + width, y, y + height, reverseBlitOffset, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
	}

	public void reverseBlit(MatrixStack matrixStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
		reverseBlit(matrixStack, x, y, this.blitOffset, (float)uOffset, (float)vOffset, uWidth, vHeight, 256, 256);
	}

	public static void reverseBlit(MatrixStack matrixStack, int x, int y, int reverseBlitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureHeight, int textureWidth) {
		innerReverseBlit(matrixStack, x, x + uWidth, y, y + vHeight, reverseBlitOffset, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
	}

	public static void reverseBlit(MatrixStack matrixStack, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
		innerReverseBlit(matrixStack, x, x + width, y, y + height, 0, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
	}

	public static void reverseBlit(MatrixStack matrixStack, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
		reverseBlit(matrixStack, x, y, width, height, uOffset, vOffset, width, height, textureWidth, textureHeight);
	}

	private static void innerReverseBlit(MatrixStack matrixStack, int x1, int x2, int y1, int y2, int reverseBlitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
		innerReverseBlit(matrixStack.getLast().getMatrix(), x1, x2, y1, y2, reverseBlitOffset, (uOffset + 0.0F) / (float)textureWidth, (uOffset + (float)uWidth) / (float)textureWidth, (vOffset + 0.0F) / (float)textureHeight, (vOffset + (float)vHeight) / (float)textureHeight);
	}

	@SuppressWarnings("deprecation")
	private static void innerReverseBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int reverseBlitOffset, float minU, float maxU, float minV, float maxV) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(matrix, (float)x1, (float)y2, (float)reverseBlitOffset).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(matrix, (float)x2, (float)y2, (float)reverseBlitOffset).tex(minU, maxV).endVertex();
		bufferbuilder.pos(matrix, (float)x2, (float)y1, (float)reverseBlitOffset).tex(minU, minV).endVertex();
		bufferbuilder.pos(matrix, (float)x1, (float)y1, (float)reverseBlitOffset).tex(maxU, minV).endVertex();
		bufferbuilder.finishDrawing();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(bufferbuilder);
	}

	public int getBlitOffset() {
		return this.blitOffset;
	}

	public void setBlitOffset(int value) {
		this.blitOffset = value;
	}
}
