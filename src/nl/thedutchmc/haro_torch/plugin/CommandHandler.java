package nl.thedutchmc.haro_torch.plugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.thedutchmc.haro_torch.plugin.torch.TorchHandler;
import nl.thedutchmc.haro_torch.plugin.torch.Torchy;

public class CommandHandler implements CommandExecutor {

	private HaroTorch plugin;

	public CommandHandler(HaroTorch plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("ht")) {
			if (!(args.length == 0)) {
				if (args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.GOLD + "- Haro's Torch help page -");
					sender.sendMessage(ChatColor.GOLD + "--------------------------");
					sender.sendMessage("- " + ChatColor.GOLD + "/ht: " + ChatColor.WHITE + "Haro's Torch base command");
					sender.sendMessage("- " + ChatColor.GOLD + "/ht help: " + ChatColor.WHITE + "Help page");
					sender.sendMessage("- " + ChatColor.GOLD + "/ht list: " + ChatColor.WHITE + "Lists all your Torches");
					return true;
				} else if (args[0].equalsIgnoreCase("list")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						UUID playerUuid = p.getUniqueId();
						sender.sendMessage("Your torches:");
						for (Torchy torch : TorchHandler.getTorches()) {
							if (torch.getOwner().equals(playerUuid)) {
								Location location = torch.getLocation();
								int x = location.getBlockX();
								int y = location.getBlockY();
								int z = location.getBlockZ();
								Environment e = location.getWorld().getEnvironment();
								String w = e.equals(Environment.NORMAL) ? "Overworld" : e.toString().toLowerCase();
								sender.sendMessage("Torch: X: " + ChatColor.GOLD + x + ChatColor.WHITE + " Y: " + ChatColor.GOLD + y + ChatColor.WHITE + " Z: " + ChatColor.GOLD + z + ChatColor.WHITE + " In world: " + ChatColor.GOLD + w);
							}
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.GOLD + "Not a player, listing all Torches and their locations:");
						for (Torchy torch : TorchHandler.getTorches()) {
							Player player = Bukkit.getPlayer(torch.getOwner());
							Location location = torch.getLocation();
							int x = location.getBlockX();
							int y = location.getBlockY();
							int z = location.getBlockZ();
							Environment e = location.getWorld().getEnvironment();
							String w = e.equals(Environment.NORMAL) ? "Overworld" : e.toString().toLowerCase();
							sender.sendMessage("Location: " + x + " " + y + " " + z + " in world: " + w + " Owner: " + player.getName());
						}
						return true;
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					sender.sendMessage(ChatColor.GOLD + "Checking if you are near a torch...");
					Player player = (Player) sender;
					Location playerLoc = player.getLocation();
					for (Torchy torch : TorchHandler.getTorches()) {
						Player owner = Bukkit.getPlayer(torch.getOwner());
						Location location = torch.getLocation();
						int x = location.getBlockX();
						int y = location.getBlockY();
						int z = location.getBlockZ();
						if (torch.getLocation().distanceSquared(playerLoc) < plugin.playerCommandCheckRadiusSq)
							sender.sendMessage(ChatColor.GOLD + "Torch at: " + ChatColor.RED + x + " " + y + " " + z + ChatColor.GOLD + " Owned by: " + ChatColor.DARK_RED + owner.getName());
					}
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid argument. Run /ht help for a list of commands!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "HaroTorch base command. use /ht help for a list of commands");
				return true;
			}
		}
		return false;
	}
}
