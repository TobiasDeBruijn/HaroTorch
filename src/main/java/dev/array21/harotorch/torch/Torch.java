package dev.array21.harotorch.torch;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Torch implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UUID torchOwner;
	private final int x, y, z;
	private final String worldName;
	
	public Torch(UUID torchOwner, Location torchLocation) {
		this.torchOwner = torchOwner;
				
		this.x = torchLocation.getBlockX();
		this.y = torchLocation.getBlockY();
		this.z = torchLocation.getBlockZ();
		this.worldName = torchLocation.getWorld().getName();
	}
	
	public UUID getTorchOwner() {
		return this.torchOwner;
	}
	
	public void setOwner(UUID newTorchOwner) {
		this.torchOwner = newTorchOwner;
	}
	
	public Location getLocation() {
		return new Location(Bukkit.getWorld(worldName), x, y, z);
	}
}
