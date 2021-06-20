package dev.array21.harotorch.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockFadeEventListener implements Listener {
	
	@EventHandler
	public void onBlockFadeEvent(BlockFadeEvent event) {
		//We wont break a scaffolding block when a torch is ontop of it
		if(event.getBlock().getType() == Material.SCAFFOLDING) {
			Location l = event.getBlock().getLocation();
			if(!Common.checkSurroundings(l)) {
				event.setCancelled(true);
			}
		}
	}
}
