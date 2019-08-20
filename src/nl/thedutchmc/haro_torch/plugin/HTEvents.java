package nl.thedutchmc.haro_torch.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;

import nl.thedutchmc.haro_torch.plugin.torch.TorchHandler;
import nl.thedutchmc.haro_torch.plugin.torch.Torchy;

public class HTEvents implements Listener {

	private HaroTorch plugin;

	public HTEvents(HaroTorch plugin) {
		this.plugin = plugin;
	}

	// handles the spawning of mobs and whether they should be prevented
	@EventHandler
	public void creatureSpawnEvent(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if (!event.getSpawnReason().equals(SpawnReason.SPAWNER)) {
			if (entity instanceof Monster || event.getEntityType() == EntityType.PHANTOM || event.getEntityType() == EntityType.SLIME && !event.getEntityType().equals(EntityType.WITHER)) {
				Location entityLoc = entity.getLocation();
				for (Torchy torch : TorchHandler.getTorches()) {
					if (TorchHandler.getDistanceCylindrical(torch.getLocation(), entityLoc) < plugin.mobBlockRadiusSq) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlock().getType().equals(Material.GLASS) || event.getBlock().getType().equals(Material.WHITE_STAINED_GLASS)) {
			Player player = event.getPlayer();
			ItemStack block = event.getItemInHand();
			if (block.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Haro's Torch")) {
				Location loc = event.getBlock().getLocation();
				TorchHandler.add(new Torchy(player.getUniqueId(), loc));
				player.sendMessage(ChatColor.GOLD + "HaroTorch placed!");
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Location location = event.getBlock().getLocation();
		if (TorchHandler.isHaroTorch(event.getBlock())) {
			UUID owner = TorchHandler.getTorchOwner(location);
			if (event.getPlayer().getUniqueId().equals(owner)) {
				Torchy torch = TorchHandler.getHaroTorch(location);
				TorchHandler.getTorches().remove(torch);
				event.setDropItems(false);
				ItemStack stack = TorchHandler.getNewTorch(1);
				event.getBlock().getWorld().dropItemNaturally(location, stack);
				event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch Removed!");
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You may not remove " + Bukkit.getPlayer(owner).getName() + "'s HaroTorch!");
			}
		}
	}

	@EventHandler
	public void onSave(WorldSaveEvent event) {
		plugin.torchSaveData.save();
	}
}
