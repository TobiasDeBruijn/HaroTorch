package nl.thedutchmc.haro_torch.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
						for(Map.Entry<Location, UUID> entry : HaroTorch.locsWithOwner.entrySet()) {
							Location loc = entry.getKey();
							UUID uuid = entry.getValue();
						
							Player player = Bukkit.getPlayer(uuid);
							String playerName = player.getName();
							int x = (int) loc.getX();
							int y = (int) loc.getY();
							int z = (int) loc.getZ();
							World world = loc.getWorld();
							String worldName = world.getName();
							
							sender.sendMessage("Location: " + x + " " + y + " " + z + " in world: " + worldName + " Owner: " + playerName);
						}
						return true;	
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					sender.sendMessage(ChatColor.GOLD + "Checking if you are near a torch...");
					Player player = (Player) sender;
					Location playerLoc = player.getLocation();
					double playerX = playerLoc.getX();
					double playerZ = playerLoc.getZ();
					UUID uuid = player.getUniqueId();
					
					Map<Location, UUID> torchesInRange = new HashMap<Location, UUID>();
					
					for(Map.Entry<Location, UUID> entry : HaroTorch.locsWithOwner.entrySet()) {
						Location torchLoc = entry.getKey();
						double torchX = torchLoc.getX();
						double torchZ = torchLoc.getZ();
						
						double distance = Math.sqrt(Math.pow(torchX - playerX, 2) + Math.pow(torchZ - playerZ, 2));
						if(distance <= haroTorch.playerCommandCheckRadius) {
							torchesInRange.put(torchLoc,uuid);
						}
					}
					
					if(torchesInRange.size() != 0) {
						for(Map.Entry<Location, UUID> entry : torchesInRange.entrySet()) {
							Location torchInRangeLoc = entry.getKey();
							
							int torchInRangeX = (int) torchInRangeLoc.getX();
							int torchInRangeY = (int) torchInRangeLoc.getY();
							int torchInRangeZ = (int) torchInRangeLoc.getZ();

							
							String torchInRangeOwner = Bukkit.getPlayer(uuid).getName();
							
							sender.sendMessage(ChatColor.GOLD + "Torch at: " + ChatColor.RED + torchInRangeX + " " + torchInRangeY + " " + torchInRangeZ + ChatColor.GOLD + " Owned by: " + ChatColor.DARK_RED + torchInRangeOwner);
						}
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
		} return false;
	}
}
