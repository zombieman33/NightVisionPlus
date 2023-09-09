package me.zombieman.nightvisionplus.commands;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.data.PlayerData;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import me.zombieman.nightvisionplus.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        PlayerData pData = new PlayerData(plugin);
        PlayerEffects pEffects = new PlayerEffects();

        if (player.hasPermission("nightvisionplus.command.apply")) {
            File playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
            FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
            UUID pUUID = player.getUniqueId();
            boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);

            if (args.length >= 1) {
                String targetName = args[0];
                Player target = Bukkit.getPlayerExact(targetName);

                if (target == null) {
                    player.sendMessage(ChatColor.RED + "'" + targetName + "'" + " isn't online.");
                } else {
                    UUID tUUID = target.getUniqueId();

                    if (player.hasPermission("nightvisionplus.command.apply.other")) {
                        boolean TargetWantsEnable = playerDataConfig.getBoolean("nightVision.player." + tUUID + ".nvp", false);
                        if (!TargetWantsEnable) {
                            playerDataConfig.set("nightVision.player." + tUUID + ".nvp", true);
                            playerDataConfig.set("nightVision.player." + tUUID + ".ign", targetName);
                            savePlayerDataConfig(playerDataConfig, playerDataFile);
                            pEffects.pEffect(target, true);
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
                            savePlayerDataConfig(playerDataConfig, playerDataFile);
                            pEffects.pEffect(target, false);
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
                        player.sendMessage(ChatColor.RED + "You don't have permission to apply effects to other players.");
                    }
                }
            } else {
                if (!wantsEnable) {
                    pEffects.pEffect(player, true);
//                    pData.savePlayerData(player, true);
                    playerDataConfig.set("nightVision.player." + pUUID + ".nvp", true);
                    playerDataConfig.set("nightVision.player." + pUUID + ".ign", player.getName());
                    savePlayerDataConfig(playerDataConfig, playerDataFile);
                    player.sendMessage(ColorUtils.color(plugin.getConfig().getString("enableMessage")));
                } else {
                    pEffects.pEffect(player, false);
                    playerDataConfig.set("nightVision.player." + pUUID + ".nvp", false);
                    playerDataConfig.set("nightVision.player." + pUUID + ".ign", player.getName());
                    savePlayerDataConfig(playerDataConfig, playerDataFile);
                    player.sendMessage(ColorUtils.color(plugin.getConfig().getString("disableMessage")));
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
        }
        return true;
    }
    private void savePlayerDataConfig(FileConfiguration config, File configFile) {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while saving the player data config.", e);
        }
    }
}

