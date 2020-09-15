package io.github.championash5357.backslot.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.championash5357.backslot.common.inventory.container.BackInventoryContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BackInventoryScreen extends DisplayEffectsScreen<BackInventoryContainer> implements IRecipeShownListener {

	private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
	private float oldMouseX;
	private float oldMouseY;
	private final RecipeBookGui recipeBookGui = new RecipeBookGui();
	private boolean removeRecipeBookGui;
	private boolean widthTooNarrow;
	private boolean buttonClicked;

	public BackInventoryScreen(BackInventoryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, new TranslationTextComponent("container.crafting"));
		this.passEvents = true;
		this.titleX = 97;
	}

	@Override
	public void tick() {
		if (this.minecraft.playerController.isInCreativeMode()) {
			this.minecraft.displayGuiScreen(new CreativeScreen(this.minecraft.player));
		} else {
			this.recipeBookGui.tick();
		}
	}

	@Override
	protected void init() {
		if (this.minecraft.playerController.isInCreativeMode()) {
			this.minecraft.displayGuiScreen(new CreativeScreen(this.minecraft.player));
		} else {
			super.init();
			this.widthTooNarrow = this.width < 379;
			this.recipeBookGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.container);
			this.removeRecipeBookGui = true;
			this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
			this.children.add(this.recipeBookGui);
			this.setFocusedDefault(this.recipeBookGui);
			this.addButton(new ImageButton(this.guiLeft + 104, this.height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214086_1_) -> {
				this.recipeBookGui.initSearchBar(this.widthTooNarrow);
				this.recipeBookGui.toggleVisibility();
				this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
				((ImageButton)p_214086_1_).setPosition(this.guiLeft + 104, this.height / 2 - 22);
				this.buttonClicked = true;
			}));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.hasActivePotionEffects = !this.recipeBookGui.isVisible();
		if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
			this.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
			this.recipeBookGui.render(matrixStack, mouseX, mouseY, partialTicks);
		} else {
			this.recipeBookGui.render(matrixStack, mouseX, mouseY, partialTicks);
			super.render(matrixStack, mouseX, mouseY, partialTicks);
			this.recipeBookGui.func_230477_a_(matrixStack, this.guiLeft, this.guiTop, false, partialTicks);
		}

		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		this.recipeBookGui.func_238924_c_(matrixStack, this.guiLeft, this.guiTop, mouseX, mouseY);
		this.oldMouseX = (float)mouseX;
		this.oldMouseY = (float)mouseY;
	}

	@Override
	public void recipesUpdated() {
		this.recipeBookGui.recipesUpdated();
	}

	@Override
	public RecipeBookGui getRecipeGui() {
		return this.recipeBookGui;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
		drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.minecraft.player);
	}

	@SuppressWarnings("deprecation")
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity p_228187_5_) {
		float f = -(float)Math.atan((double)(mouseX / 40.0F));
		float f1 = (float)Math.atan((double)(mouseY / 40.0F));
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float)scale, (float)scale, (float)scale);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
		matrixstack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		quaternion.multiply(quaternion1);
		matrixstack.rotate(quaternion);
		float f2 = p_228187_5_.renderYawOffset;
		float f3 = p_228187_5_.rotationYaw;
		float f4 = p_228187_5_.rotationPitch;
		float f5 = p_228187_5_.prevRotationYawHead;
		float f6 = p_228187_5_.rotationYawHead;
		p_228187_5_.renderYawOffset = 180.0F + f * 20.0F;
		p_228187_5_.rotationYaw = 180.0F + f * 40.0F;
		p_228187_5_.rotationPitch = -f1 * 20.0F;
		p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
		p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
		quaternion1.conjugate();
		entityrenderermanager.setCameraOrientation(quaternion1);
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		RenderSystem.runAsFancy(() -> {
			entityrenderermanager.renderEntityStatic(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
		});
		irendertypebuffer$impl.finish();
		entityrenderermanager.setRenderShadow(true);
		p_228187_5_.renderYawOffset = f2;
		p_228187_5_.rotationYaw = f3;
		p_228187_5_.rotationPitch = f4;
		p_228187_5_.prevRotationYawHead = f5;
		p_228187_5_.rotationYawHead = f6;
		RenderSystem.popMatrix();
	}

	@Override
	protected boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {
		return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isPointInRegion(x, y, width, height, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.recipeBookGui.mouseClicked(mouseX, mouseY, button)) {
			this.setListener(this.recipeBookGui);
			return true;
		} else {
			return this.widthTooNarrow && this.recipeBookGui.isVisible() ? false : super.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.buttonClicked) {
			this.buttonClicked = false;
			return true;
		} else {
			return super.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
		boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
		return this.recipeBookGui.func_195604_a(mouseX, mouseY, this.guiLeft, this.guiTop, this.xSize, this.ySize, mouseButton) && flag;
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		this.recipeBookGui.slotClicked(slotIn);
	}

	@Override
	public void onClose() {
		if (this.removeRecipeBookGui) {
			this.recipeBookGui.removed();
		}
		super.onClose();
	}
}
