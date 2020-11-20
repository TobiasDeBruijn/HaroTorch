package nl.thedutchmc.nmsdebugger;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("nmsdebugger")
public class NmsDebugger {
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

    public NmsDebugger() {
        System.out.println("Mod started");

        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
    	
    	if(event.getEntity() instanceof ClientPlayerEntity) {
    		ClientPlayerEntity cpe = (ClientPlayerEntity) event.getEntity();
    		cpe.connection.getNetworkManager().channel().pipeline().addBefore("packet_handler", "nmsDebugChannelHandler", new NmsChannelHandler());
    	}
    }
}
