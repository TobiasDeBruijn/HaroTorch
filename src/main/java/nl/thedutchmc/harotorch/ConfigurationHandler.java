package nl.thedutchmc.harotorch;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationHandler {

	private HaroTorch plugin;

	public String torchBlock, messageTorchBrokenOwnerMismatch, messageTorchBrokenSuccess, messageTorchPlacedSucccess;
	public boolean enableTorchParticles;
	public int torchRange, torchHighlightRange, torchHighlightTime;
	
	public List<String> recipeShape;
	public HashMap<Character, Material> recipeKeys = new HashMap<>();
	
	public ConfigurationHandler(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	private File file;
	private FileConfiguration config;
	
	private FileConfiguration getConfig() {
		return config;
	}
	
	public void loadConfig() {
		file = new File(plugin.getDataFolder(), "config.yml");
		
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource("config.yml", false);
		}
		
		config = new YamlConfiguration();
		
		try {
			config.load(file);
			readConfig();
		} catch (InvalidConfigurationException e) {
			plugin.logWarn("Invalid config.yml!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readConfig() {
		torchBlock = this.getConfig().getString("torchBlock");
		messageTorchBrokenOwnerMismatch = this.getConfig().getString("messageTorchBrokenOwnerMismatch");
		messageTorchBrokenSuccess = this.getConfig().getString("messageTorchBrokenSuccess");
		messageTorchPlacedSucccess = this.getConfig().getString("messageTorchPlacedSucccess");
		
		enableTorchParticles = this.getConfig().getBoolean("enableTorchParticles");
		
		torchRange = this.getConfig().getInt("torchRange");
		torchHighlightRange = this.getConfig().getInt("torchHighlightRange");
		torchHighlightTime = this.getConfig().getInt("torchHighlightTime");
		
		//Recipe parsing
		recipeShape = this.getConfig().getStringList("recipeShape");
		
		List<String> recipeKeysList = this.getConfig().getStringList("recipeKeys");
		
		for(String str : recipeKeysList) {
			String[] parts = str.split("<-->");
			
			char key = parts[0].toCharArray()[0];
			Material m = Material.matchMaterial(parts[1]);
			
			if(m == null) {
				plugin.logWarn("Invalid material for Torch recipe: " + parts[1]);
				plugin.logWarn("Errors may occur from now on!");
			}
			
			recipeKeys.put(key, m);
		}
	}
}
