package nl.thedutchmc.haro_torch.plugin;

import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
		if(entity instanceof Monster || event.getEntityType().equals(EntityType.PHANTOM) || event.getEntityType().equals(EntityType.SLIME)) {
			Location loc = entity.getLocation();
			for(int i = 0; i < HaroTorch.locs.size(); i++) {
				double torchLocX = HaroTorch.locs.get(i).getX();
				double torchLocZ = HaroTorch.locs.get(i).getZ();
				
				double mobLocX = loc.getX();
				double mobLocZ = loc.getZ();
				
				double distance = Math.sqrt(Math.pow(torchLocX - mobLocX, 2) + Math.pow(torchLocZ - mobLocZ, 2));
				if(distance <= 48) {
					event.setCancelled(true);
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
				HaroTorch.locs.add(loc);
				
				HaroTorch.locsWithOwner.put(loc, event.getPlayer().getUniqueId());
				
				event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch placed!");
				haroTorch.saveToConfig();
			}
			
			
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getBlock().getType() == Material.TORCH) {			
			Location locToRemove = null;
			boolean removedLoc = false;
			for(Map.Entry<Location, UUID> entry : HaroTorch.locsWithOwner.entrySet()) {
				Location loc = event.getBlock().getLocation();

	    		//Get the key from the HashMap
	    		Location key = entry.getKey();
	    	    
	    		//get the x,y,z and w from locWithOwner.
	    		int x = (int) key.getX();
	    		int y = (int) key.getY();
	    		int z = (int) key.getZ();
	    		World w = key.getWorld();
	    	
	    		Location locFromList = new Location(w,x,y,z);
	    			    		
	    		if(locFromList.equals(loc)) {					
	    			final Recipes recipe = new Recipes(haroTorch);
					event.setDropItems(false);
					event.getPlayer().getInventory().addItem(recipe.getHaroTorch(1));
					event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch Removed!");
					haroTorch.saveToConfig();
					locToRemove = loc;
					removedLoc = true;
	    		}
			} if(removedLoc) {
				HaroTorch.locsWithOwner.remove(locToRemove);
			}
		}
	}
}