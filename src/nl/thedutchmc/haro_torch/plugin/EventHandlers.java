package nl.thedutchmc.haro_torch.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
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
	public void creatureSpawnEvent(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if(!event.getSpawnReason().equals(SpawnReason.SPAWNER)) {
			if(entity instanceof Monster || event.getEntityType().equals(EntityType.PHANTOM) || event.getEntityType().equals(EntityType.SLIME) && !event.getEntityType().equals(EntityType.WITHER)) {
				Location entityLoc = entity.getLocation();

				for(int i = 0; i <= HaroTorch.locs.size()-1; i++) {
            		if(HaroTorch.locs.size() != 0 && !(i > HaroTorch.locs.size())) {
            			Location loc = HaroTorch.locs.get(i);
						double torchLocX = loc.getX();
						double torchLocZ = loc.getZ();
						
						double mobLocX = entityLoc.getX();
						double mobLocZ = entityLoc.getZ();
						
						double distance = Math.sqrt(Math.pow(torchLocX - mobLocX, 2) + Math.pow(torchLocZ - mobLocZ, 2));
	
						if(distance <= 48) {
							event.setCancelled(true);
						}
            		}
				}
				/*
				for(Map.Entry<Location, UUID> entry : HaroTorch.locsWithOwner.entrySet()) {
					Location key = entry.getKey();
					
					double mobLocX = loc.getX();
					double mobLocZ = loc.getZ();
					
					double torchLocX = key.getX();
					double torchLocZ = key.getZ();
					
					double distance = Math.sqrt(Math.pow(torchLocX - mobLocX, 2) + Math.pow(torchLocZ - mobLocZ, 2));

					if(distance <= 48) {
						event.setCancelled(true);
					}
				}*/
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getBlock().getType() == Material.TORCH) {
			ItemStack block = event.getItemInHand();
			if(block.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Haro's Torch")) {
				Location loc = event.getBlock().getLocation();				
				HaroTorch.locsWithOwner.put(loc, event.getPlayer().getUniqueId());
				HaroTorch.locs.add(loc);
				
				event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch placed!");
				haroTorch.saveToConfig();
			}
			
			
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getBlock().getType() == Material.TORCH) {			
			Location locToRemove = event.getBlock().getLocation();
			boolean removedLoc = false;
			
			for(int i = 0; i <= HaroTorch.locs.size()-1; i++) {
        		if(HaroTorch.locs.size() >= i && HaroTorch.locs.size() != 0) {
	
					Location loc = HaroTorch.locs.get(i);
					
					if(locToRemove.equals(loc)) {
		    			final Recipes recipe = new Recipes(haroTorch);
						event.setDropItems(false);
						event.getPlayer().getInventory().addItem(recipe.getHaroTorch(1));
						event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch Removed!");
						removedLoc = true;
					}
					
					if(removedLoc) {
						HaroTorch.locsWithOwner.remove(locToRemove);
						HaroTorch.locs.remove(locToRemove);
						haroTorch.saveToConfig();
					}
        		}
			}
			
			/*
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
			}*/
		}
	}
}