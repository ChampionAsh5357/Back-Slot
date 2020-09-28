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

package io.github.championash5357.backslot.client;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ClientConfigHolder {

	//General Settings
	public final BooleanValue enableUpdateNotifications;
	
	//GUI Settings
	public final BooleanValue enableGui;
	public final IntValue xOffset, yOffset;
	
	//Render Settings
	public final BooleanValue enableRendering;

	public ClientConfigHolder(final ForgeConfigSpec.Builder builder) {
		builder.comment("Back Slot Configurations").push("backslot");
		
		builder.comment("General Settings").push("general");
		
		enableUpdateNotifications = builder
				.comment("Set this to true if you want update notifications about the mod on world load.")
				.translation("backslot.configgui.enableupdatenotifications")
				.define("enableUpdateNotifications", true);
		
		builder.pop();
		builder.comment("GUI Settings").push("gui");

		enableGui = builder
				.comment("Set this to true if you want to enable access to the GUI.")
				.translation("backslot.configgui.enablegui")
				.define("enableGui", true);
		
		xOffset = builder
				.comment("Set the x offset of the button location.")
				.translation("backslot.configgui.xoffset")
				.defineInRange("xOffset", 154, 0, 159);
		
		yOffset = builder
				.comment("Set the y offset of the button location.")
				.translation("backslot.configgui.xoffset")
				.defineInRange("yOffset", 5, 0, 149);

		builder.pop();
		builder.comment("Render Settings").push("render");
		
		enableRendering = builder
				.comment("Enables rendering for back slot on entities.",
						"The game has to be reloaded for the changes to take effect.")
				.translation("backslot.configgui.enablerendering")
				.define("enableRendering", true);
		
		builder.pop();
		builder.pop();
	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final ClientConfigHolder CLIENT;
	static {
		final Pair<ClientConfigHolder, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfigHolder::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}
}
