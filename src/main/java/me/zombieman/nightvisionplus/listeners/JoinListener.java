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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.UUID;

public class JoinListener implements Listener {
    private final NightVisionPlus plugin;

    public JoinListener(NightVisionPlus plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        boolean hasPlayedBefore = p.hasPlayedBefore();
        if (hasPlayedBefore) {
            UUID pUUID = p.getUniqueId();
            FileConfiguration playerDataConfig = PlayerData.getPlayerDataConfig(plugin, pUUID);
            PlayerEffects pEffects = new PlayerEffects();
            boolean hasEnabled = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);
            if (hasEnabled) {
                pEffects.pEffect(p, true);
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> PlayerData.cleanupCache(event.getPlayer()));
    }

}
