package io.github.championash5357.backslot.api.client.renderer;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.common.model.TransformationHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * The manager that holds all information relating to the 
 * transformations of the back slot item.
 * */
public class BackSlotTransformationManager extends JsonReloadListener {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder())
			.registerTypeAdapter(TransformationMatrix.class, new TransformationHelper.Deserializer())
			.setPrettyPrinting().disableHtmlEscaping().create();
	private Map<Item, IBackRender> renderers = ImmutableMap.of();
	private static final IBackRender EMPTY = (stack) -> {};
	
	public BackSlotTransformationManager() {
		super(GSON, "back_slot");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
		ImmutableMap.Builder<Item, IBackRender> builder = ImmutableMap.builder();

		for(Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
			ResourceLocation location = entry.getKey();
			Item item = ForgeRegistries.ITEMS.getValue(location);
			if(item != null) {
				try {
					builder.put(item, new BackRender(GSON.fromJson(entry.getValue(), TransformationMatrix.class)));
				} catch (JsonParseException e) {
					LOGGER.error("Parsing error loading item {}", location);
				}
			} else {
				throw new NullPointerException("Item " + location + " does not exist!");
			}
		}
		
		this.renderers = builder.build();
		LOGGER.info("Loaded {} back renders", renderers.size());
	}
	
	/**
	 * Gets the render if present. If not, 
	 * returns an identity instance.
	 * 
	 * @param item
	 * 			The associated item.
	 * @return The back render of the item.
	 * */
	public IBackRender getRender(Item item) {
		return renderers.getOrDefault(item, EMPTY);
	}
}
