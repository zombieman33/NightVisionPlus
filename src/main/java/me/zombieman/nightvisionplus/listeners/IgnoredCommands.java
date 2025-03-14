package me.zombieman.nightvisionplus.listeners;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.data.PlayerData;
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
        Player player = event.getPlayer();
        String command = event.getMessage();
        UUID uuid = player.getUniqueId();
        FileConfiguration playerDataConfig = PlayerData.getPlayerDataConfig(plugin, uuid);

        if (!player.hasPermission("nightvisionplus.command.apply")) return;

        if (!command.startsWith("/nv")) {
            for (Player oPlayer : Bukkit.getOnlinePlayers()) {
                boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + oPlayer.getUniqueId() + ".nvp", false);
                if (!wantsEnable) continue;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PlayerEffects.pEffect(oPlayer, true);
                    }
                }.runTaskLater(plugin, 5);
            }
        }
    }
}