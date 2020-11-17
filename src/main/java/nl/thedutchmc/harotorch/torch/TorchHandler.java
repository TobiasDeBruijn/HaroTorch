package nl.thedutchmc.harotorch.torch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;

public class TorchHandler {
	
	private static HaroTorch plugin;
	
	private static HashMap<Location, Torch> torches = new HashMap<>();
	private static StorageHandler STORAGE;
	
	public TorchHandler(HaroTorch plugin) {		
		TorchHandler.plugin = plugin;
	}
	
	public void setup() {
		STORAGE = new StorageHandler(plugin);
		
		for(Torch t : STORAGE.read()) {
			torches.put(t.getLocation(), t);
		}
	}
	
	public static void addTorch(Torch torch) {
		torches.put(torch.getLocation(), torch);
		STORAGE.write(torch);
	}
	
	public static void removeTorch(Torch torch) {
		torches.remove(torch.getLocation());	
		STORAGE.remove(torch);
	}
	
	public static boolean isTorch(Location loc) {
		return torches.containsKey(loc);
	}
	
	public static Torch getTorch(Location loc) {
		return torches.get(loc);
	}
	
	public static UUID getTorchOwner(Location loc) {
		return torches.get(loc).getTorchOwner();
	}
	
	public static List<Torch> getTorches() {
		List<Torch> result = new ArrayList<>();
		
		for(Map.Entry<Location, Torch> entry : torches.entrySet()) {
			result.add(entry.getValue());
		}
		
		return result;
	}
	
	/**
	 * @param torch The Torch to return the location for.
	 * @return The location associated with the provided Torch. Returns null if the Torch is not registered.
	 */
	public static Location getLocation(Torch torch) {
		for(Map.Entry<Location, Torch> entry : torches.entrySet()) {
			
			if(entry.getValue().equals(torch)) {
				return entry.getKey();
			}
		}
		
		return null;
	}

	public static ItemStack getTorch(int count) {
		
		ItemStack stack = new ItemStack(Material.matchMaterial(HaroTorch.getConfigHandler().torchBlock));
		ItemMeta meta = stack.getItemMeta();

		List<String> lore = new ArrayList<>();
		lore.add(LangHandler.activeLang.getLangMessages().get("torchLore").replaceAll("%TORCH_RADIUS%", String.valueOf(HaroTorch.getConfigHandler().torchRange)));
		
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', LangHandler.activeLang.getLangMessages().get("torchTitle")));
		
		NamespacedKey key = new NamespacedKey(plugin, "haro_torch");
		meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
		
		stack.setItemMeta(meta);
		stack.addUnsafeEnchantment(Enchantment.MENDING, 1);
		stack.setAmount(count);
		
		return stack;
	}
	
	public static double getDistanceCylindrical(Location locationA, Location locationB) {
		return Math.pow((locationA.getX() - locationB.getX()), 2) + Math.pow((locationA.getZ() - locationB.getZ()), 2);
	}
	
	public static List<Location> getTorchLocationsNearPlayer(Player player, int radius) {
		List<Location> result = new ArrayList<>();
		
		for(Map.Entry<Location, Torch> entry : torches.entrySet()) {
			Location l = entry.getKey();
			
			if(!l.getWorld().getName().equals(player.getLocation().getWorld().getName())) continue;
			
			if(l.distanceSquared(player.getLocation()) < Math.pow(radius, 2)) {
				result.add(l);
			}
		}
		
		return result;
	}
}
