package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import nl.thedutchmc.harotorch.torch.TorchHandler;

public class BlockPistonExtendEventListener implements Listener {
	
	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
		for(Block b : event.getBlocks()) {
			//The block being moved
			Location movedBlockLocation = b.getLocation();
		
			//The block above the block being moved. If this is a torch we don't want to move the block below that torch, as it'd break the torch.
			Location movedBlockLocationAbove = b.getLocation();
			movedBlockLocationAbove.setY(b.getLocation().getY() +1);
			
			if(TorchHandler.isTorch(movedBlockLocation)) {
				event.setCancelled(true);
			}
			
			if(TorchHandler.isTorch(movedBlockLocationAbove)) {
				event.setCancelled(true);
			}
		}
	}
	
}
