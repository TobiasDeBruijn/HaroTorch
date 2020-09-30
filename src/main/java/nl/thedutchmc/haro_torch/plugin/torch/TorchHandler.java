package nl.thedutchmc.haro_torch.plugin.torch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TorchHandler {

	private static List<Torchy> torches = new ArrayList<Torchy>();

	public static void add(Torchy torch) {
		torches.add(torch);
	}

	public static boolean remove(Torchy torch) {
		return torches.remove(torch);
	}

	public static List<Torchy> getTorches() {
		return torches;
	}

	public static UUID getTorchOwner(Location location) {
		for (Torchy torch : torches) {
			if (torch.getLocation().equals(location)) {
				return torch.getOwner();
			}
		}
		return null;
	}

	public static List<Location> getOwnerLocations(UUID owner) {
		List<Location> locations = new ArrayList<Location>();
		for (Torchy torch : torches) {
			if (torch.getOwner().equals(owner)) {
				locations.add(torch.getLocation());
			}
		}
		return locations;
	}

	public static ItemStack getNewTorch(int count) {
		ItemStack torch = new ItemStack(Material.WHITE_STAINED_GLASS);
		ItemMeta meta = torch.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		meta.getLore();
		lore.add("This isn't a normal torch!");
		lore.add("well it is in some ways");
		lore.add("but it's not!");
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.AQUA + "Haro's Torch");
		torch.setItemMeta(meta);
		torch.setAmount(count);
		return torch;
	}

	public static Torchy getHaroTorch(Location location) {
		for (Torchy torch : torches) {
			if (torch.getLocation().equals(location)) {
				return torch;
			}
		}
		return null;
	}

	public static boolean isHaroTorch(Block block) {
		if (block.getType() != Material.WHITE_STAINED_GLASS) {
			return false;
		}
		for (Torchy torch : torches) {
			if (torch.getLocation().equals(block.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public static double getDistanceCylindrical(Location locationA, Location locationB) {
		return Math.pow((locationA.getX() - locationB.getX()), 2) + Math.pow((locationA.getZ() - locationB.getZ()), 2);
	}

	public static List<Torchy> getTorchesNearPlayer(Player player) {
		List<Torchy> toReturn = new ArrayList<Torchy>();

		for (Torchy torch : torches) {
			if(torch.getLocation().getWorld().equals(player.getLocation().getWorld())) {
				if (torch.getLocation().distanceSquared(player.getLocation()) < (32 * 32)) {
					toReturn.add(torch);		
				}
			}
		}
		return toReturn;
	}

}