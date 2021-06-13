package nl.thedutchmc.harotorch.commands;

import org.bukkit.command.CommandSender;

import nl.thedutchmc.harotorch.HaroTorch;

public interface SubCommand {	
	public boolean run(HaroTorch plugin, CommandSender sender, String[] args);
}
