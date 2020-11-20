package nl.thedutchmc.harotorch.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.commands.torchSubCmds.AdminCommandExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.ConvertExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.GiveExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.HelpExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.HighlightAreaOfEffectExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.HighlightExecutor;
import nl.thedutchmc.harotorch.commands.torchSubCmds.VersionExecutor;
import nl.thedutchmc.harotorch.lang.LangHandler;

public class TorchCommandExecutor implements CommandExecutor {

	private HaroTorch plugin;
	
	public TorchCommandExecutor(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length < 1) {
			String msg = LangHandler.activeLang.getLangMessages().get("missingArguments").replaceAll("%HELP_COMMAND%", ChatColor.RED + "/torch help" + ChatColor.GOLD);
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + msg);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("help")) {
			if(!sender.hasPermission("harotorch.help")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
				return true;
			}
			
			return HelpExecutor.help(sender);
		}
		
		else if(args[0].equalsIgnoreCase("version")) {
			if(!sender.hasPermission("harotorch.version")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
				return true;
			}
			
			return VersionExecutor.version(sender, plugin);
		}
		
		else if(args[0].equalsIgnoreCase("convert")) {
			if(!sender.hasPermission("harotorch.convert")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
				return true;
			}
			
			return ConvertExecutor.convert(sender);
		}
		
		else if(args[0].equalsIgnoreCase("give")) {
			if(!sender.hasPermission("harotorch.give")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
				return true;
			}
			
			return GiveExecutor.give(sender, args);
		}
		
		else if(args[0].equalsIgnoreCase("highlight")) {
			if(!sender.hasPermission("harotorch.highlight")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
				return true;
			}
			
			return HighlightExecutor.highlight(sender, args, plugin);
		}
		
		else if(args[0].equalsIgnoreCase("aoe")) {
			if(!sender.hasPermission("harotorch.aoe")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
				return true;
			}
			
			HighlightAreaOfEffectExecutor.aoe(sender, plugin);
			
			return true;
		}
		
		else if(args[0].equalsIgnoreCase("admin")) {
			if(!sender.hasPermission("harotorch.admin")) {
				sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
				return true;
			}
			
			AdminCommandExecutor.admin(sender);
			
			return true;
		}
		
		return false;
	}
	
}
