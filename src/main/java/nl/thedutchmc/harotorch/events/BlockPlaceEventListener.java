package nl.thedutchmc.harotorch.events;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;
import nl.thedutchmc.harotorch.torch.Torch;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class BlockPlaceEventListener implements Listener {

	private HaroTorch plugin;
	
	public BlockPlaceEventListener(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		
		ItemStack blockPlacing = event.getPlayer().getItemInHand();
		
		if(!blockPlacing.hasItemMeta()) return;
		
		NamespacedKey key = new NamespacedKey(plugin, "haro_torch");
		
		if(!blockPlacing.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) return;
		
		if(blockPlacing.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER).equals(1)) {
			
			Location l = event.getBlock().getLocation();
			TorchHandler.addTorch(new Torch(event.getPlayer().getUniqueId(), l));
			
			event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("torchPlaced"));	
		}
	}
}
