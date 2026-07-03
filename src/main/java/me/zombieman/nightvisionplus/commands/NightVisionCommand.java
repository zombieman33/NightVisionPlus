package me.zombieman.nightvisionplus.commands;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.data.PlayerData;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import me.zombieman.nightvisionplus.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class NightVisionCommand implements CommandExecutor {
    private final NightVisionPlus plugin;

    public NightVisionCommand(NightVisionPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Console must specify a player: /" + label + " <player>");
                return true;
            }

            if (!player.hasPermission("nightvisionplus.command.apply")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return false;
            }

            FileConfiguration playerDataConfig = PlayerData.getPlayerDataConfig(plugin, player.getUniqueId());
            UUID pUUID = player.getUniqueId();
            boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);

            if (!wantsEnable) {
                applyOrRemove(player, true, playerDataConfig, pUUID, "enableMessage");
            } else {
                applyOrRemove(player, false, playerDataConfig, pUUID, "disableMessage");
            }
            return true;
        }

        if (!sender.hasPermission("nightvisionplus.command.apply.other")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to apply effects to other players.");
            if (sender instanceof Player player) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
            return false;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "'" + targetName + "'" + " isn't online.");
            if (sender instanceof Player player) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
            return false;
        }
        UUID tUUID = target.getUniqueId();
        FileConfiguration playerDataConfig = PlayerData.getPlayerDataConfig(plugin, tUUID);
        String senderName = sender instanceof Player ? sender.getName() : "Console";

        boolean TargetWantsEnable = playerDataConfig.getBoolean("nightVision.player." + tUUID + ".nvp", false);
        if (!TargetWantsEnable) {
            playerDataConfig.set("nightVision.player." + tUUID + ".nvp", true);
            playerDataConfig.set("nightVision.player." + tUUID + ".ign", targetName);
            PlayerData.savePlayerData(plugin, target);
            PlayerEffects.pEffect(plugin, target, true);
            sender.sendMessage(ColorUtils.color(plugin.getConfig().getString("enableMessageOthers")
                    .replace("%player%", senderName)
                    .replace("%target-player%", targetName)));
            if (target != sender) {
                target.sendMessage(ColorUtils.color(plugin.getConfig().getString("enableMessageToOther")
                        .replace("%player%", senderName)
                        .replace("%target-player%", targetName)));
            }
        } else {
            playerDataConfig.set("nightVision.player." + tUUID + ".nvp", false);
            playerDataConfig.set("nightVision.player." + tUUID + ".ign", targetName);
            PlayerData.savePlayerData(plugin, target);
            PlayerEffects.pEffect(plugin, target, false);
            sender.sendMessage(ColorUtils.color(plugin.getConfig().getString("disableMessageOthers")
                    .replace("%player%", senderName)
                    .replace("%target-player%", targetName)));
            if (target != sender) {
                target.sendMessage(ColorUtils.color(plugin.getConfig().getString("disableMessageToOther")
                        .replace("%player%", senderName)
                        .replace("%target-player%", targetName)));
            }
        }
        return true;
    }

    private void applyOrRemove(Player player, boolean b, FileConfiguration playerDataConfig, UUID pUUID, String disableMessage) {
        PlayerEffects.pEffect(plugin, player, b);
        playerDataConfig.set("nightVision.player." + pUUID + ".nvp", b);
        playerDataConfig.set("nightVision.player." + pUUID + ".ign", player.getName());
        PlayerData.savePlayerData(plugin, player);
        player.sendMessage(ColorUtils.color(plugin.getConfig().getString(disableMessage)));
    }
}

