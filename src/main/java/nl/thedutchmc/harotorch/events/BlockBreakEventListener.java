package nl.thedutchmc.harotorch.events;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;
import nl.thedutchmc.harotorch.torch.Torch;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class BlockBreakEventListener implements Listener {

	private HaroTorch plugin;
	
	public BlockBreakEventListener(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		
		Location loc = event.getBlock().getLocation();
		
		Location loc_y_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
		Location loc_x_plus_1 = new Location(loc.getWorld(), loc.getX() +1, loc.getY(), loc.getZ());
		Location loc_x_minus_1 = new Location(loc.getWorld(), loc.getX() -1, loc.getY(), loc.getZ());
		Location loc_z_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() +1);
		Location loc_z_minus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() -1);
		
		if(
				(
					TorchHandler.isTorch(loc_y_plus_1)
					|| TorchHandler.isTorch(loc_x_plus_1)
					|| TorchHandler.isTorch(loc_x_minus_1)
					|| TorchHandler.isTorch(loc_z_plus_1)
					|| TorchHandler.isTorch(loc_z_minus_1)
				)
			&& !TorchHandler.isTorch(loc)) {
			
			event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("blockBreakNotAllowedTorchOntop"));
			event.setCancelled(true);
			
			return;
		}
		
		if(!TorchHandler.isTorch(loc)) {
			return;
		}
		
		UUID torchOwner = TorchHandler.getTorchOwner(loc);
		if(!this.plugin.getConfigManifest().allowRemoveNotOwnedTorch && !event.getPlayer().hasPermission("harotorch.breakall") && !event.getPlayer().getUniqueId().equals(torchOwner)) {
			event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("blockBreakNotAllowedOwnerMismatch"));
			
			event.setCancelled(true);
			
			return;
		}
		
		event.setDropItems(false);
		
		Torch t = TorchHandler.getTorch(loc);
		TorchHandler.removeTorch(t);
		
		ItemStack torchStack = TorchHandler.getTorch(1);
		
		event.getBlock().getWorld().dropItemNaturally(loc, torchStack);
		
		event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("torchBroken"));
	}
}
