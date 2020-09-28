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

package io.github.championash5357.backslot.api.data.client;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

/**
 * This is a provider used to generate back slot transformations.
 * */
public abstract class BackSlotTransformationProvider implements IDataProvider {

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private static final Logger LOGGER = LogManager.getLogger();
	private final DataGenerator gen;

	public BackSlotTransformationProvider(DataGenerator gen) {
		this.gen = gen;
	}

	/**
	 * Override this to register your transformations.
	 * */
	protected abstract void registerTransformations(Consumer<TransformationBuilder> consumer);

	@Override
	public void act(DirectoryCache cache) throws IOException {
		Path outputFolder = this.gen.getOutputFolder();
		Set<ResourceLocation> items = new HashSet<>();
		registerTransformations((builder) -> {
			if(!items.add(builder.getItemName())) {
				throw new IllegalStateException("Duplicate transformation: " + builder.getItemName());
			} else {
				try {
					IDataProvider.save(GSON, cache, builder.serialize(), resolvePath(outputFolder, builder.getItemName()));
				} catch (IOException e) {
					 LOGGER.error("Couldn't save transformation {}", builder.getItemName(), e);
				}
			}
		});
	}

	@Override
	public String getName() {
		return "Back Slot Transformations";
	}

	private Path resolvePath(Path path, ResourceLocation item) {
		return path.resolve("assets/" + item.getNamespace() + "/back_slot/" + item.getPath() + ".json");
	}
}
