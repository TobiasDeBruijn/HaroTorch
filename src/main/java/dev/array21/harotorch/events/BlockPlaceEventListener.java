package dev.array21.harotorch.events;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import dev.array21.harotorch.HaroTorch;
import dev.array21.harotorch.lang.LangHandler;
import dev.array21.harotorch.torch.Torch;
import dev.array21.harotorch.torch.TorchHandler;

public class BlockPlaceEventListener implements Listener {

	private HaroTorch plugin;
	
	public BlockPlaceEventListener(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		
		ItemStack blockPlacing = event.getPlayer().getInventory().getItemInMainHand();
		
		// If the itemstack has no metadata, we know it isn't a HaroTorch
		if(!blockPlacing.hasItemMeta()) return;
		
		NamespacedKey key = new NamespacedKey(plugin, "haro_torch");
		
		// If the itemstack doesn't have an Integer persistent data type, we know it isn't a HaroTorch
		if(!blockPlacing.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;
		
		if(blockPlacing.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER).equals(1)) {
			// We now know we're dealing with a HaroTorch
			
			Location torchLocation = event.getBlock().getLocation();
			
			// Check if the torch is allowed to be placed where the Player wants to place it
			// by checking the Material of the Block below where the Torch is to be placed
			Location blockBelow = new Location(torchLocation.getWorld(), torchLocation.getX(), torchLocation.getY() -1, torchLocation.getZ());
			if(this.plugin.getConfigManifest().getDissallowedPlacementOn().contains(blockBelow.getBlock().getType())) {
				event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD  + LangHandler.activeLang.getLangMessages().get("torchPlacementNotAllowedOnBlock"));
				
				event.setCancelled(true);
				return;
			}
			
			// Get the amount of torches placed by the Player
			Integer torchCountNullable = TorchHandler.getTorchCountForPlayer(event.getPlayer().getUniqueId());
			int torchCount = (torchCountNullable != null) ? torchCountNullable : 0;
			
			Integer torchPlaceLimit = this.plugin.getConfigManifest().torchPlaceLimit;
			
			// Check that the Player is still under the Torch limit
			if(torchPlaceLimit != null && torchPlaceLimit != -1 && torchCount >= torchPlaceLimit) {
				event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("torchLimitReached")
						.replaceAll("%TORCH_LIMIT%", ChatColor.RED + String.valueOf((int) torchPlaceLimit) + ChatColor.GOLD)
						.replaceAll("%PLACED_TORCHES%", ChatColor.RED + String.valueOf(torchCount) + ChatColor.GOLD));
				
				event.setCancelled(true);
				return;
			}
			
			// Place the Torch
			TorchHandler.addTorch(new Torch(event.getPlayer().getUniqueId(), torchLocation));
			
			event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("torchPlaced"));	
		}
	}
}
