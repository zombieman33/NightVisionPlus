package me.zombieman.fewernightvision.listeners;

import me.zombieman.fewernightvision.NightVisionPlus;
import me.zombieman.fewernightvision.effects.PlayerEffects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
            File playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
            FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
            PlayerEffects pEffects = new PlayerEffects();
            boolean hasEnabled = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nv", false);
            if (hasEnabled) {
                pEffects.pEffect(p, true);
            }
        }
    }
}
