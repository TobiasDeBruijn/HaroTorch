package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

import nl.thedutchmc.harotorch.torch.TorchHandler;

public class BlockPistonRetractEventListener implements Listener {
	
	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
		for(Block b : event.getBlocks()) {
			//The block being moved
			Location loc = b.getLocation();
		
			//The block above the block being moved. If this is a torch we don't want to move the block below that torch, as it'd break the torch.
			Location loc_y_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
			Location loc_x_plus_1 = new Location(loc.getWorld(), loc.getX() +1, loc.getY(), loc.getZ());
			Location loc_x_minus_1 = new Location(loc.getWorld(), loc.getX() -1, loc.getY(), loc.getZ());
			Location loc_z_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() +1);
			Location loc_z_minus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() -1);
			
			if(TorchHandler.isTorch(loc)) {
				event.setCancelled(true);
			}
			
			if(TorchHandler.isTorch(loc_y_plus_1)
					|| TorchHandler.isTorch(loc_x_plus_1)
					|| TorchHandler.isTorch(loc_x_minus_1)
					|| TorchHandler.isTorch(loc_z_plus_1)
					|| TorchHandler.isTorch(loc_z_minus_1)) {
				event.setCancelled(true);
			}
		}
	}
}
