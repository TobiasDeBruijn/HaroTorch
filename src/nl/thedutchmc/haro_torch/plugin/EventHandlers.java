package nl.thedutchmc.haro_torch.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EventHandlers implements Listener {
	
	Plugin plugin;
	HaroTorch haroTorch;

	public EventHandlers(HaroTorch plugin, HaroTorch haroTorch) {
		this.plugin = plugin;
		this.haroTorch = haroTorch;
	}
	
	@EventHandler
	public void onEntitySpawnEvent(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Monster) {
			Location loc = entity.getLocation();
			for(int i = 0; i < haroTorch.locs.size(); i++) {
				double torchLocX = haroTorch.locs.get(i).getX();
				double torchLocZ = haroTorch.locs.get(i).getZ();
				
				double mobLocX = loc.getX();
				double mobLocZ = loc.getZ();
				
				double distance = Math.sqrt(Math.pow(torchLocX - mobLocX, 2) + Math.pow(torchLocZ - mobLocZ, 2));
				if(distance <= 48) {
					//TODO 
					entity.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getBlock().getType() == Material.TORCH) {
			ItemStack block = event.getItemInHand();
			if(block.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Haro's Torch")) {
				Location loc = event.getBlock().getLocation();
				haroTorch.locs.add(loc);
				haroTorch.locsByOwner.put(event.getPlayer().getUniqueId(), loc);
				event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch placed!");
				haroTorch.saveToConfig();
			}
			
			
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getBlock().getType() == Material.TORCH) {
			for(int i = 0; i == haroTorch.locs.size(); i++) {
				Location loc = event.getBlock().getLocation();
				if(haroTorch.locs.get(i) == loc) {
					final Recipes recipe = new Recipes(haroTorch);
					haroTorch.locs.remove(i);
					event.getPlayer().getInventory().removeItem(new ItemStack(Material.TORCH, 1));
					event.getPlayer().getInventory().addItem(recipe.getHaroTorch(1));
					event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch Removed!");
					haroTorch.locsByOwner.remove(event.getPlayer().getUniqueId(),loc);
					haroTorch.saveToConfig();
				}
			}
		}
	}
}
