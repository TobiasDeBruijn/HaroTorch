package nl.thedutchmc.haro_torch.plugin;

import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandHandler implements CommandExecutor {

	@SuppressWarnings("unused") //SuppressWarning because it may be used in the future
	private Plugin plugin;
	@SuppressWarnings("unused") //SuppressWarning because it may be used in the future
	private HaroTorch haroTorch;
	
	//Plugin not used at the moment, for future commands
	public CommandHandler(HaroTorch plugin, HaroTorch haroTorch) {
		this.plugin = plugin;
		this.haroTorch = haroTorch;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("ht")) {
			if(!(args.length == 0)) {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.GOLD + "- Haro's Torch help page -");
					sender.sendMessage(ChatColor.GOLD + "--------------------------");
					sender.sendMessage("- " + ChatColor.GOLD + "/ht: " + ChatColor.WHITE + "Haro's Torch base command");
					sender.sendMessage("- " + ChatColor.GOLD + "/ht help: " + ChatColor.WHITE + "Help page");
					sender.sendMessage("- " + ChatColor.GOLD + "/ht list: " + ChatColor.WHITE + "Lists all your Torches");
					return true;
				} else if (args[0].equalsIgnoreCase("list")) {
					if(sender instanceof Player ) {
						Player p = (Player) sender;
						UUID playerUuid = p.getUniqueId();
						
						sender.sendMessage("Your torches:");
						
						for(Map.Entry<Location, UUID> entry : HaroTorch.locsWithOwner.entrySet()) {
							Location loc = entry.getKey();
							
							if(HaroTorch.locsWithOwner.get(loc).equals(playerUuid)) {
								int x = (int) loc.getX();
								int y = (int) loc.getY();
								int z = (int) loc.getZ();
								String w = loc.getWorld().getName();
								
								sender.sendMessage("Torch: X: " + ChatColor.GOLD + x + ChatColor.WHITE + " Y: " + ChatColor.GOLD + y + ChatColor.WHITE +" Z: " + ChatColor.GOLD + z + ChatColor.WHITE + " In world: " + ChatColor.GOLD + w);
							} 
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.GOLD + "Not a player, listing all Torches and their locations:");
						//TODO console torch list
						return true;	
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid argument. Run /ht help for a list of commands!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "HaroTorch base command. use /ht help for a list of commands");
				return true;
			}
		} return false;
	}
}
