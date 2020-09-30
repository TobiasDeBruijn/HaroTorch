package nl.thedutchmc.harotorch.commands.torchSubCmds;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.haro_torch.plugin.torch.Torchy;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.torch.Torch;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class ConvertExecutor {

	public static boolean convert(CommandSender sender) {
		
		if(Bukkit.getPluginManager().getPlugin("HaroTorch") == null) {
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "HaroTorch v1 is not installed. Aborting.");
			return true;
		}
		
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "Converting HaroTorch v1 Torches to v2...");
		
		List<Torchy> torchys = nl.thedutchmc.haro_torch.plugin.torch.TorchHandler.getTorches();
		
		for(Torchy tOld : torchys) {
			
			final Location loc = tOld.getLocation();
			final UUID owner = tOld.getOwner();
			
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "Converting: " + ChatColor.RED + owner.toString() + 
					ChatColor.GOLD + "at: " + ChatColor.RED + loc.getBlockX() + 
					ChatColor.GOLD + ", " + ChatColor.RED + loc.getBlockY() + 
					ChatColor.GOLD + ", " + ChatColor.RED + loc.getBlockZ() + 
					ChatColor.GOLD + " in: " + ChatColor.RED + loc.getWorld().getName());
			
			TorchHandler.addTorch(new Torch(owner, loc));
		}
		
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "Conversion complete. Disabling HaroTorch v1!");
		
		Plugin haroTorchV1 = Bukkit.getPluginManager().getPlugin("HaroTorch");
		
		Bukkit.getPluginManager().disablePlugin(haroTorchV1);
		
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "Done.");
		
		return true;
	}
}
