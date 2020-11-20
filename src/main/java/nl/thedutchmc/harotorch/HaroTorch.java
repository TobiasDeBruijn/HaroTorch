package nl.thedutchmc.harotorch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.commands.TorchCommandExecutor;
import nl.thedutchmc.harotorch.commands.TorchCommandTabCompleter;
import nl.thedutchmc.harotorch.events.BlockBreakEventListener;
import nl.thedutchmc.harotorch.events.BlockExplodeEventListener;
import nl.thedutchmc.harotorch.events.BlockFromToEventListener;
import nl.thedutchmc.harotorch.events.BlockPlaceEventListener;
import nl.thedutchmc.harotorch.events.CreatureSpawnEventListener;
import nl.thedutchmc.harotorch.events.EntityExplodeEventListener;
import nl.thedutchmc.harotorch.lang.LangHandler;
import nl.thedutchmc.harotorch.torch.Recipe;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class HaroTorch extends JavaPlugin {
	
	private static ConfigurationHandler CONFIG;
	
	public static double RANGE;
	public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
	public static List<UUID> activeAdmins = new ArrayList<>();
	
	@Override
	public void onEnable() {
		logInfo("You are using NMS version " + NMS_VERSION);
		
		CONFIG = new ConfigurationHandler(this);
		CONFIG.loadConfig();
		
		LangHandler langHandler = new LangHandler(this);
		langHandler.load();
		
		logInfo(LangHandler.activeLang.getLangMessages().get("welcome"));
		
		RANGE = Math.pow(CONFIG.torchRange, 2);
		
		//TorchHandler
		TorchHandler torchHandler = new TorchHandler(this);
		torchHandler.setup();
		
		//Torch Recipe
		Recipe recipe = new Recipe(this);
		this.getServer().addRecipe(recipe.getTorchRecipe());
		
		//Minecraft events
		Bukkit.getPluginManager().registerEvents(new BlockBreakEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlaceEventListener(this), this);
		Bukkit.getPluginManager().registerEvents(new CreatureSpawnEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockFromToEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockExplodeEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntityExplodeEventListener(), this);
		
		//Commands
		this.getCommand("torch").setExecutor(new TorchCommandExecutor(this));
		this.getCommand("torch").setTabCompleter(new TorchCommandTabCompleter());
		
		//Scheduler for particles
		if(CONFIG.enableTorchParticles) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					for(Player p : Bukkit.getOnlinePlayers()) {		
						for(Location l : TorchHandler.getTorchLocationsNearPlayer(p, 32)) {
														
							final World w = l.getWorld();
							final double x = l.getBlockX() + 0.5D;
							final double y = l.getBlockY() + 0.5D;
							final double z = l.getBlockZ() + 0.5D;
							
							w.spawnParticle(Particle.DRAGON_BREATH, x, y, z, 2, 0D, 0D, 0D, 0.005);
						}
					}
				}
			}.runTaskTimer(this, 60L, 20L);
		}
	}
	
	@Override
	public void onDisable() {
		logInfo(LangHandler.activeLang.getLangMessages().get("goodbye"));
	}
	
	public void logInfo(String log) {
		this.getLogger().info(log);
	}
	
	public void logWarn(String log) {
		this.getLogger().warning(log);
	}
	
	public static  ConfigurationHandler getConfigHandler() {
		return HaroTorch.CONFIG;
	}
	
	public static String getMessagePrefix() {
		return ChatColor.GRAY + "[" + ChatColor.AQUA + "HT" + ChatColor.GRAY + "] " + ChatColor.RESET;
	}
	
}
