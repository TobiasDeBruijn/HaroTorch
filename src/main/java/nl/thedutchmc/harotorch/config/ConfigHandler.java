package nl.thedutchmc.harotorch.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;

import dev.array21.classvalidator.ClassValidator;
import dev.array21.classvalidator.Pair;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.annotations.Nullable;

public class ConfigHandler {

	private HaroTorch plugin;
	private ConfigManifest manifest = null;
	
	private final Yaml yaml = new Yaml();
	private final Gson gson = new Gson();
	
	public ConfigHandler(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Load and read the configuration file
	 */
	public void readConfig() {
		File configFile = new File(this.plugin.getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			this.plugin.saveResource("config.yml", false);
		}
		
		Object loadedYaml;
		try {
			loadedYaml = yaml.load(new FileInputStream(configFile));
		} catch(FileNotFoundException e) {
			//Unreachable
			return;
		}
		
		String json = gson.toJson(loadedYaml, LinkedHashMap.class);
		ConfigManifest manifest = gson.fromJson(json, ConfigManifest.class);
		
		Pair<Boolean, String> validation = ClassValidator.validateType(manifest);
		if(validation.getA() == null) {
			HaroTorch.logWarn("Failed to validate config file: " + validation.getB());
			Bukkit.getPluginManager().disablePlugin(this.plugin);
			return;
		}
		
		if(!validation.getA()) {
			HaroTorch.logWarn("Invalid config file: " + validation.getB());
			Bukkit.getPluginManager().disablePlugin(this.plugin);
			return;
		}
		
		this.manifest = manifest;
	}
	
	/**
	 * Update the statUuid key in the config file
	 * @param uuid The new statUuid to set.
	 */
	public void setStatUuid(String uuid) {
		this.manifest.statUuid = uuid;
		String yaml = this.yaml.dump(this.manifest);
		
		try {
			File configFile = new File(this.plugin.getDataFolder(), "config.yml");
			BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
			bw.write(yaml);
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			HaroTorch.logWarn("Failed to update statUuid in config file: " + e.getMessage());
		}
	}
	
	/**
	 * Get the configuration manifest
	 * @return Returns the loaded ConfigManifest, or null when {@link #readConfig()} hasn't been called yet.
	 */
	@Nullable
	public ConfigManifest getManifest() {
		return this.manifest;
	}
}
