package nl.thedutchmc.harotorch.torch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;

import nl.thedutchmc.harotorch.HaroTorch;

public class StorageHandler {

	private HaroTorch plugin;
	
	private String basePath;
	
	public StorageHandler(HaroTorch plugin) {
		this.plugin = plugin;
		
		basePath = plugin.getDataFolder() + File.separator + "Torches";
	}
	
	public List<Torch> read() {
		
		List<Torch> result = new ArrayList<>();
		
		for(String path : discover()) {
			
			try {
				
				Torch t = null;
				
				FileInputStream fis = new FileInputStream(path);
				ObjectInputStream in = new ObjectInputStream(fis);
				
				t = (Torch) in.readObject();
				
				in.close();
				fis.close();
				
				result.add(t);
				
			} catch (IOException | ClassNotFoundException e) {
				plugin.logWarn("An Exception was thrown whilst trying to read a Torch!");
				return null;
			}
		}
		
		return result;
		
	}
	
	public void write(Torch torch) {
		
		File outFile = new File(this.basePath + File.separator + getFileName(torch) + ".torch");
		
		if(!outFile.exists()) {
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				plugin.logWarn("There was an error creating the Torch (" + getFileName(torch) + ".torch" + "). An IOException was thrown!");
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(outFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			
			out.writeObject(torch);
			
			out.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			plugin.logWarn("There was an error creating the Torch (" + getFileName(torch) + ".torch" + "). A FileNotFoundException was thrown!");

		} catch (IOException e) {
			plugin.logWarn("There was an error creating the Torch (" + getFileName(torch) + ".torch" + "). An IOException was thrown!");

		}
	}
	
	public void remove(Torch t) {
		
		File removeFile = new File(basePath + File.separator + getFileName(t) + ".torch");		
		removeFile.delete();
	}
	
	private List<String> discover() {
		File storageFolder = new File(basePath);
		
		if(!storageFolder.exists()) {
			try {
				Files.createDirectories(Paths.get(storageFolder.getAbsolutePath()));
			} catch (IOException | SecurityException e) {
				plugin.logWarn("Failed to create Torch storage directory! Please check your file permissions!");
			
				return null;
			}
		}
		
		try {
			Stream<Path> walk = Files.walk(Paths.get(storageFolder.getAbsolutePath()));
			
			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".torch")).collect(Collectors.toList());
			
			walk.close();
			
			return result;
		} catch(IOException e) {
			plugin.logWarn("A IOException was thrown whilst discovering Torches!");
		
			e.printStackTrace();
			
			return null;
		}
	}
	
	private String getFileName(Torch t) {
		Location l = t.getLocation();
		return l.getX() + "=" + l.getY() + "=" + l.getZ() + "=" + l.getWorld() + "=" + t.getTorchOwner().toString();
	}
}
