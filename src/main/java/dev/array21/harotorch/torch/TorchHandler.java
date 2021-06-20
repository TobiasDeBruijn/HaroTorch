package dev.array21.harotorch.torch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import dev.array21.harotorch.HaroTorch;
import dev.array21.harotorch.annotations.Nullable;
import dev.array21.harotorch.config.ConfigManifest.TorchRangeShape;
import dev.array21.harotorch.lang.LangHandler;

public class TorchHandler {
	
	private static HaroTorch plugin;
	
	private static HashMap<Location, Torch> torches = new HashMap<>();
	private static HashMap<UUID, Integer> playerTorchCounter = new HashMap<>();
	private static StorageHandler STORAGE;
	
	public TorchHandler(HaroTorch plugin) {		
		TorchHandler.plugin = plugin;
	}
	
	public void setup() {
		STORAGE = new StorageHandler(plugin);
		
		for(Torch t : STORAGE.read()) {
			torches.put(t.getLocation(), t);
			playerTorchCounter.merge(t.getTorchOwner(), 1, Integer::sum);
		}
	}
	
	public static void addTorch(Torch torch) {
		playerTorchCounter.merge(torch.getTorchOwner(), 1, Integer::sum);
		
		torches.put(torch.getLocation(), torch);
		STORAGE.write(torch);
	}
	
	public static void removeTorch(Torch torch) {
		playerTorchCounter.merge(torch.getTorchOwner(), -1, Integer::sum);
		
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
		
		ItemStack stack = new ItemStack(Material.matchMaterial(TorchHandler.plugin.getConfigManifest().torchBlock));
		ItemMeta meta = stack.getItemMeta();

		List<String> lore = new ArrayList<>();
		lore.add(LangHandler.activeLang.getLangMessages().get("torchLore").replaceAll("%TORCH_RADIUS%", String.valueOf(TorchHandler.plugin.getConfigManifest().torchRange)));
		
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
			
			World playerWorld = player.getLocation().getWorld();
			//Issue #9
			if(playerWorld == null || l.getWorld() == null) {
				continue;
			}
			
			if(!l.getWorld().getName().equals(playerWorld.getName())) {
				continue;
			}
			
			if(TorchHandler.plugin.getConfigManifest().getTorchRangeShape() == TorchRangeShape.CIRCLE) {
				if(l.distanceSquared(player.getLocation()) < Math.pow(radius, 2)) {
					result.add(l);
				}
			} else {
				Location lPlayer = player.getLocation();
				double distanceX = Math.abs(l.getX() - lPlayer.getX());
				double distanceZ = Math.abs(l.getZ() - lPlayer.getZ());
				
				if(distanceX < radius && distanceZ < radius) {
					result.add(l);
				}
			}
		}
		
		return result;
	}
	
	@Nullable
	public static Integer getTorchCountForPlayer(UUID uuid) {
		return playerTorchCounter.get(uuid);
	}
}
