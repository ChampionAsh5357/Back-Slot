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

package io.github.championash5357.backslot.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.github.championash5357.backslot.api.client.ClientManagers;
import io.github.championash5357.backslot.api.client.renderer.IBackRender;
import io.github.championash5357.backslot.api.common.capability.CapabilityInstances;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class PlayerBackItemLayer<T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {

	public PlayerBackItemLayer(IEntityRenderer<T, M> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		entitylivingbaseIn.getCapability(CapabilityInstances.BACK_SLOT_CAPABILITY).ifPresent(backSlot -> {
			ItemStack stack = backSlot.getBackStack();
			if(!stack.isEmpty()) {
				Item item = stack.getItem();
				IBackRender render = ClientManagers.getTransformationManager().getRender(item);
				matrixStackIn.push();
				if(entitylivingbaseIn.isChild()) {
					matrixStackIn.translate(0.0D, 0.75D, 0.0D);
		            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
				}
				
				matrixStackIn.push();
				this.getEntityModel().bipedBody.translateRotate(matrixStackIn);
				ItemStack chest = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.CHEST);
				if(!chest.isEmpty() && chest.getItem() instanceof ArmorItem) matrixStackIn.translate(0.0, 0.0, 0.0625);
				render.offset(matrixStackIn);
				if(item instanceof BlockItem) {
					matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
					if(((BlockItem)item).getBlock() instanceof AbstractSkullBlock) {
						matrixStackIn.translate(0.0d, 0.0d, 0.375d);
					} else {
						matrixStackIn.scale(0.5f, 0.5f, 0.5f);
						matrixStackIn.translate(0.0d, -0.5d, 0.75d);
					}
				} else {
					matrixStackIn.translate(0.0d, 0.25d, 0.15625d);
				}
				Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entitylivingbaseIn, stack, TransformType.NONE, false, matrixStackIn, bufferIn, packedLightIn);
				matrixStackIn.pop();
				matrixStackIn.pop();
			}
		});
	}
}
