package me.zombieman.nightvisionplus.listeners;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IgnoredCommands implements Listener {
    private final NightVisionPlus plugin;
    public IgnoredCommands(NightVisionPlus plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        String command = event.getMessage();

        List<String> ignoredCommands = new ArrayList<>();

        String pName = p.getName();
        ignoredCommands.add("/heal");
        ignoredCommands.add("/heal " + pName);
        ignoredCommands.add("/effect clear");
        ignoredCommands.add("/effect clear " + pName);
        ignoredCommands.add("/effect clear " + pName + " minecraft:night_vision");
        ignoredCommands.add("/effect clear " + pName + " night_vision");
        ignoredCommands.add("/effect clear @p");

        if (ignoredCommands.contains(command)) {
            UUID pUUID = p.getUniqueId();
            File playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
            FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
            boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);
            if (wantsEnable) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PlayerEffects playerEffects = new PlayerEffects();
                        playerEffects.pEffect(p, true);
                    }
                }.runTaskLater(plugin, 1);
            }
        }
    }
}
