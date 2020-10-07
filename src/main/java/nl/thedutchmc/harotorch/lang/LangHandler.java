package nl.thedutchmc.harotorch.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import nl.thedutchmc.harotorch.HaroTorch;

public class LangHandler {

	public static Language activeLang;
	
	private String basePath;
	private HaroTorch plugin;
	
	public LangHandler(HaroTorch plugin) {
		this.plugin = plugin;
		
		basePath = plugin.getDataFolder() + File.separator + "Lang";
	}
	
	private File configFile;
	private Properties props;
	
	public void load() {
		
		String activeLangPath = "";
		for(String langFile : discover()) {
			if(langFile.endsWith(HaroTorch.getConfigHandler().activeLang + ".properties")) activeLangPath = langFile;
		}
		
		if(activeLangPath.equals("")) {
			plugin.logWarn("Active language file " + HaroTorch.getConfigHandler().activeLang + ".properties not found! Please check your Lang directory and your config file. Defaulting to English");
			configFile = new File(basePath, "en.properties");
			HaroTorch.getConfigHandler().activeLang = "en";
		} else {
			configFile = new File(activeLangPath);
		}
		
		if(!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource("en.properties", false);
			
			//FileUtil.copy(plugin.getResource("en.properties"), configFile);
			try {
				FileUtils.copyToFile(plugin.getResource("en.properties"), configFile);
			} catch (IOException e) {
				plugin.logWarn("Something went wrong whilst trying to create " + configFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
		
		try {
			props = new Properties();
			props.load(new FileInputStream(configFile));
			
			readLang(HaroTorch.getConfigHandler().activeLang);
		} catch(FileNotFoundException e) {
			plugin.logWarn("Language file not found!");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readLang(String lang) {
		
		HashMap<String, String> langMap = new HashMap<>();
		
		for(String key : props.stringPropertyNames()) {
			String value = props.getProperty(key).toString();
			
			langMap.put(key, value);
		}
		
		activeLang= new Language(lang, langMap);
	}
	
	private List<String> discover() {
		
		File storageFolder = new File(basePath);
		
		if(!storageFolder.exists()) {
			try {
				Files.createDirectories(Paths.get(storageFolder.getAbsolutePath()));
			} catch (IOException | SecurityException e) {
				plugin.logWarn("Failed to create Lang storage directory! Please check your file permissions!");
			
				return null;
			}
		}
		
		try {
			Stream<Path> walk = Files.walk(Paths.get(storageFolder.getAbsolutePath()));
			
			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".properties")).collect(Collectors.toList());
			
			walk.close();
			
			return result;
		} catch(IOException e) {
			plugin.logWarn("A IOException was thrown whilst discovering Lang files!");
		
			e.printStackTrace();
			
			return null;
		}
	}
}
