package nl.thedutchmc.harotorch.commands.torchSubCmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class HighlightAreaOfEffectExecutor {

	private static HashMap<UUID, Long> lastCommandTimestamps = new HashMap<>();
	
	public static boolean aoe(CommandSender sender, HaroTorch plugin) {
		
		Integer commandCooldown = HaroTorch.getConfigHandler().commandCooldown;
		if(commandCooldown != null && commandCooldown > 0) {
			Long lastCommandUseTimestamp = lastCommandTimestamps.get(((Player) sender).getUniqueId());
			if(lastCommandUseTimestamp != null) {
				if(lastCommandUseTimestamp >= System.currentTimeMillis()) {
					sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("commandCooldown"));
					return true;
				}
			}
			
			lastCommandTimestamps.put(((Player) sender).getUniqueId(), System.currentTimeMillis() + (commandCooldown * 1000));
		}
		
		String msg = LangHandler.activeLang.getLangMessages().get("startingAoe").replaceAll("%SECONDS%", ChatColor.RED + String.valueOf(HaroTorch.getConfigHandler().torchHighlightTime) + ChatColor.GOLD);
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + msg);
		
		List<Location> nearbyTorches = TorchHandler.getTorchLocationsNearPlayer((Player) sender, HaroTorch.getConfigHandler().torchHighlightRange);
		final List<TorchParticleObject> torchParticles = new ArrayList<>();
		
		for(Location l : nearbyTorches) {
			
			final List<Location> blocksOnTorchRadius = new ArrayList<>();
			
			final int radius = HaroTorch.getConfigHandler().torchRange;
			final int cx = l.getBlockX();
			final int cz = l.getBlockZ();
			final World w = l.getWorld();
			
			for(int i = 0; i < 360; i++) {
				
				final double rad = i * ((2 * Math.PI)/360);
				
				final int x = (int) (cx + (radius * Math.cos(rad)));
				final int z = (int) (cz + (radius * Math.sin(rad)));
				
				int y;
				if(w.getEnvironment() == Environment.NETHER) {
					y = l.getBlockY();
				} else {
					y = w.getHighestBlockAt(x, z).getY() + 1;
				}
				
				blocksOnTorchRadius.add(new Location(w, x, y, z));
			}
			
			final int r = (int) (Math.random() * 256D);
			final int g = (int) (Math.random() * 256D);
			final int b = (int) (Math.random() * 256D);
			
			torchParticles.add(new TorchParticleObject(r, g, b, blocksOnTorchRadius, l));
		}
		
		final BukkitTask particle = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				for(TorchParticleObject torchParticle : torchParticles) {
				
					final Location torchLoc = torchParticle.getTorch();
					
					torchLoc.getWorld().spawnParticle(Particle.REDSTONE, torchLoc.getX() + 0.5D, torchLoc.getY() + 1.5D, torchLoc.getZ() + 0.5D, 25, 0D, 0D, 0D, 0.005, new Particle.DustOptions(torchParticle.getTorchParticleColor(), 1));
					
					for(Location l : torchParticle.getCircleLocations()) {	
						
						for(int i = 0; i < HaroTorch.getConfigHandler().torchAoeParticleHeight; i++) {
							torchLoc.getWorld().spawnParticle(Particle.REDSTONE, l.getX() + 0.5D, l.getY() + 0.5D + i, l.getZ() + 0.5D, 5, 0D, 0D, 0D, 0.005, new Particle.DustOptions(torchParticle.getTorchParticleColor(), 1));
						}
						
						if(torchLoc.getWorld().getEnvironment() == Environment.NETHER) {
							for(int i = 1; i < HaroTorch.getConfigHandler().torchAoeParticleHeight -1; i++) {
								torchLoc.getWorld().spawnParticle(Particle.REDSTONE, l.getX() + 0.5D, l.getY() + 0.5D - i, l.getZ() + 0.5D, 5, 0D, 0D, 0D, 0.005, new Particle.DustOptions(torchParticle.getTorchParticleColor(), 1));
							}
						}
					}
				}	
			}
			
		}.runTaskTimer(plugin, 60L, 10L);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				particle.cancel();
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("endingAoe"));
			}
		}.runTaskLater(plugin, 30L * 20L);
		
		return true;
	}
	
	private static class TorchParticleObject {
		
		private Color particleColor;
		private List<Location> circleLocations;
		private Location torch;
		
		public TorchParticleObject(int r, int g, int b, List<Location> circleLocations, Location torch) {
			this.particleColor = Color.fromRGB(r, g, b);
			this.circleLocations = circleLocations;
			this.torch = torch;
		}
		
		public Color getTorchParticleColor() {
			return this.particleColor;
		}
		
		public List<Location> getCircleLocations() {
			return this.circleLocations;
		}
		
		public Location getTorch() {
			return this.torch;
		}
	}
	
}
