package io.github.championash5357.backslot.client;

import java.util.Map.Entry;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.lwjgl.glfw.GLFW;

import io.github.championash5357.backslot.api.client.ClientManagers;
import io.github.championash5357.backslot.client.gui.screen.inventory.BackInventoryScreen;
import io.github.championash5357.backslot.client.gui.widget.button.ExtendedImageButton;
import io.github.championash5357.backslot.client.renderer.entity.layer.PlayerBackItemLayer;
import io.github.championash5357.backslot.common.BackSlotMain;
import io.github.championash5357.backslot.common.ISidedReference;
import io.github.championash5357.backslot.common.init.BSContainerTypes;
import io.github.championash5357.backslot.common.inventory.container.BackInventoryContainer;
import io.github.championash5357.backslot.common.network.client.CProcessActionMessage;
import io.github.championash5357.backslot.common.network.client.CProcessActionMessage.Actions;
import io.github.championash5357.backslot.common.util.TranslationStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.VersionChecker.CheckResult;
import net.minecraftforge.fml.VersionChecker.Status;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.forgespi.language.IModInfo;

public class ClientReference implements ISidedReference {

	public final KeyBinding swapBackSlot = new KeyBinding(TranslationStrings.SWAP_BACK_SLOT_KEY, KeyConflictContext.IN_GAME, Type.KEYSYM, GLFW.GLFW_KEY_G, TranslationStrings.BACK_SLOT_CATEGORY);
	private static final ResourceLocation BACK_SLOT_BUTTON = new ResourceLocation(BackSlotMain.ID, "textures/gui/back_button.png");
	private Minecraft mc;

	@Override
	public void setup(IEventBus mod, IEventBus forge) {
		mod.addListener(this::construct);
		mod.addListener(this::clientSetup);
		mod.addListener(this::enqueueMessage);
		mod.addListener(this::stitchTexture);
		forge.addListener(this::clientLoggedIn);
		forge.addListener(this::keyInput);
		forge.addListener(this::mouseInput);
		forge.addListener(this::screenInit);
	}

	private void construct(final FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(ClientManagers.getTransformationManager());
		});
	}

	private void enqueueMessage(final InterModEnqueueEvent event) {
		ModList.get().forEachModContainer((string, container) -> InterModComms.sendTo(string, "enableRendering", () -> ClientConfigHolder.CLIENT.enableRendering.get())); 
	}

	private void clientLoggedIn(final ClientPlayerNetworkEvent.LoggedInEvent event) {
		IModInfo info = ModList.get().getModContainerById(BackSlotMain.ID).get().getModInfo();
		CheckResult result = VersionChecker.getResult(info);
		if (ClientConfigHolder.CLIENT.enableUpdateNotifications.get() && (result.status == Status.OUTDATED || result.status == Status.BETA_OUTDATED)) {
			TextComponent clickableVersion = new StringTextComponent(TextFormatting.GOLD + "" + result.target);
			clickableVersion.setStyle(clickableVersion.getStyle().setClickEvent(new ClickEvent(Action.OPEN_URL, result.url)));
			event.getPlayer().sendMessage(new TranslationTextComponent("notification.backslot.outdated", new StringTextComponent(TextFormatting.RED + "Back " + TextFormatting.BLUE + "Slot")).mergeStyle(TextFormatting.GRAY), Util.DUMMY_UUID);
			event.getPlayer().sendMessage(new TranslationTextComponent("notification.backslot.current_version", clickableVersion).mergeStyle(TextFormatting.GRAY), Util.DUMMY_UUID);
			event.getPlayer().sendMessage(new TranslationTextComponent("notification.backslot.changelog").mergeStyle(TextFormatting.WHITE), Util.DUMMY_UUID);
			for(Entry<ComparableVersion, String> entry : result.changes.entrySet()) {
				event.getPlayer().sendMessage(new StringTextComponent(" - " + TextFormatting.GOLD + entry.getKey() + ": " + TextFormatting.GRAY + entry.getValue()), Util.DUMMY_UUID);
			}
		}
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		this.mc = event.getMinecraftSupplier().get();
		ClientRegistry.registerKeyBinding(swapBackSlot);
		event.enqueueWork(() -> {
			ScreenManager.registerFactory(BSContainerTypes.BACK_INVENTORY.get(), BackInventoryScreen::new);
			if(ClientConfigHolder.CLIENT.enableRendering.get()) this.mc.getRenderManager().getSkinMap().forEach((name, renderer) -> renderer.addLayer(new PlayerBackItemLayer<>(renderer)));
		});
	}

	private void stitchTexture(final TextureStitchEvent.Pre event) {
		event.addSprite(BackInventoryContainer.EMPTY_BACK_SLOT);
	}

	private void screenInit(final GuiScreenEvent.InitGuiEvent.Post event) {
		if(ClientConfigHolder.CLIENT.enableGui.get()) {
			Screen screen = event.getGui();
			if(screen instanceof InventoryScreen) {
				event.addWidget(new ExtendedImageButton((ContainerScreen<?>) screen, ClientConfigHolder.CLIENT.xOffset.get(), ClientConfigHolder.CLIENT.yOffset.get(), 17, 17, 17, 34, 0, 0, 17, BACK_SLOT_BUTTON, new TranslationTextComponent(TranslationStrings.TO_BACK_BUTTON), false, (button) -> {
					BackSlotMain.NETWORK.sendToServer(new CProcessActionMessage(Actions.OPEN_BACK_INVENTORY));
				}, (button, stack, x, y) -> {
					event.getGui().renderTooltip(stack, new TranslationTextComponent(TranslationStrings.TO_BACK_BUTTON), x, y);
				}));
			} else if(event.getGui() instanceof BackInventoryScreen) {
				event.addWidget(new ExtendedImageButton((ContainerScreen<?>) screen, ClientConfigHolder.CLIENT.xOffset.get(), ClientConfigHolder.CLIENT.yOffset.get(), 17, 17, 17, 34, 0, 0, 17, BACK_SLOT_BUTTON, new TranslationTextComponent(TranslationStrings.TO_FRONT_BUTTON), true, (button) -> {
					BackSlotMain.NETWORK.sendToServer(new CProcessActionMessage(Actions.OPEN_PLAYER_INVENTORY));
					screen.getMinecraft().displayGuiScreen(new InventoryScreen(screen.getMinecraft().player));
				}, (button, stack, x, y) -> {
					event.getGui().renderTooltip(stack, new TranslationTextComponent(TranslationStrings.TO_FRONT_BUTTON), x, y);
				}));
			}
		}
	}

	private void keyInput(final KeyInputEvent event) {
		if(mc.player != null && !mc.player.isSpectator()) {
			if(swapBackSlot.isPressed()) {
				BackSlotMain.NETWORK.sendToServer(new CProcessActionMessage(Actions.SWAP_BACK_SLOT));
			}
		}
	}

	private void mouseInput(final MouseInputEvent event) {
		if(mc.player != null && !mc.player.isSpectator()) {
			if(swapBackSlot.isPressed()) {
				BackSlotMain.NETWORK.sendToServer(new CProcessActionMessage(Actions.SWAP_BACK_SLOT));
			}
		}
	}
}
