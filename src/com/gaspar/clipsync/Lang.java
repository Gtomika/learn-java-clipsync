package com.gaspar.clipsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles languages supported by the app.
 * @author Gáspár Tamás
 */
public class Lang {
	
	//hungarian
	private static final String HU = "hu";
	
	//english
	private static final String EN = "en";
	
	/**
	 * This contant determines the app language.
	 */
	private static final String SELECTED_LANG = HU;
	
	/**
	 * All string used by the app.
	 */
	private static final List<LocalizedString> localizedStrings = new ArrayList<>();
	
	static {
		localizedStrings.add(buildLocalisedStringEnHu("learn_java_ready", "Learn java ClipSync server ready!", "A ClipSync server készen áll"));
		localizedStrings.add(buildLocalisedStringEnHu("mode_bluetooth", "Bluetooth mode", "Bluetooth mód"));
		localizedStrings.add(buildLocalisedStringEnHu("mode_network", "Network mode", "Hálózati mód"));
		localizedStrings.add(buildLocalisedStringEnHu("mode_select", "Select a ClipSync mode!", "Válassz ClipSync módot!"));
		localizedStrings.add(buildLocalisedStringEnHu("log_show_selector", "Showing selector screen...", "Mód választó mutatása..."));
		localizedStrings.add(buildLocalisedStringEnHu("bluetooth_fail_disc", "Could not be made discoverable!", "Nem lehetett láthatóvá tenni!"));
		localizedStrings.add(buildLocalisedStringEnHu("server_running", "Server already running", "A szerver már fut"));
		localizedStrings.add(buildLocalisedStringEnHu("bluetooth_starting", "Starting bluetooth server...", "Bluetooth szerver indítása..."));
		localizedStrings.add(buildLocalisedStringEnHu("bluetooth_stopping", "Stopping bluetooth server...", "Bluetooth szerver leállítása..."));
		localizedStrings.add(buildLocalisedStringEnHu("bluetooth_active", "Bluetooth ClipSync active", "Bluetooth ClipSync aktív"));
		localizedStrings.add(buildLocalisedStringEnHu("bluetooth_fail", "Failed to activate bluetooth on your computer! Make sure you have bluetooth. If not, you can try network mode.", "Nem sikerült bekapcsolni a bluetooth-t a gépeden. Biztos, hogy van blueooth ezen a gépen? Ha nincs, megpróbálhatod a hálózati módot."));
		localizedStrings.add(buildLocalisedStringEnHu("back_confirm", "Are you sure you want to go back to mode selection?", "Biztos vissza akarsz menni a mód választóhoz?"));
		localizedStrings.add(buildLocalisedStringEnHu("bluetooth_accepting", "Accepting bluetooth connection from the app...", "Kapcsolat létesítése az alkalmazással..."));
		localizedStrings.add(buildLocalisedStringEnHu("bluetooth_accepted", "Accepted bluetooth connection from the app!", "Bluetooth kapcsolat létrejött!"));
		localizedStrings.add(buildLocalisedStringEnHu("data_received", "Received data from the app!", "Adat érkezett az alkalmazástól"));
		localizedStrings.add(buildLocalisedStringEnHu("data_copied", "Copied data to clipboard!", "Adat a vágólapra másolva!"));
		localizedStrings.add(buildLocalisedStringEnHu("network_starting", "Starting local network server...", "Helyi hálózati szerver indítása..."));
		localizedStrings.add(buildLocalisedStringEnHu("network_stopping", "Stopping local network server...", "Helyi hálózati szerver leállítása..."));
		localizedStrings.add(buildLocalisedStringEnHu("network_active", "Network ClipSync active", "Hálózati ClipSync aktív"));
		localizedStrings.add(buildLocalisedStringEnHu("network_fail", "Failed to start server! Make sure you have connection.\n You can try Bluetooth mode as an alternative.", "A hálózati szerver nem tudott elindulni! Van hálózai kapcsolatod?\n Megpróbálhatod a bluetooth szervert is."));
		localizedStrings.add(buildLocalisedStringEnHu("network_accepting", "Accepting broadcasts over local network...", "Várakozás csomagokra az alkalmazástól..."));
		localizedStrings.add(buildLocalisedStringEnHu("network_accepted", "Received something from ", "Csomag érkezett tõle: "));
		localizedStrings.add(buildLocalisedStringEnHu("network_response", "Sent response message to the app!", "Válasz elküdve az alkalmazásnak!"));
		localizedStrings.add(buildLocalisedStringEnHu("network_unknown", "This message is not from the app.", "Ez a csomag nem az alkalmazástól jött."));
		localizedStrings.add(buildLocalisedStringEnHu("back", "Back", "Vissza"));
	}
	
	/**
	 * Create a {@link LocalizedString} from english and hungarian translation.
	 */
	private static LocalizedString buildLocalisedStringEnHu(String id, String enTranslation, String huTranslation) {
		Map<String, String> translations = new HashMap<>();
		translations.put(EN, enTranslation);
		translations.put(HU, huTranslation);
		return new LocalizedString(id, translations);
	}
	
	/**
	 * Finds the translation for the string with id, in the currently selected language.
	 * @param id Id of the string.
	 * @return The translated string.
	 */
	public static String getTranslation(String id) {
		for(LocalizedString localizedString: localizedStrings) {
			if(localizedString.getId().equals(id)) {
				return localizedString.getTranlation(SELECTED_LANG);
			}
		}
		return null;
	}
	
	/**
	 * A string resource in multiple languages.
	 * @author Gáspár Tamás
	 */
	static class LocalizedString {
		
		/**
		 * Identifies such as "string_hello".
		 */
		private final String id;
		
		/**
		 * Translated value of the string in all supported languages.
		 */
		private Map<String, String> translations;
		
		public LocalizedString(String id, Map<String, String> translations) {
			this.id = id;
			this.translations = translations;
		}

		/**
		 * Find a translation for this string.
		 * @param lang The language.
		 * @return The translation.
		 */
		public String getTranlation(String lang) {
			return translations.get(lang);
		}

		public String getId() {
			return id;
		}
	}

}
