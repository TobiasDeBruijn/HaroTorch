package dev.array21.harotorch.commands.torchSubCmds;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import dev.array21.bukkitreflectionlib.abstractions.entity.Entity;
import dev.array21.bukkitreflectionlib.abstractions.entity.monster.EntityMagmaCube;
import dev.array21.bukkitreflectionlib.abstractions.entity.player.CraftPlayer;
import dev.array21.bukkitreflectionlib.abstractions.entity.player.PlayerConnection;
import dev.array21.bukkitreflectionlib.abstractions.packet.EntityDestroyPacket;
import dev.array21.bukkitreflectionlib.abstractions.packet.EntityMetadataPacket;
import dev.array21.bukkitreflectionlib.abstractions.packet.EntitySpawnPacket;
import dev.array21.bukkitreflectionlib.abstractions.world.CraftWorld;
import dev.array21.bukkitreflectionlib.exceptions.ReflectException;
import dev.array21.harotorch.commands.CommandCooldown;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import dev.array21.harotorch.HaroTorch;
import dev.array21.harotorch.commands.SubCommand;
import dev.array21.harotorch.lang.LangHandler;
import dev.array21.harotorch.torch.TorchHandler;

public class HighlightExecutor implements SubCommand {
	
	/**
	 * HashMap to keep track of command usage as to avoid spam
	 * K = The UUID of the player
	 * V = Miliseconds since Jan 1 1970 after which the Player may execute a command again
	 */
	private static final HashMap<UUID, Long> lastCommandTimestamps = new HashMap<>();

	public boolean run(HaroTorch plugin, CommandSender sender, String[] args) {

		if (CommandCooldown.checkCommandCooldown(plugin, sender, lastCommandTimestamps)) return true;

		List<Location> nearbyTorches = TorchHandler.getTorchLocationsNearPlayer((Player) sender, plugin.getConfigManifest().torchHighlightRange);
		Player p = (Player) sender;

		Entity[] spawnedEntities = spawnHighlight(p, nearbyTorches.toArray(new Location[0]));
		
		String msg = LangHandler.activeLang.getLangMessages().get("startingHiglight").replaceAll("%SECONDS%", ChatColor.RED + String.valueOf(plugin.getConfigManifest().torchHighlightTime) + ChatColor.GOLD);
		p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + msg);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				killHighlighted(spawnedEntities, p);
				p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("endingHighlight"));
			}
		}.runTaskLater(plugin, plugin.getConfigManifest().torchHighlightTime * 20);
		
		
		return true;
	}
	
	/**
	 * Spawn magma cubes for the provided Player at the given Locations
	 * @param player The Player
	 * @param locations The Locations
	 * @return Returns a List of Entity IDs
	 */
	public Entity[] spawnHighlight(Player player, Location[] locations) {
		Entity[] spawnedEntities = new Entity[locations.length];

		try {
			CraftPlayer craftPlayer = CraftPlayer.getInstance(player);
			PlayerConnection playerConnection = craftPlayer.getPlayerConnection();

			for (int i = 0; i < locations.length; i++) {
				Location location = locations[i];

				CraftWorld craftWorld = CraftWorld.getInstance(location.getWorld());

				EntityMagmaCube magmaCube = EntityMagmaCube.getInstance(craftWorld);
				magmaCube.setInvisible(true);
				magmaCube.setGlowing(true);
				magmaCube.setSize(2);
				magmaCube.setLocation(
						location.getBlockX() + 0.5d,
						location.getBlockY(),
						location.getBlockZ() + 0.5d,
						0f,
						0f
				);

				EntitySpawnPacket entitySpawnPacket = EntitySpawnPacket.getInstance(magmaCube);
				entitySpawnPacket.send(playerConnection);

				EntityMetadataPacket entityMetadataPacket = EntityMetadataPacket.getInstance(magmaCube);
				entityMetadataPacket.send(playerConnection);

				spawnedEntities[i] = magmaCube;
			}
		} catch (ReflectException e) {
			e.printStackTrace();
		}

		return spawnedEntities;
	}
	
	/**
	 * Kill the spawned Magma cubes to end highlighting
	 * @param entities The entities to destroy
	 * @param player The Player for which the Entities are showing
	 */
	public void killHighlighted(Entity[] entities, Player player) {
		try {
			CraftPlayer craftPlayer = CraftPlayer.getInstance(player);
			PlayerConnection playerConnection = craftPlayer.getPlayerConnection();

			for(Entity entity : entities) {
				EntityDestroyPacket destroyPacket = EntityDestroyPacket.getInstance(new Entity[] { entity });
				destroyPacket.send(playerConnection);
			}
		} catch (ReflectException e) {
			e.printStackTrace();
		}
	}
}
