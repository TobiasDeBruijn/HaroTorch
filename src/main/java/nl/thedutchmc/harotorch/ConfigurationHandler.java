package nl.thedutchmc.harotorch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import nl.thedutchmc.harotorch.annotations.Nullable;

public class ConfigurationHandler {

	private HaroTorch plugin;

	public String torchBlock, activeLang;
	public Boolean enableTorchParticles, allowRemoveNotOwnedTorch, onlyBlockHostileMobs;
	public Integer torchRange, torchHighlightRange, torchHighlightTime, torchAoeParticleHeight, commandCooldown;
	
	/**
	 * Indicates how many torches a player may place. If null or -1, it should be disabled.
	 * @since 2.2.2
	 */
	@Nullable
	public Integer torchPlaceLimit;
	
	public List<String> recipeShape;
	public List<EntityType> mobExclusionList = new ArrayList<>();
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
		activeLang = this.getConfig().getString("activeLang");
		
		enableTorchParticles = this.getConfig().getBoolean("enableTorchParticles");
		allowRemoveNotOwnedTorch = this.getConfig().getBoolean("allowRemoveNotOwnedTorch");
		onlyBlockHostileMobs = this.getConfig().getBoolean("onlyBlockHostileMobs");
		
		torchRange = this.getConfig().getInt("torchRange");
		torchHighlightRange = this.getConfig().getInt("torchHighlightRange");
		torchHighlightTime = this.getConfig().getInt("torchHighlightTime");
		torchAoeParticleHeight = this.getConfig().getInt("torchAoeParticleHeight");
		torchPlaceLimit = this.getConfig().getInt("torchPlaceLimit");
		commandCooldown = this.getConfig().getInt("commandCooldown");
		
		//Mob exclusion list parsing
		List<String> mobExclusionList = this.getConfig().getStringList("mobsExcludeFromBlockList");
		for(String mobStr : mobExclusionList ) {
			try {
				EntityType et = EntityType.valueOf(mobStr.toUpperCase());
				this.mobExclusionList.add(et);
			} catch(IllegalArgumentException e) {
				plugin.logWarn("Provided mob type " + mobStr + " is not valid. Please check your configuration file!");
			}
		}
		
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
