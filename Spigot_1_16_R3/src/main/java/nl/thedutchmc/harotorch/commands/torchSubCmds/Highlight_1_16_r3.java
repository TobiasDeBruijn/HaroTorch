package nl.thedutchmc.harotorch.commands.torchSubCmds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.EntityMagmaCube;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.World;

public class Highlight_1_16_r3 {

	public static List<Integer> spawnHighlight(Player p, List<Location> locations) {
		
		List<Integer> result = new ArrayList<>();
		
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PlayerConnection conn = ep.playerConnection;
		
		for(Location loc : locations) {
			
			World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
						
			EntityMagmaCube nmsEntity = new EntityMagmaCube(EntityTypes.MAGMA_CUBE, nmsWorld);
			
			nmsEntity.setFlag(6, true); //Glowing
			nmsEntity.setFlag(5, true); //Invisibility
			nmsEntity.setSize(2, true); //Set the size of the magma cube to be a full block
			nmsEntity.setLocation(loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D, 0f, 0f);
						
			PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving();
			int entityId = 0;
			UUID entityUuid = null;
			try {
				Random ran = new Random();
				entityId = ran.nextInt();
				entityUuid = UUID.randomUUID();
				
				Field entityIdField = spawnPacket.getClass().getDeclaredField("a");
				entityIdField.setAccessible(true);
				entityIdField.setInt(spawnPacket, entityId);
				entityIdField.setAccessible(false);
				
				Field entityUuidField = spawnPacket.getClass().getDeclaredField("b");
				entityUuidField.setAccessible(true);
				entityUuidField.set(spawnPacket, entityUuid);
				entityUuidField.setAccessible(false);
				
				Field entityType = spawnPacket.getClass().getDeclaredField("c");
				entityType.setAccessible(true);
				entityType.setInt(spawnPacket, 44);
				
				Field entityX = spawnPacket.getClass().getDeclaredField("d");
				entityX.setAccessible(true);
				entityX.setDouble(spawnPacket, loc.getBlockX() + 0.5D);
				entityX.setAccessible(false);
				
				Field entityY = spawnPacket.getClass().getDeclaredField("e");
				entityY.setAccessible(true);
				entityY.setDouble(spawnPacket, loc.getBlockY());
				entityY.setAccessible(false);
				
				Field entityZ = spawnPacket.getClass().getDeclaredField("f");
				entityZ.setAccessible(true);
				entityZ.setDouble(spawnPacket, loc.getBlockZ() + 0.5D);
				entityZ.setAccessible(false);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(entityId, nmsEntity.getDataWatcher(), true);
			
			conn.sendPacket(spawnPacket);
			conn.sendPacket(metaPacket);
			
			result.add(entityId);
		}
		
		return result;
	}
	
	public static void killHighlighted(List<Integer> ids, Player p) {
		
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PlayerConnection conn = ep.playerConnection;
		
		for(int id : ids) {
			PacketPlayOutEntityDestroy killPacket = new PacketPlayOutEntityDestroy(id);
			
			conn.sendPacket(killPacket);
		}
	}
}
