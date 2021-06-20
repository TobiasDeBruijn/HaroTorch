package dev.array21.harotorch.events;

import org.bukkit.Location;

import dev.array21.harotorch.torch.TorchHandler;

public class Common {
	
	/**
	 * Check if a Location has any torches surrounding it
	 * @param loc
	 * @return Returns true if it does not, false if it does.
	 */
	public static boolean checkSurroundings(Location loc) {
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
			return false;
		}
		
		return true;
	}
}
