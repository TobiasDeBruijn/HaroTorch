package nl.thedutchmc.haro_torch.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HaroTorch extends JavaPlugin implements Listener {
	
    public Recipes recipes = new Recipes(this);
    public EventHandlers eventHandlers = new EventHandlers(this, this);
    public CommandHandler commandHandler = new CommandHandler(this, this);
	
	List<Location> locs = new ArrayList<Location>();
	Map<UUID, Location> locsByOwner = new HashMap<UUID, Location>();
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
    }
    
    public void saveToConfig() {    	
    	
    	//Handles the locs List
    	List<String> locsString = new ArrayList<String>();
    	for(int i = 0; i == locs.size(); i++) {
    		Location loc = locs.get(i);
    		
    		//get the x,y,z and w from loc, as it is easier to store this way
    		int x = (int) loc.getX();
    		int y = (int) loc.getY();
    		int z = (int) loc.getZ();
    		String w = loc.getWorld().toString();
    		
    		//Join all the above variables together into one string, for storage
    		StringJoiner joiner = new StringJoiner(",");
    		joiner.add(String.valueOf(x)).add(String.valueOf(y)).add(String.valueOf(z)).add(w);
    		String joinedString = joiner.toString();
    		
    		//TODO Temp
    		System.out.println(joinedString);
    		 
    		//Add the joined string to the array, which will get stored at the end
    		locsString.add(joinedString);
    	}
    	
    	//TODO Temp
    	System.out.print("locsString length: " + locsString.size());
    	
    	//Handles the HashMap
    	List<String> locsByOwnerString = new ArrayList<String>();
    	
    	//TODO temp
    	System.out.println(locsByOwner.size());
    	
    	for(Map.Entry<UUID, Location> entry : locsByOwner.entrySet()) {
    		
    		//Get the key and the value from the HashMap
    		UUID key = entry.getKey();
    		Location value = entry.getValue();
    	    
    		//get the uuid,x,y,z and w from locByOwner, as it is easier to store this way
    		String uuid = key.toString();
    		int x = (int) value.getX();
    		int y = (int) value.getY();
    		int z = (int) value.getZ();
    		String w = value.getWorld().toString();
    		
    		//Join all the variables together into one String, for storage
    		StringJoiner joiner = new StringJoiner(",");
    		joiner.add(uuid).add(String.valueOf(x)).add(String.valueOf(y)).add(String.valueOf(z)).add(w);
    		String joinedString = joiner.toString();
    		System.out.println(joinedString);
    		
    		//Add the joined string to the array, which will get stored at the end
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