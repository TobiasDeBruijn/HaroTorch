package nl.thedutchmc.haro_torch.plugin;

import java.io.File;
import java.util.Collection;

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

		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				for (Torchy torch : TorchHandler.getTorches()) {
					World world = torch.getLocation().getWorld();
					if (torch.getLocation().getChunk().isLoaded()) {
						Location loc = torch.getLocation();
						Collection<Entity> entities = world.getNearbyEntities(torch.getLocation(), 48, 48, 48);
						for (Entity e : entities) {
							if (e instanceof Player) {
								double x = loc.getBlockX() + 0.5;
								double y = loc.getBlockY() + 0.5;
								double z = loc.getBlockZ() + 0.5;
								world.spawnParticle(Particle.DRAGON_BREATH, x, y, z, 1, 0D, 0D, 0D, 0.005);

							}
						}

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