package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
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
		
		//We dont want to block Player spawns
		if(e instanceof Player) return;
		
		//We dont want to prevent non living entities from spawning
		if(!(e instanceof LivingEntity)) return;
		
		//If the spawn reason is a spawner, or via infection (zombie villagers), return.
		if(event.getSpawnReason().equals(SpawnReason.SPAWNER) && !event.getSpawnReason().equals(SpawnReason.INFECTION)) return;

		EntityType et = event.getEntityType();

		//If the spawned mob is in the exclusion list, we dont want to block it
		if(HaroTorch.getConfigHandler().mobExclusionList.contains(et)) return;
		
		//Check if we should only be blocking hostile mobs
		if(HaroTorch.getConfigHandler().onlyBlockHostileMobs) {
		
			//Check if the spawned Entity is a Monster, Phantom or Slime and not a Wither
			if(e instanceof Monster || et.equals(EntityType.PHANTOM) || et.equals(EntityType.SLIME) && !et.equals(EntityType.WITHER) && !et.equals(EntityType.ENDER_DRAGON)) {
				if(torchInRange(event.getLocation())) event.setCancelled(true);
			}
		} else {
			//Check if the spawned entity is an ender dragon or wither, we dont want to block those
			if(et.equals(EntityType.ENDER_DRAGON) || et.equals(EntityType.WITHER)) return;
						
			if(torchInRange(event.getLocation())) event.setCancelled(true);
		}
	}
	
	private boolean torchInRange(Location entityLocation) {
		//Iterate over all Torches
		for(Torch t : TorchHandler.getTorches()) {
			
			//Check if the spawn occured in the same World as the current Torch
			if(!t.getLocation().getWorld().equals(entityLocation.getWorld())) continue;
			
			//Check if the distance cylindrical is less than the defined range squared
			if(TorchHandler.getDistanceCylindrical(t.getLocation(), entityLocation) < HaroTorch.RANGE) {
				return true;
			}
		}
		
		return false;
	}
}
