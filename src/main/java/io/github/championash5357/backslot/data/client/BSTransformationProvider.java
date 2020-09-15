package io.github.championash5357.backslot.data.client;

import java.util.function.Consumer;

import io.github.championash5357.backslot.api.data.client.BackSlotTransformationProvider;
import io.github.championash5357.backslot.api.data.client.TransformationBuilder;
import net.minecraft.data.DataGenerator;

/**TODO: Fix all not good items*/
public class BSTransformationProvider extends BackSlotTransformationProvider {

	public BSTransformationProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void registerTransformations(Consumer<TransformationBuilder> consumer) {
		
	}
}
