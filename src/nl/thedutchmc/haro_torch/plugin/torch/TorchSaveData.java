package nl.thedutchmc.haro_torch.plugin.torch;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.thedutchmc.haro_torch.plugin.HaroTorch;

public class TorchSaveData {

	private File file;
	private FileConfiguration torchData;
	private HaroTorch plugin;

	public TorchSaveData(HaroTorch plugin, File file) {
		this.plugin = plugin;
		this.file = file;

		createTorchData();
		load();
	}

	private void createTorchData() {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		torchData = new YamlConfiguration();

		try {
			torchData.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		HashSet<UUID> set = new HashSet<UUID>();
		for (Torchy torch : TorchHandler.getTorches()) {
			set.add(torch.getOwner());
		}
		for (UUID uuid : set) {
			List<Location> locations = TorchHandler.getOwnerLocations(uuid);
			System.out.println("Saving torch data for: " + Bukkit.getPlayer(uuid).getName() + " - TorchCount: " + locations.size());
			torchData.set("torches." + uuid.toString(), locations);
		}
		try {
			torchData.save(file = new File(plugin.getDataFolder(), "torches.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void load() {
		System.out.println("[HaroTorch] Reading torches.yml...");

		ConfigurationSection section = (ConfigurationSection) torchData.get("torches");
		if (section == null) {
			return;
		}
		Map<String, Object> players = section.getValues(true);
		for (String uuid : players.keySet()) {
			List<Location> locations = (List<Location>) players.get(uuid);
			for (Location location : locations) {
				Torchy torch = new Torchy(UUID.fromString(uuid), location);
				TorchHandler.add(torch);
			}
		}
		if (TorchHandler.getTorches().size() != 0) {
			Bukkit.getLogger().info("[HaroTorch] Reading torches.yml successful!");
		} else {
			Bukkit.getLogger().info("[HaroTorch] torches.yml doesn't contain any entries. Skipping it");
		}
	}

}
