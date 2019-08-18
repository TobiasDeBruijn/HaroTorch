package nl.thedutchmc.haro_torch.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class HaroTorch extends JavaPlugin implements Listener {
	
    public Recipes recipes = new Recipes(this);
    public EventHandlers eventHandlers = new EventHandlers(this, this);
    public CommandHandler commandHandler = new CommandHandler(this, this);
    Plugin plugin = this;
	
    static List<Location> locs = new ArrayList<Location>();
	static Map<Location, UUID> locsWithOwner = new HashMap<Location, UUID>();
	
	double mobBlockRadius = 48;
	double playerCommandCheckRadius = 64;
	
	private File customConfigFile;
    private FileConfiguration customConfig;
	
	@Override
	public void onEnable() {
		//Add Recipe for HaroTorch
		getServer().addRecipe(recipes.getHaroTorchRecipe());

		//getServer().getPluginManager().registerEvents(this, this);
		
		//Set command executor to the CommandHandler class
		getCommand("ht").setExecutor(commandHandler);
		getCommand("stc").setExecutor(commandHandler);
        
		//Set the EventRegister to the EventHandlers class
		getServer().getPluginManager().registerEvents(eventHandlers, this);
		
        
        createCustomConfig();
        
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {

            	for(int i = 0; i <= locs.size()-1; i++) {
            		if(locs.size() >= i && locs.size() != 0) {
                		Location loc = locs.get(i);
                		
                		World world = loc.getWorld();
                        double x = loc.getBlockX() + 0.5;
                        double y = loc.getBlockY() + 0.5;
                        double z = loc.getBlockZ() + 0.5;
                        if (loc.getChunk().isLoaded()) {
                            world.spawnParticle(Particle.DRAGON_BREATH, x, y, z, 1, 0D, 0D, 0D, 0.005);
                        }
            		}

            	}
            	/*
                for (Map.Entry<Location, UUID> entry : locsWithOwner.entrySet()) {
                    Location loc = entry.getKey();
                	                	
                    World world = loc.getWorld();
                    double x = loc.getBlockX() + 0.5;
                    double y = loc.getBlockY() + 0.5;
                    double z = loc.getBlockZ() + 0.5;
                    if (loc.getChunk().isLoaded()) {
                        world.spawnParticle(Particle.DRAGON_BREATH, x, y, z, 1, 0D, 0D, 0D, 0.005);
                    }
                }*/

            }
        }, 60L, 0);
	}
	
	@Override
	public void onDisable() {
		saveToConfig();
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

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        
        readTorches();
    }
    
    public void saveToConfig() {    	
    	//Handles the HashMap
    	List<String> locsWithOwnerString = new ArrayList<String>();    	
    	for(Map.Entry<Location, UUID> entry : locsWithOwner.entrySet()) {
    		
    		//Get the key and the value from the HashMap
    		UUID value = entry.getValue();
    		Location key = entry.getKey();
    	    
    		//get the uuid,x,y,z and w from locByOwner, as it is easier to store this way
    		String uuid = value.toString();
    		int x = (int) key.getX();
    		int y = (int) key.getY();
    		int z = (int) key.getZ();
    		String w = key.getWorld().getName();
    		
    		//Join all the variables together into one String, for storage
    		StringJoiner joiner = new StringJoiner(",");
    		joiner.add(uuid).add(String.valueOf(x)).add(String.valueOf(y)).add(String.valueOf(z)).add(w);
    		String joinedString = joiner.toString();
    		
    		//Add the joined string to the array, which will get stored at the end
    		locsWithOwnerString.add(joinedString);
    	}
    	getCustomConfig().set("torchesWithOwner", locsWithOwnerString);
    	try {
			getCustomConfig().save(customConfigFile = new File(getDataFolder(), "torches.yml"));
			//System.out.println("[HaroTorch] Config saved sucessfully!");
		} catch (IOException e) {
			//System.err.println("[HaroTorch] Failed to save config!");
			e.printStackTrace();
		}
    }
    
    public void readTorches() {
    	System.out.println("[HaroTorch] Reading torches.yml...");
    	    	
    	//Populate locsWithOwner and locs
    	List<String> locsWithOwnerToSeparate = new ArrayList<String>();
    	locsWithOwnerToSeparate = (getCustomConfig().getStringList("torchesWithOwner"));
    	
    	if(locsWithOwnerToSeparate.size() != 0) {    		
    		for(String toSeparate : locsWithOwnerToSeparate) {
        		String[] parts = toSeparate.split(",");
        	
        		UUID uuid = UUID.fromString(parts[0]);
        		int x = Integer.valueOf(parts[1]);
        		int y = Integer.valueOf(parts[2]);
        		int z = Integer.valueOf(parts[3]);
        		World w = Bukkit.getWorld(parts[4]);
        		
        		Location loc = new Location(w,x,y,z);
        		
        		locs.add(loc);
        		locsWithOwner.put(loc, uuid);
    		}
        	System.out.println("[HaroTorch] Reading torches.yml successful!");
    	} else {
    		System.out.println("[HaroTorch] torches.yml doesn't contain any entries. Skipping it");
    	}
    }
} 