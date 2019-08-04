package nl.thedutchmc.haro_torch.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;

public class ParticleHandler {

	private Location loc;
	@SuppressWarnings("unused")
	private HaroTorch haroTorch; //SuppressWarning as it may be used in the future
	private Plugin plugin;
	
	public ParticleHandler(Location loc, HaroTorch haroTorch, Plugin plugin) {
		this.loc = loc;
		this.haroTorch = haroTorch;
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void spawnParticle() {		
		double x = loc.getX() + 0.5;
		double y = loc.getY() + 0.7;
		double z = loc.getZ() + 0.5;
		String world = loc.getWorld().getName();
			
		  Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
              @Override
              public void run() {
              
                 Bukkit.getWorld(world).spawnParticle(Particle.DRAGON_BREATH,x,y,z,1,0D,0D,0D,0.005);
              }
}, 100, 0);
		
	}
}
