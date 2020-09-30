package nl.thedutchmc.harotorch.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.commands.torchSubCmds.ConvertExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.GiveExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.HighlightExecutor;

public class TorchCommandExecutor implements CommandExecutor {

	private HaroTorch plugin;
	
	public TorchCommandExecutor(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length < 1) {
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "Missing arguments! Use " + ChatColor.RED + "/torch help" + ChatColor.GOLD + " for help!");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("help")) {
			
			return true;
		}
		
		else if(args[0].equalsIgnoreCase("convert")) {
			if(!sender.hasPermission("harotorch.convert")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + "You do not have permission to use this command!");
				return true;
			}
			
			return ConvertExecutor.convert(sender);
		}
		
		else if(args[0].equalsIgnoreCase("give")) {
			if(!sender.hasPermission("harotorch.give")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + "You do not have permission to use this command!");
				return true;
			}
			
			return GiveExecutor.give(sender, args);
		}
		
		else if(args[0].equalsIgnoreCase("highlight")) {
			if(!sender.hasPermission("harotorch.highlight")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + "You do not have permission to use this command!");
				return true;
			}
			
			return HighlightExecutor.highlight(sender, args, plugin);
		}
		
		
		return false;
	}
	
}
