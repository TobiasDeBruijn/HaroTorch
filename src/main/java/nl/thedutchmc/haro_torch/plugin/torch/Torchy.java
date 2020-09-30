package nl.thedutchmc.haro_torch.plugin.torch;

import java.util.UUID;

import org.bukkit.Location;

public class Torchy {

	private UUID owner;
	private Location location;

	public Torchy(UUID owner, Location location) {
		this.owner = owner;
		this.location = location;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
