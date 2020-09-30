package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import nl.thedutchmc.harotorch.torch.TorchHandler;

public class BlockExplodeEventListener implements Listener {

	@EventHandler
	public void onBlockExplodeEvent(BlockExplodeEvent event) {
		
		for(Block b : event.blockList()) {
			Location loc = b.getLocation();
			
			//Check if the block being exploded is a Torch
			if(TorchHandler.isTorch(loc)) {
				event.setCancelled(true);
				continue;
			}
			
			
			//Check if the block above the block being exploded is a torch
			Location loc_y_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
			if(TorchHandler.isTorch(loc_y_plus_1)) {
				event.setCancelled(true);
			}
		}
	}
}
