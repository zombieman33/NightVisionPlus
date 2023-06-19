package me.zombieman.nightvisionplus.listeners;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;
import java.util.UUID;

public class DeathListener implements Listener {
    private final NightVisionPlus plugin;

    public DeathListener(NightVisionPlus plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        UUID pUUID = p.getUniqueId();
        File playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
        FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);
        if (!wantsEnable) {
            PlayerEffects playerEffects = new PlayerEffects();
            playerEffects.pEffect(p, true);
        }
    }
}
