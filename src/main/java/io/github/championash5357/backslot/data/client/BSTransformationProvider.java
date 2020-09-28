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
