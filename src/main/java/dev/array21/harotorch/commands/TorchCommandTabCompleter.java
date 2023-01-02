package dev.array21.harotorch.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TorchCommandTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length == 1) {
			List<String> result = new ArrayList<>();
			if(sender.hasPermission("harotorch.highlight")) result.add("highlight");
			if(sender.hasPermission("harotorch.help")) result.add("help");
			if(sender.hasPermission("harotorch.give")) result.add("give");
			if(sender.hasPermission("harotorch.version")) result.add("version");
			if(sender.hasPermission("harotorch.aoe")) result.add("aoe");
			
			return result;
		}

		return new ArrayList<>();
	}
	
}
