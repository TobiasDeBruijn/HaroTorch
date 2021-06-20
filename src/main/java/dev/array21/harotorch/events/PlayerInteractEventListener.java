package dev.array21.harotorch.events;

import org.bukkit.Location;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import dev.array21.harotorch.torch.TorchHandler;


public class PlayerInteractEventListener implements Listener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) {
			return;
		}
		
		if(event.getClickedBlock().getType().data == TrapDoor.class) {
			Location clickedBlockLoc = event.getClickedBlock().getLocation();
			Location possibleTorchLoc = new Location(clickedBlockLoc.getWorld(), clickedBlockLoc.getX(), clickedBlockLoc.getY() + 1d, clickedBlockLoc.getZ());
			
			if(TorchHandler.isTorch(possibleTorchLoc)) {
				event.setCancelled(true);
			}
		}
	}
}
