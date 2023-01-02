package dev.array21.harotorch.commands;

import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.ChatColor;
import dev.array21.harotorch.HaroTorch;
import dev.array21.harotorch.commands.torchSubCmds.GiveExecutor;
import dev.array21.harotorch.commands.torchSubCmds.HelpExecutor;
import dev.array21.harotorch.commands.torchSubCmds.HighlightAreaOfEffectExecutor;
import dev.array21.harotorch.commands.torchSubCmds.HighlightExecutor;
import dev.array21.harotorch.commands.torchSubCmds.VersionExecutor;
import dev.array21.harotorch.lang.LangHandler;

public class TorchCommandExecutor implements CommandExecutor {

	private final HaroTorch plugin;
	private final HashMap<String, SubCommand> subcommands;
	
	public TorchCommandExecutor(HaroTorch plugin) {
		this.plugin = plugin;
		
		HashMap<String, SubCommand> subCommands = new HashMap<>();
		subCommands.put("help", new HelpExecutor());
		subCommands.put("version", new VersionExecutor());
		subCommands.put("give", new GiveExecutor());
		subCommands.put("highlight", new HighlightExecutor());
		subCommands.put("aoe", new HighlightAreaOfEffectExecutor());
		this.subcommands = subCommands;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				
		if(args.length < 1) {
			String msg = LangHandler.activeLang.getLangMessages().get("missingArguments").replaceAll("%HELP_COMMAND%", ChatColor.RED + "/torch help" + ChatColor.GOLD);
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + msg);
			return true;
		}
		
		SubCommand c = this.subcommands.get(args[0]);
		if(!sender.hasPermission("harotorch." + args[0])) {
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("noPermission"));
			return true;
		}
		
		c.run(this.plugin, sender, args);
		return true;
	}
}
