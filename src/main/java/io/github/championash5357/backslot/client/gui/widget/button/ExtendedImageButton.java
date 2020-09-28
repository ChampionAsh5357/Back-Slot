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

package io.github.championash5357.backslot.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.championash5357.backslot.client.util.BlitUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ExtendedImageButton extends Button {

	private final ContainerScreen<?> screen;
	private final ResourceLocation resourceLocation;
	private final int xTexStart;
	private final int yTexStart;
	private final int yDiffText;
	private final int textureWidth;
	private final int textureHeight;
	private final boolean reverse;
	private boolean wasHovered;

	public ExtendedImageButton(ContainerScreen<?> screen, int x, int y, int width, int height, int textureWidth, int textureHeight, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, ITextComponent title, boolean reverse, IPressable pressedAction, ITooltip onTooltip) {
		super(x, y, width, height, title, pressedAction, onTooltip);
		this.screen = screen;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.xTexStart = xTexStart;
		this.yTexStart = yTexStart;
		this.yDiffText = yDiffText;
		this.resourceLocation = resourceLocation;
		this.reverse = reverse;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			this.isHovered = mouseX >= (this.x + screen.getGuiLeft()) && mouseY >= (this.y + screen.getGuiTop()) && mouseX < (this.x + this.width + screen.getGuiLeft()) && mouseY < (this.y + this.height + screen.getGuiTop());
			if (this.wasHovered != this.isHovered()) {
				if (this.isHovered()) {
					if (this.isFocused()) {
						this.queueNarration(200);
					} else {
						this.queueNarration(750);
					}
				} else {
					this.nextNarration = Long.MAX_VALUE;
				}
			}

			if (this.visible) {
				this.renderButton(matrixStack, mouseX, mouseY, partialTicks);
			}

			this.narrate();
			this.wasHovered = this.isHovered();
		}
	}

	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(this.resourceLocation);
		int i = this.yTexStart;
		if (this.isHovered()) {
			i += this.yDiffText;
		}

		RenderSystem.enableDepthTest();
		matrixStack.push();
		if(reverse) BlitUtils.reverseBlit(matrixStack, screen.getGuiLeft() + this.x, screen.getGuiTop() + this.y, (float)this.xTexStart, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
		else blit(matrixStack, screen.getGuiLeft() + this.x, screen.getGuiTop() + this.y, (float)this.xTexStart, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
		matrixStack.pop();
		if(this.isHovered()) {
			this.renderToolTip(matrixStack, mouseX, mouseY);
		}
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return this.active && this.visible && mouseX >= (double)(this.x + screen.getGuiLeft()) && mouseY >= (double)(this.y + screen.getGuiTop()) && mouseX < (double)(this.x + this.width + screen.getGuiLeft()) && mouseY < (double)(this.y + this.height + screen.getGuiTop());
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return this.active && this.visible && mouseX >= (double)(this.x + screen.getGuiLeft()) && mouseY >= (double)(this.y + screen.getGuiTop()) && mouseX < (double)(this.x + this.width + screen.getGuiLeft()) && mouseY < (double)(this.y + this.height + screen.getGuiTop());
	}
}
