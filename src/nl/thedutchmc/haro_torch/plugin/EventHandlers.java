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
				System.out.println("locs size: " + HaroTorch.locs.size());
				
				HaroTorch.locsWithOwner.put(loc, event.getPlayer().getUniqueId());
					
				new ParticleHandler(loc,haroTorch, plugin).spawnParticle();
				
				event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch placed! It may take a couple seconds for the particles to appear!");
				haroTorch.saveToConfig();
			}
			
			
		}
	}
	
	@SuppressWarnings("unused") //variables are there for a WIP item
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getBlock().getType() == Material.TORCH) {			
			for(Map.Entry<Location, UUID> entry : HaroTorch.locsWithOwner.entrySet()) {
				Location loc = event.getBlock().getLocation();
				double x = loc.getX();
				double y = loc.getY();
				double z = loc.getZ();
				String world = loc.getWorld().getName();

	    		//Get the key from the HashMap
	    		Location key = entry.getKey();
	    	    
	    		//get the x,y,z and w from locWithOwner. WO meaning WithOwner
	    		int xWO = (int) key.getX();
	    		int yWO = (int) key.getY();
	    		int zWO = (int) key.getZ();
	    		World wWO = key.getWorld();
	    	
	    		Location locFromList = new Location(wWO,xWO,yWO,zWO);
	    			    		
	    		if(locFromList.equals(loc)) {					
	    			final Recipes recipe = new Recipes(haroTorch);
					HaroTorch.locsWithOwner.remove(locFromList);
					//TODO remove particle
					event.setDropItems(false);
					event.getPlayer().getInventory().addItem(recipe.getHaroTorch(1));
					event.getPlayer().sendMessage(ChatColor.GOLD + "HaroTorch Removed! Particles are not removed yet automatically. They will be gone on the next restart!");
					HaroTorch.locsWithOwner.remove(loc);
					haroTorch.saveToConfig();
	    		}
			}
		}
	}
}