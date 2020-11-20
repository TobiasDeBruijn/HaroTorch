package nl.thedutchmc.harotorch.commands.torchSubCmds;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;
import nl.thedutchmc.harotorch.torch.TorchHandler;
import nl.thedutchmc.harotorch.util.NmsUtil;

public class HighlightExecutor {
	
	public static boolean highlight(CommandSender sender, String[] args, HaroTorch plugin) {
		
		List<Location> nearbyTorches = TorchHandler.getTorchLocationsNearPlayer((Player) sender, HaroTorch.getConfigHandler().torchHighlightRange);
		Player p = (Player) sender;
		
		List<Integer> returnedIds;
		
		switch(HaroTorch.NMS_VERSION) {
		//case "v1_16_R2": returnedIds = Highlight_1_16_r2.spawnHighlight(p, nearbyTorches); break;
		//case "v1_16_R3": returnedIds = Highlight_1_16_r3.spawnHighlight(p, nearbyTorches); break;
		default:
			
			//TODO Restructure this
			
			returnedIds = highlight(p, nearbyTorches);
			//String msg = LangHandler.activeLang.getLangMessages().get("highlightVersionNotSupported").replaceAll("%NMS_VERSION%", HaroTorch.NMS_VERSION);
			//p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + msg);
			//return true;
			
		}
		
		String msg = LangHandler.activeLang.getLangMessages().get("startingHiglight").replaceAll("%SECONDS%", ChatColor.RED + String.valueOf(HaroTorch.getConfigHandler().torchHighlightTime) + ChatColor.GOLD);
		p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + msg);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {

				switch(HaroTorch.NMS_VERSION) {
				//case "v1_16_R2": Highlight_1_16_r2.killHighlighted(returnedIds, p); break;
				//case "v1_16_R3": Highlight_1_16_r3.killHighlighted(returnedIds, p); break;
				default:
					System.out.println("Not ending highlight, TBD");
					//TODO Reflection for this
				}
				
				p.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("endingHighlight"));

			}
		}.runTaskLater(plugin, HaroTorch.getConfigHandler().torchHighlightTime * 20);
		
		
		return true;
	}
	
	private static List<Integer> highlight(Player p, List<Location> nearbyTorches) {
		NmsUtil nmsUtil = new NmsUtil();
		List<Integer> result = new ArrayList<>();
		
		try {
			//CraftPlayer#getHandle()
			Method getHandlePlayer = p.getClass().getMethod("getHandle");
						
			//Create an nmsPlayer object
			Object nmsPlayer = getHandlePlayer.invoke(p);
			
			System.out.println(nmsPlayer);

			//PlayerConnection
			Field playerConnectionField = nmsPlayer.getClass().getField("playerConnection");
			Object playerConnection = playerConnectionField.get(nmsPlayer);

			System.out.println(playerConnection);
			
			//PacketPlayOutSpawnEntityLiving class
			Class<?> packetPlayOutSpawnEntityLivingClass = nmsUtil.getNmsClass("PacketPlayOutSpawnEntityLiving");
			Constructor<?> packetPlayOutSpawnEntityLivingConstructor = packetPlayOutSpawnEntityLivingClass.getConstructor();
			
			//Packet Interface
			//We know that PacketPlayOutSpawnEntityLiving only implements one Interface
			//So we can just take the first Interface it implements, since it's constant.
			Class<?> packetInterfaceClass = packetPlayOutSpawnEntityLivingClass.getInterfaces()[0];
			
			//PlayerConnection#sendPacket(Packet packet);
			Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", packetInterfaceClass);

			//Fields from class PacketPlayOutSpawnEntityLiving
			Field entityIdField = packetPlayOutSpawnEntityLivingClass.getDeclaredField("a");
			Field entityUuidField = packetPlayOutSpawnEntityLivingClass.getDeclaredField("b");
			Field entityType = packetPlayOutSpawnEntityLivingClass.getDeclaredField("c");
			Field entityX = packetPlayOutSpawnEntityLivingClass.getDeclaredField("d");
			Field entityY = packetPlayOutSpawnEntityLivingClass.getDeclaredField("e");
			Field entityZ = packetPlayOutSpawnEntityLivingClass.getDeclaredField("f");

			//Entity class, Entity#setFlag(int flagId, boolean flagValue), Entity#setLocation(double x, double y, double z, float xRot, float yRot), Entity#getId()
			Class<?> entityClass = nmsUtil.getNmsClass("Entity");
			Method setFlagMethod = entityClass.getDeclaredMethod("setFlag", int.class, boolean.class);
			Method setLocationMethod = entityClass.getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
			Method getIdMethod = entityClass.getDeclaredMethod("getId");
			
			//DataWatcher class, DataWatcher#getDataWatcher()
			Class<?> dataWatcherClass = nmsUtil.getNmsClass("DataWatcher");
			Method getDataWatcherMethod = entityClass.getDeclaredMethod("getDataWatcher");
			
			//PacketPlayOutEntityMetadata class
			Class<?> packetPlayOutEntityMetadataClass = nmsUtil.getNmsClass("PacketPlayOutEntityMetadata");
			Constructor<?> packetPlayOutEntityMetadataConstructor = packetPlayOutEntityMetadataClass.getConstructor(int.class, dataWatcherClass, boolean.class);

			//World class
			Class<?> worldClass = nmsUtil.getNmsClass("World");
			
			//EntityTypes class, MAGMA_CUBE field
			Class<?> entityTypesClass = nmsUtil.getNmsClass("EntityTypes");
			Field magmaCubeField = entityTypesClass.getField("MAGMA_CUBE");
			
			for(Location l : nearbyTorches) {				
				World w = l.getWorld();
				
				//CraftWorld#getHandle()
				Method getHandleWorld = w.getClass().getMethod("getHandle");
				
				//Create a nmsWorld object
				Object nmsWorld = getHandleWorld.invoke(w);

				System.out.println(nmsWorld);

				
				//EntityMagmaCube class
				Class<?> entityMagmaCubeClass = nmsUtil.getNmsClass("EntityMagmaCube");
				Constructor<?> entityMagmaCubeConstructor = entityMagmaCubeClass.getConstructor(entityTypesClass, worldClass);
				
				//Create an EntityMagmaCube object
				Object entityMagmaCube = entityMagmaCubeConstructor.newInstance(magmaCubeField.get(null), nmsWorld);
				
				System.out.println(entityMagmaCube);
				
				//Set metadata on the entity
				//Reference: https://wiki.vg/Entity_metadata#Entity_Metadata_Format
				setFlagMethod.invoke(entityMagmaCube, 6, true); //Glowing
				setFlagMethod.invoke(entityMagmaCube, 5, true); //Invisible
				setFlagMethod.invoke(entityMagmaCube, 2, true); //Set the Magma Cube size to '2', i.e. a full block
				setLocationMethod.invoke(entityMagmaCube, l.getBlockX() + 0.5d, l.getBlockY(), l.getBlockZ() + 0.5d, 0f, 0f); //Position and rotation
								
				//Create a PacketPlayOutSpawnEntityLiving object
				Object spawnPacket = packetPlayOutSpawnEntityLivingConstructor.newInstance();
				int entityId = 0;
				
				System.out.println(spawnPacket);
				
				//Get the ID of the above created EntiyMagmaCube
				entityId = (int) getIdMethod.invoke(entityMagmaCube);
				
				//Set entityId to the Id of the above created EntityMagmaCube
				entityIdField.setAccessible(true);
				entityIdField.setInt(spawnPacket, entityId);
				entityIdField.setAccessible(false);
				
				//Set entityUuid to a random UUID
				entityUuidField.setAccessible(true);
				entityUuidField.set(spawnPacket, UUID.randomUUID());
				entityUuidField.setAccessible(false);
				
				//Set the entity type to minecraft:magma cube
				//Reference: https://wiki.vg/Entity_metadata#Mobs
				entityType.setAccessible(true);
				entityType.setInt(spawnPacket, 44);
				entityType.setAccessible(false);
				
				//Set the entity's X position
				entityX.setAccessible(true);
				entityX.setDouble(spawnPacket, l.getBlockX() + 0.5d);
				entityX.setAccessible(false);
				
				//Set the entity's Y position
				entityY.setAccessible(true);
				entityY.setDouble(spawnPacket, l.getBlockY());
				entityY.setAccessible(false);
				
				//Set the entity's Z position
				entityZ.setAccessible(true);
				entityZ.setDouble(spawnPacket, l.getBlockZ() + 0.5d);
				entityZ.setAccessible(false);
				
				//Create a PacketPlayOutEntityMetadata object
				Object dataWatcher = getDataWatcherMethod.invoke(nmsPlayer);
				
				System.out.println(dataWatcher);
				
				Object metaPacket = packetPlayOutEntityMetadataConstructor.newInstance(entityId, dataWatcher, true);

				System.out.println(metaPacket);

				//Send the PacketPlayOutSpawnEntityLiving and PacketPlayOutEntityMetadata packets to the client
				sendPacket.invoke(playerConnection, spawnPacket);
				sendPacket.invoke(playerConnection, metaPacket);
				
				//Add the ID to the list of Integers we will return
				result.add(entityId);
			}
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
