package nl.thedutchmc.harotorch.util;

import nl.thedutchmc.harotorch.HaroTorch;

public class NmsUtil {

	private static String NMS_PACKAGE;
	
	public NmsUtil() {
		NMS_PACKAGE = "net.minecraft.server." + HaroTorch.NMS_VERSION + ".";
	}
	
	public Class<?> getNmsClass(String className) {
		try {
			return Class.forName(NMS_PACKAGE + className);			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}	
	}
}