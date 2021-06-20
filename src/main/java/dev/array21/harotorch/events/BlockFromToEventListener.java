package dev.array21.harotorch.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import dev.array21.harotorch.torch.TorchHandler;

public class BlockFromToEventListener implements Listener {

	@EventHandler
	public void onBlockFromToEvent(BlockFromToEvent event) {
		
		if(TorchHandler.isTorch(event.getToBlock().getLocation())) {
			event.setCancelled(true);
		}	
	}
}
