package nl.thedutchmc.harotorch.commands.torchSubCmds;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;

public class AdminCommandExecutor {

	public static boolean admin(CommandSender sender) {
		
		UUID senderUuid = ((Player) sender).getUniqueId();
		
		if(HaroTorch.activeAdmins.contains(senderUuid)) {
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("disableAdmin"));
			HaroTorch.activeAdmins.remove(senderUuid);
		} else {
			sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("enableAdmin"));
			HaroTorch.activeAdmins.add(senderUuid);
		}
		
		return true;
	}
}
