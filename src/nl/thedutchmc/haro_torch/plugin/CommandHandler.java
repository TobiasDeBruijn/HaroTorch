package nl.thedutchmc.haro_torch.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CommandHandler implements CommandExecutor {

	@SuppressWarnings("unused") //SuppressWarning because it may be used in the future
	private Plugin plugin;
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
					sender.sendMessage("- " + ChatColor.GOLD + "/ht torch <set/remove> <x> <y> <z>: " + ChatColor.WHITE + "Set or remove a Torch");
					return true;
				} else if (args[0].equalsIgnoreCase("torch")) {
					if(args.length >= 2) {
						if(args[1].equalsIgnoreCase("set")) {
							if(sender instanceof Player) {
								if(args.length == 5) {
									Player p = (Player)sender;
									if(p.getInventory().containsAtLeast(new ItemStack(Material.NETHER_STAR), 5) && p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND_BLOCK), 10)) {
										//TODO create torch
										
										int x = Integer.valueOf(args[2]);
										int y = Integer.valueOf(args[3]);
										int z = Integer.valueOf(args[4]);
										World w = p.getWorld();
										
										Location newLoc = new Location(w,x,y,z);
										HaroTorch.locs.add(newLoc);
										HaroTorch.locsWithOwner.put(newLoc, ((Player) sender).getPlayer().getUniqueId());
										
										System.out.println(HaroTorch.locsWithOwner.size());
										
										haroTorch.saveToConfig();
										return true;
									} else {
										sender.sendMessage(ChatColor.RED + "You do not have enough materials to create a torch!");
										return true;
									}
								} else { 
									sender.sendMessage(ChatColor.RED + "Missing options!");
									return true;
								}
							} else {
								sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
								return true;
							}
						} else if (args[1].equalsIgnoreCase("remove")) {
							//TODO Remove Torch
							
							//TEMP
							System.out.println("remove torch");
							
							return true;
						} else if(args[1].equalsIgnoreCase("list")) {
							sender.sendMessage(ChatColor.GOLD + "All torches:");
							
							for(int i = 0; i == HaroTorch.locs.size(); i++) {
								double x = HaroTorch.locs.get(i).getX();
								double y = HaroTorch.locs.get(i).getY();
								double z = HaroTorch.locs.get(i).getZ();
								String w = HaroTorch.locs.get(i).getWorld().toString();
								System.out.println(x);
								sender.sendMessage("- " + ChatColor.GOLD + "X: " + x + " Y: " + y + " Z: " + z + " World: " + w);	 
							}
							return true;
							
						} else {
							sender.sendMessage(ChatColor.RED + "Invalid option!");
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Missing option!");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Invalid option!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "Haro's Torch Base command. use /ht help for a list of commands!");
				return true;
			}
		} else if(command.getName().equalsIgnoreCase("stc")) {
			haroTorch.saveToConfig();
			return true;
		} return false;
	}
}
