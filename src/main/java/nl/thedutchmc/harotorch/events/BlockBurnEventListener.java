package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

import nl.thedutchmc.harotorch.torch.TorchHandler;

public class BlockBurnEventListener implements Listener {

	@EventHandler
	public void onBlockBurnEvent(BlockBurnEvent event) {
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
			
			event.setCancelled(true);
		}
	}
}
