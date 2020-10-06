package nl.thedutchmc.harotorch.commands.torchSubCmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R2.EntityMagmaCube;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R2.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R2.PlayerConnection;
import net.minecraft.server.v1_16_R2.World;

public class Highlight_1_16_r2 {

	public static List<Integer> spawnHighlight(Player p, List<Location> locations) {
		
		List<Integer> result = new ArrayList<>();
		
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PlayerConnection conn = ep.playerConnection;
		
		for(Location loc : locations) {
			
			World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
						
			EntityMagmaCube nmsEntity = new EntityMagmaCube(EntityTypes.MAGMA_CUBE, nmsWorld);
					
			nmsEntity.setInvisible(false);
			nmsEntity.collides = false; //Colllision
			nmsEntity.setFlag(6, true); //Glowing
			nmsEntity.setFlag(5, true); //Invisibility
			nmsEntity.setSize(2, true); //Set the size of the magma cube to be a full block
			nmsEntity.setLocation(loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D, 0f, 0f);
			
			PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(nmsEntity);
			PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(nmsEntity.getId(), nmsEntity.getDataWatcher(), true);

			conn.sendPacket(spawnPacket);
			conn.sendPacket(metaPacket);
			
			result.add(nmsEntity.getId());
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
