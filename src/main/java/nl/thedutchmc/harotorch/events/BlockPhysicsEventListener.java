package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;

import nl.thedutchmc.harotorch.torch.TorchHandler;

import org.bukkit.event.Listener;

public class BlockPhysicsEventListener implements Listener {

	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		if(event.getBlock().getType().data != TrapDoor.class) {
			return;
		}
		
		Openable blockState = (Openable) event.getBlock().getBlockData();
		if(blockState.isOpen()) {
			Location loc = event.getBlock().getLocation();
			
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
				
				blockState.setOpen(false);
				event.getBlock().setBlockData(blockState);
			}
		}
	}
}
