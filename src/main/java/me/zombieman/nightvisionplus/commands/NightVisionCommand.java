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
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't run this command from the console");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("nightvisionplus.command.apply")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }

        FileConfiguration playerDataConfig = PlayerData.getPlayerDataConfig(plugin, player.getUniqueId());
        UUID pUUID = player.getUniqueId();
        boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);

        if (args.length >= 1) {
            if (!player.hasPermission("nightvisionplus.command.apply.other")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to apply effects to other players.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return false;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "'" + targetName + "'" + " isn't online.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return false;
            }
            UUID tUUID = target.getUniqueId();

            boolean TargetWantsEnable = PlayerData.getPlayerDataConfig(plugin, player.getUniqueId()).getBoolean("nightVision.player." + tUUID + ".nvp", false);
            if (!TargetWantsEnable) {
                playerDataConfig.set("nightVision.player." + tUUID + ".nvp", true);
                playerDataConfig.set("nightVision.player." + tUUID + ".ign", targetName);
                PlayerData.savePlayerData(plugin, target);
                PlayerEffects.pEffect(target, true);
                player.sendMessage(ColorUtils.color(plugin.getConfig().getString("enableMessageOthers")
                        .replace("%player%", player.getName())
                        .replace("%target-player%", targetName)));
                if (target != player) {
                    target.sendMessage(ColorUtils.color(plugin.getConfig().getString("enableMessageToOther")
                            .replace("%player%", player.getName())
                            .replace("%target-player%", targetName)));
                }
            } else {
                playerDataConfig.set("nightVision.player." + tUUID + ".nvp", false);
                playerDataConfig.set("nightVision.player." + tUUID + ".ign", targetName);
                PlayerData.savePlayerData(plugin, target);
                PlayerEffects.pEffect(target, false);
                player.sendMessage(ColorUtils.color(plugin.getConfig().getString("disableMessageOthers")
                        .replace("%player%", player.getName())
                        .replace("%target-player%", targetName)));
                if (target != player) {
                    target.sendMessage(ColorUtils.color(plugin.getConfig().getString("disableMessageToOther")
                            .replace("%player%", player.getName())
                            .replace("%target-player%", targetName)));
                }
            }

        } else {
            if (!wantsEnable) {
                applyOrRemove(player, true, playerDataConfig, pUUID, "enableMessage");
            } else {
                applyOrRemove(player, false, playerDataConfig, pUUID, "disableMessage");
            }
        }
        return true;
    }

    private void applyOrRemove(Player player, boolean b, FileConfiguration playerDataConfig, UUID pUUID, String disableMessage) {
        PlayerEffects.pEffect(player, b);
        playerDataConfig.set("nightVision.player." + pUUID + ".nvp", b);
        playerDataConfig.set("nightVision.player." + pUUID + ".ign", player.getName());
        PlayerData.savePlayerData(plugin, player);
        player.sendMessage(ColorUtils.color(plugin.getConfig().getString(disableMessage)));
    }
}

