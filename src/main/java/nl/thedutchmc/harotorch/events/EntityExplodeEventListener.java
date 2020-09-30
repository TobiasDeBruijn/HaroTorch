package nl.thedutchmc.harotorch.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import nl.thedutchmc.harotorch.torch.TorchHandler;

public class EntityExplodeEventListener implements Listener {

	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		
		List<Block> blocksDontRemove = new ArrayList<>();
		
		for(Block b : event.blockList()) {
			Location loc = b.getLocation();
			
			//Check if the block being exploded is a Torch
			if(TorchHandler.isTorch(loc)) {
				blocksDontRemove.add(b);
				continue;
			}
			
			//Check if the block above the block being exploded is a torch
			Location loc_y_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
			if(TorchHandler.isTorch(loc_y_plus_1)) {
				blocksDontRemove.add(b);
			}
		}
		
		for(Block b : blocksDontRemove) {
			event.blockList().remove(b);
		}
	}
	
}
