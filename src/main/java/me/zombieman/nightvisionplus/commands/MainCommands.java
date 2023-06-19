package me.zombieman.nightvisionplus.commands;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import me.zombieman.nightvisionplus.utils.ColorUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainCommands implements CommandExecutor, TabCompleter {
    private final NightVisionPlus plugin;

    public MainCommands(NightVisionPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return true;
        }
        Player player = (Player) sender;
        UUID pUUID = player.getUniqueId();
        File playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
        FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        PlayerEffects pEffects = new PlayerEffects();

        if (player.hasPermission("nightvisionplus.command.use")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    long startTime = System.currentTimeMillis();
                    try {
                        plugin.reloadConfig();
                        savePlayerDataConfig(playerDataConfig, playerDataFile);
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;

                        player.sendMessage(ColorUtils.color("&aSuccessfully reloaded the config files in (" + elapsedTime + "ms)"));

                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "An error occurred while reloading the plugin: " + e.getMessage());
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
//                    if (args[1].equalsIgnoreCase("config.yml")) {
//                        long startTime = System.currentTimeMillis();
//                        plugin.saveResource("config.yml", true);
//                        plugin.reloadConfig();
//                        long endTime = System.currentTimeMillis();
//                        long time = endTime - startTime + 1;
//                        player.sendMessage(ChatColor.GREEN + "You successfully reset " + args[1] + ChatColor.AQUA + " (" + time + "ms)");
                    if (args[1].equalsIgnoreCase("playerData.yml")) {
                        long startTime = System.currentTimeMillis();
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            UUID onlineUUID = onlinePlayers.getUniqueId();
                            boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + onlineUUID + ".nvp", false);
                            if (wantsEnable) {
                                pEffects.pEffect(onlinePlayers, false);
                            }
                        }
                        plugin.saveResource("playerData.yml", true);
                        long endTime = System.currentTimeMillis();
                        long time = endTime - startTime + 1;
                        player.sendMessage(ChatColor.GREEN + "You successfully reset " + args[1] + ChatColor.AQUA + " (" + time + "ms)");
                    } else if (args[1].equalsIgnoreCase("all")) {
                        long startTime = System.currentTimeMillis();
                        plugin.saveResource("config.yml", true);
                        plugin.reloadConfig();
                        plugin.saveResource("playerData.yml", true);
                        long endTime = System.currentTimeMillis();
                        long time = endTime - startTime + 1;
                        player.sendMessage(ChatColor.GREEN + "You successfully reset all configs " + ChatColor.AQUA + "(" + time + "ms)");
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "/nvp reset <all, config.yml, playerData.yml>");
                    }
                }
            } else {
                boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);
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
        return false;
    }
    private void savePlayerDataConfig(FileConfiguration config, File configFile) {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while saving the player data config.", e);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        Player player = (Player) sender;

        if (args.length == 1) {
            if (player.hasPermission("nightvisionplus.command.use")) {
                completions.add("reload");
                completions.add("reset");
            }
        } else if (args.length == 2) {
            if (player.hasPermission("nightvisionplus.command.use")) {
                if (args[0].equalsIgnoreCase("reset")) {
                    completions.add("playerData.yml");
//                    completions.add("config.yml");
                    completions.add("all");
                }
            }
        }
        return completions;
    }
}
