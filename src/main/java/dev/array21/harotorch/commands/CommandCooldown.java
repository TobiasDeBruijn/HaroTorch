package dev.array21.harotorch.commands;

import dev.array21.harotorch.HaroTorch;
import dev.array21.harotorch.lang.LangHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CommandCooldown {
    public static boolean checkCommandCooldown(HaroTorch plugin, CommandSender sender, HashMap<UUID, Long> lastCommandTimestamps) {
        Integer commandCooldown = plugin.getConfigManifest().commandCooldown;
        if(commandCooldown != null && commandCooldown > 0) {
            Long lastCommandUseTimestamp = lastCommandTimestamps.get(((Player) sender).getUniqueId());
            if(lastCommandUseTimestamp != null) {
                if(lastCommandUseTimestamp >= System.currentTimeMillis()) {
                    sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("commandCooldown"));
                    return true;
                }
            }

            lastCommandTimestamps.put(((Player) sender).getUniqueId(), System.currentTimeMillis() + (commandCooldown * 1000));
        }
        return false;
    }
}
