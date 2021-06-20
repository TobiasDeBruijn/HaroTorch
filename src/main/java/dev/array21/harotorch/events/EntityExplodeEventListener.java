package dev.array21.harotorch.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import dev.array21.harotorch.torch.TorchHandler;

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
			Location loc_x_plus_1 = new Location(loc.getWorld(), loc.getX() +1, loc.getY(), loc.getZ());
			Location loc_x_minus_1 = new Location(loc.getWorld(), loc.getX() -1, loc.getY(), loc.getZ());
			Location loc_z_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() +1);
			Location loc_z_minus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() -1);			
			
			if(TorchHandler.isTorch(loc_y_plus_1)
					|| TorchHandler.isTorch(loc_x_plus_1)
					|| TorchHandler.isTorch(loc_x_minus_1)
					|| TorchHandler.isTorch(loc_z_plus_1)
					|| TorchHandler.isTorch(loc_z_minus_1)) {
				blocksDontRemove.add(b);
			}
		}
		
		for(Block b : blocksDontRemove) {
			event.blockList().remove(b);
		}
	}
	
}
