package dev.array21.harotorch.events;

import org.bukkit.Location;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.array21.harotorch.HaroTorch;

import org.bukkit.event.Listener;

public class BlockPhysicsEventListener implements Listener {

	private HaroTorch plugin;
	
	public BlockPhysicsEventListener(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
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

		
		//We don't want gravity blocks to fall when they (could) have a torch attached/ontop
		if(event.getBlock().getType().hasGravity()) {
			if(!Common.checkSurroundings(event.getBlock().getLocation())) {
				event.setCancelled(true);

	            new BukkitRunnable() {
					@Override
					public void run() {
						System.out.println("Placing back block");
						Location l = event.getBlock().getLocation();
						l.getBlock().setType(event.getBlock().getType(), false);
					}
				}.runTaskLater(this.plugin, 2L);
			}
		}
	}
}
