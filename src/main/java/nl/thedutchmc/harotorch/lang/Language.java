package nl.thedutchmc.harotorch.lang;

import java.util.HashMap;

public class Language {

	private String lang;
	private HashMap<String, String> langMessages;
	
	public Language(String lang, HashMap<String, String> langMessages) {
		this.lang = lang;
		this.langMessages = langMessages;
	}
	
	public String getLang() {
		return this.lang;
	}
	
	public HashMap<String, String> getLangMessages() {
		return this.langMessages;
	}
}
