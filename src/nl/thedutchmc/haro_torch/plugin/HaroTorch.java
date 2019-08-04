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
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
    	//getCustomConfig().set("torches", locsString);
    	try {
			getCustomConfig().save(customConfigFile = new File(getDataFolder(), "torches.yml"));
			System.out.println("[HaroTorch] Config saved sucessfully!");
		} catch (IOException e) {
			System.err.println("[HaroTorch] Failed to save config!");
			e.printStackTrace();
		}
    }
    
    public void readTorches() {
    	System.out.println("[HaroTorch] Reading torches.yml...");
    	    	
    	//Populate locsWithOwner and locs
    	List<String> locsWithOwnerToSeparate = new ArrayList<String>();
    	locsWithOwnerToSeparate = (getCustomConfig().getStringList("torchesWithOwner"));
    	
    	System.out.println("locsWithOwnerToSeparate.size(): " + locsWithOwnerToSeparate.size());
    	if(locsWithOwnerToSeparate.size() != 0) {
        	for(int i = 0; i == locsWithOwnerToSeparate.size(); i++) {
        		String toSeperate = locsWithOwnerToSeparate.get(i);
        		String[] parts = toSeperate.split(",");
        		
        		UUID uuid = UUID.fromString(parts[0]);
        		int x = Integer.valueOf(parts[1]);
        		int y = Integer.valueOf(parts[2]);
        		int z = Integer.valueOf(parts[3]);
        		World w = Bukkit.getWorld(parts[4]);
        		
        		Location loc = new Location(w,x,y,z);

        		new ParticleHandler(loc,this,this).spawnParticle();
        		
        		locs.add(loc);
        		locsWithOwner.put(loc, uuid);
        	}
        	System.out.println("[HaroTorch] Reading torches.yml successful!");
    		setParticles();
    	} else {
    		System.out.println("[HaroTorch] torches.yml doesn't contain any entries. Skipping it");
    	}
    }
    
    public void setParticles() {
    	for(Map.Entry<Location, UUID> entry : HaroTorch.locsWithOwner.entrySet()) {
    		Location loc = entry.getKey();
			
			int x = (int) loc.getX();
			int y = (int) loc.getY();
			int z = (int) loc.getZ();
			World w = loc.getWorld();
			Location locForParticles = new Location(w,x,y,z);
			new ParticleHandler(locForParticles, this,plugin);
		} 
    }
} 