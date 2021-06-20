package dev.array21.harotorch.commands;

import org.bukkit.command.CommandSender;

import dev.array21.harotorch.HaroTorch;

public interface SubCommand {	
	public boolean run(HaroTorch plugin, CommandSender sender, String[] args);
}
