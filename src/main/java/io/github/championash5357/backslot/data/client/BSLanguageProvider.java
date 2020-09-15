package io.github.championash5357.backslot.data.client;

import io.github.championash5357.backslot.common.BackSlotMain;
import io.github.championash5357.backslot.common.util.TranslationStrings;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class BSLanguageProvider extends LanguageProvider {

	public BSLanguageProvider(DataGenerator gen, String locale) {
		super(gen, BackSlotMain.ID, locale);
	}

	@Override
	protected void addTranslations() {
		String locale = this.getName().replace("Languages: ", "");
		switch(locale) {
		case "en_us":
			add(TranslationStrings.BACK_SLOT_CATEGORY, "Back Slot");
			add(TranslationStrings.SWAP_BACK_SLOT_KEY, "Swap Back Slot");
			add(TranslationStrings.TO_BACK_BUTTON, "To Back");
			add(TranslationStrings.TO_FRONT_BUTTON, "To Front");
			add(TranslationStrings.NOTIFICATION_OUTDATED, "%s is currently outdated.");
			add(TranslationStrings.NOTIFICATION_CURRENT, "The current version is %s.");
			add(TranslationStrings.NOTIFICATION_CHANGELOG, "Changelog: ");
			break;
		default:
			break;
		}
	}
}
