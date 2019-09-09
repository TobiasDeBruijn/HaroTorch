package nl.thedutchmc.haro_torch.plugin;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import nl.thedutchmc.haro_torch.plugin.torch.TorchHandler;
import nl.thedutchmc.haro_torch.plugin.torch.TorchSaveData;
import nl.thedutchmc.haro_torch.plugin.torch.Torchy;

public class HaroTorch extends JavaPlugin implements Listener {

	public Recipes recipes = new Recipes(this);

	public CommandHandler commandHandler = new CommandHandler(this);

	public int mobBlockRadiusSq = 48 * 48;
	public int playerCommandCheckRadiusSq = 64 * 64;

	public TorchSaveData torchSaveData;

	@Override
	public void onEnable() {
		torchSaveData = new TorchSaveData(this, new File(getDataFolder(), "torches.yml"));

		getServer().addRecipe(recipes.getHaroTorchRecipe());

		getCommand("ht").setExecutor(commandHandler);
		getCommand("stc").setExecutor(commandHandler);

		getServer().getPluginManager().registerEvents(new HTEvents(this), this);

		// Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
		// @Override
		// public void run() {
		// for (Torchy torch : TorchHandler.getTorches()) {
		// World world = torch.getLocation().getWorld();
		// Location location = torch.getLocation();
		// if (world.isChunkLoaded(location.getBlockX() / 16, location.getBlockZ() /
		// 16)) {
		// Collection<Entity> entities = world.getNearbyEntities(location, 48, 48, 48);
		// for (Entity e : entities) {
		// if (e instanceof Player) {
		// double x = location.getBlockX() + 0.5;
		// double y = location.getBlockY() + 0.5;
		// double z = location.getBlockZ() + 0.5;
		// world.spawnParticle(Particle.DRAGON_BREATH, x, y, z, 1, 0D, 0D, 0D, 0.005);
		//
		// }
		// }
		// }
		// }
		// }
		// }, 60L, 20L);

		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				Collection<? extends Player> players = Bukkit.getOnlinePlayers();
				for (Player p : players) {
					List<Torchy> nearbyTorches = TorchHandler.getTorchesNearPlayer(p);
					for (Torchy torch : nearbyTorches) {
						Location location = torch.getLocation();
						World world = p.getWorld();
						double x = location.getBlockX() + 0.5;
						double y = location.getBlockY() + 0.5;
						double z = location.getBlockZ() + 0.5;
						world.spawnParticle(Particle.DRAGON_BREATH, x, y, z, 1, 0D, 0D, 0D, 0.005);
					}
				}
			}
		}, 60L, 20L);
	}

	@Override
	public void onDisable() {
		torchSaveData.save();
	}

}