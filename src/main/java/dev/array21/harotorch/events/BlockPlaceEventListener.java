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
		
		
		if(!blockPlacing.hasItemMeta()) return;
		
		NamespacedKey key = new NamespacedKey(plugin, "haro_torch");
		
		if(!blockPlacing.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;
		
		if(blockPlacing.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER).equals(1)) {
			Integer torchCountNullable = TorchHandler.getTorchCountForPlayer(event.getPlayer().getUniqueId());
			int torchCount = (torchCountNullable != null) ? torchCountNullable : 0;
			
			Integer torchPlaceLimit = this.plugin.getConfigManifest().torchPlaceLimit;
			if(torchPlaceLimit != null && torchPlaceLimit != -1 && torchCount >= torchPlaceLimit) {
				event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("torchLimitReached")
						.replaceAll("%TORCH_LIMIT%", ChatColor.RED + String.valueOf((int) torchPlaceLimit) + ChatColor.GOLD)
						.replaceAll("%PLACED_TORCHES%", ChatColor.RED + String.valueOf(torchCount) + ChatColor.GOLD));
				
				event.setCancelled(true);
				return;
			}
			
			Location l = event.getBlock().getLocation();
			TorchHandler.addTorch(new Torch(event.getPlayer().getUniqueId(), l));
			
			event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("torchPlaced"));	
		}
	}
}
