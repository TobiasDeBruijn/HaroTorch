package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.torch.Torch;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class CreatureSpawnEventListener implements Listener {

	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		
		Entity e = event.getEntity();
		
		//If the spawn reason is a spawner, or via infection (zombie villagers), return.
		if(event.getSpawnReason().equals(SpawnReason.SPAWNER) && !event.getSpawnReason().equals(SpawnReason.INFECTION)) return;
		
		//Check if the spawned Entity is a Monster, Phantom or Slime and not a Wither
		if (e instanceof Monster || event.getEntityType() == EntityType.PHANTOM || event.getEntityType() == EntityType.SLIME && !event.getEntityType().equals(EntityType.WITHER )) {
			
			Location mobLocation = event.getLocation();
			
			//Iterate over all Torches
			for(Torch t : TorchHandler.getTorches()) {
				
				//Check if the spawn occured in the same World as the current Torch
				if(!t.getLocation().getWorld().equals(mobLocation.getWorld())) continue;
				
				//Check if the distance cylindrical is less than the defined range squared
				if(TorchHandler.getDistanceCylindrical(t.getLocation(), mobLocation) < HaroTorch.RANGE) {
					event.setCancelled(true);
					
					break;
				}
			}
		}
	}
}
