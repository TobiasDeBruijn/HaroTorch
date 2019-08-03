package nl.thedutchmc.haro_torch.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HaroTorch extends JavaPlugin implements Listener {
	
    public Recipes recipes = new Recipes(this);
	
	List<Location> locs = new ArrayList<Location>();
	Map<UUID, Location> locsByOwner = new HashMap<UUID, Location>();
	double mobBlockRadius = 48;
	
	private File customConfigFile;
    private FileConfiguration customConfig;
	
	@Override
	public void onEnable() {
		getServer().addRecipe(recipes.getHaroTorchRecipe());
        getServer().getPluginManager().registerEvents(this, this);
        
        createCustomConfig();        
	}
	
	@Override
	public void onDisable() {
		saveToConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender,
							Command command,
				            String label,
				            String[] args) {
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
										
										//TEMP
										System.out.println("create torch about now");
										int x = Integer.valueOf(args[2]);
										int y = Integer.valueOf(args[3]);
										int z = Integer.valueOf(args[4]);
										World w = p.getWorld();
										
										Location newLoc = new Location(w,x,y,z);
										locs.add(newLoc);
										locsByOwner.put(((Player) sender).getPlayer().getUniqueId(), newLoc);
										
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
							
							for(int i = 0; i == locs.size(); i++) {
								double x = locs.get(i).getX();
								double y = locs.get(i).getY();
								double z = locs.get(i).getZ();
								String w = locs.get(i).getWorld().toString();
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
			saveToConfig();
			return true;
		} return false;
	}
	
	@EventHandler
	public void onEntitySpawnEvent(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Monster) {
			Location loc = entity.getLocation();
			for(int i = 0; i < locs.size(); i++) {
				double torchLocX = locs.get(i).getX();
				double torchLocZ = locs.get(i).getZ();
				
				double mobLocX = loc.getX();
				double mobLocZ = loc.getZ();
				
				double distance = Math.sqrt(Math.pow(torchLocX - mobLocX, 2) + Math.pow(torchLocZ - mobLocZ, 2));
				if(distance <= 48) {
					//TODO 
					entity.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getBlock().getType() == Material.TORCH) {
			ItemStack block = event.getItemInHand();
			if(block.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Haro's Torch")) {
				Location loc = event.getBlock().getLocation();
				locs.add(loc);
				locsByOwner.put(event.getPlayer().getUniqueId(), loc);
			}
			
			
		}
	}
	
    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }
	
	//Check if a custom config exists, if not, create one
    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "torches.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("torches.yml", false);
         }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void saveToConfig() {    	
    	
    	//Handles the locs List
    	List<String> locsString = new ArrayList<String>();
    	for(int i = 0; i == locs.size(); i++) {
    		Location loc = locs.get(i);
    		
    		int x = (int) loc.getX();
    		int y = (int) loc.getY();
    		int z = (int) loc.getZ();
    		String w = loc.getWorld().toString();
    		
    		StringJoiner joiner = new StringJoiner(",");
    		joiner.add(String.valueOf(x)).add(String.valueOf(y)).add(String.valueOf(z)).add(w);
    		String joinedString = joiner.toString();
    		
    		System.out.println(joinedString);
    		 
    		locsString.add(joinedString);
    	}
    	
    	System.out.print("locsString length: " + locsString.size());
    	
    	//Handles the HashMap
    	List<String> locsByOwnerString = new ArrayList<String>();
    	for(Map.Entry<UUID, Location> entry : locsByOwner.entrySet()) {
    		UUID key = entry.getKey();
    		Location value = entry.getValue();
    	    		
    		String uuid = key.toString();
    		int x = (int) value.getX();
    		int y = (int) value.getY();
    		int z = (int) value.getZ();
    		String w = value.getWorld().toString();
    		
    		StringJoiner joiner = new StringJoiner(",");
    		joiner.add(uuid).add(String.valueOf(x)).add(String.valueOf(y)).add(String.valueOf(z)).add(w);
    		String joinedString = joiner.toString();
    		System.out.println(joinedString);
    		
    		locsByOwnerString.add(joinedString);
    	}
    	
    	getCustomConfig().set("torchesByOwner", locsByOwnerString);
    	getCustomConfig().set("torches", locsString);
    	try {
			getCustomConfig().save(customConfigFile = new File(getDataFolder(), "torches.yml"));
			System.out.println("[HaroTorch] Config saved sucessfully!");
		} catch (IOException e) {
			System.err.println("[HaroTorch] Failed to save config!");
			e.printStackTrace();
		}
    }
} 
