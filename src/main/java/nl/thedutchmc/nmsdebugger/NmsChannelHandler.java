package nl.thedutchmc.nmsdebugger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.network.play.server.SEntityMetadataPacket;
import net.minecraft.network.play.server.SSpawnMobPacket;

@SuppressWarnings("rawtypes")
public class NmsChannelHandler extends SimpleChannelInboundHandler<IPacket> {

	private static File logFile;
	private static FileWriter fw;
	
	public NmsChannelHandler() {
		logFile = new File("A:\\Files\\Coding\\java\\workspace\\forge-1.16.4-35.0.18-mdk", "Packets.log");
		
		try {
			if(!logFile.exists()) {
				logFile.createNewFile();
			}
			
			fw = new FileWriter(logFile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket packIn) throws Exception {
		if(packIn instanceof SSpawnMobPacket) {
			SSpawnMobPacket spawnPack = (SSpawnMobPacket) packIn;
			System.out.println("eID: " + spawnPack.getEntityID());
		}
		
		else if(packIn instanceof SEntityMetadataPacket) {
			SEntityMetadataPacket metaPack = (SEntityMetadataPacket) packIn;
			if(metaPack.getDataManagerEntries().size() >= 14) {
				DataEntry<?> o = metaPack.getDataManagerEntries().get(14);
				System.out.println(o.getKey().getId() + " : " + o.getValue());
			}
			
		}
		ctx.fireChannelRead(packIn);
	}
}
