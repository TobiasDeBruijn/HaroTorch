package dev.array21.harotorch.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;

import org.bukkit.event.Listener;

public class BlockPhysicsEventListener implements Listener {

	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		
		// We dont allow opening a trapdoor when a torch is ontop of it
		if(event.getBlock().getType().data == TrapDoor.class) {
			Openable blockState = (Openable) event.getBlock().getBlockData();
			if(blockState.isOpen()) {
				if(!Common.checkSurroundings(event.getBlock().getLocation())) {
					blockState.setOpen(false);
					event.getBlock().setBlockData(blockState);
				}
			}
		}

		//We wont break a scaffolding block when a torch is ontop of it
		if(event.getBlock().getType() == Material.SCAFFOLDING) {
			Location l = event.getBlock().getLocation();
			if(!Common.checkSurroundings(l)) {
				Block b = event.getBlock();
			}
		}
		
		//We don't want gravity blocks to fall when they (could) have a torch attached/ontop
		if(event.getBlock().getType().hasGravity()) {
			if(!Common.checkSurroundings(event.getBlock().getLocation())) {
				event.setCancelled(true);
			}
		}
	}
}
