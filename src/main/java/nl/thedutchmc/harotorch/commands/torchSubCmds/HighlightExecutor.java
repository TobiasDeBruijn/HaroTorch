package nl.thedutchmc.harotorch.commands.torchSubCmds;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class HighlightExecutor {
	
	public static boolean highlight(CommandSender sender, String[] args, HaroTorch plugin) {
		
		List<Location> nearbyTorches = TorchHandler.getTorchLocationsNearPlayer((Player) sender, HaroTorch.getConfigHandler().torchHighlightRange);
		Player p = (Player) sender;
		
		List<Integer> returnedIds;
		
		switch(HaroTorch.NMS_VERSION) {
		case "v1_16_R2": returnedIds = Highlight_1_16_r2.spawnHighlight(p, nearbyTorches); break;
		default:
			p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "The highlight feature is not supported on this version of Minecraft! (" + HaroTorch.NMS_VERSION + ")");
			return true;
		}
		
		p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "Highlighting all Torches for " + ChatColor.RED + HaroTorch.getConfigHandler().torchHighlightTime + ChatColor.GOLD + " seconds!");
		
		new BukkitRunnable() {
			
			@Override
			public void run() {

				switch(HaroTorch.NMS_VERSION) {
				case "v1_16_R2": Highlight_1_16_r2.killHighlighted(returnedIds, p); break;
				
				}
				
				p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "Highlighting has ended!");

			}
		}.runTaskLater(plugin, HaroTorch.getConfigHandler().torchHighlightTime * 20);
		
		
		return true;
	}
}
