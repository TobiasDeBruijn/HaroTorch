package nl.thedutchmc.harotorch.commands.torchSubCmds;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class GiveExecutor {

	public static boolean give(CommandSender sender, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("commandOnlyForPlayers"));
			return true;
		}
		
		int count = 0;
		
		if(args.length < 2) {
			count = 1;
		} else { 
			
			//Check if the quantity provided is a number and act accordingly
			if(isNumeric(args[1])) {
				count = Integer.valueOf(args[1]);
				
			} else {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("quantityNaN"));
				return true;
			}
		}
		
		if(count <= 0) {
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("quantityNegOrZero"));
			return true;
		}
		
		((Player) sender).getInventory().addItem(TorchHandler.getTorch(count));
		
		return true;
	}
	
	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); 
	}
	
}
