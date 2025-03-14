package me.zombieman.nightvisionplus.listeners;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.data.PlayerData;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private final NightVisionPlus plugin;

    public PlayerListener(NightVisionPlus plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTP(PlayerTeleportEvent event) {

        Player player = event.getPlayer();

        FileConfiguration playerDataConfig = PlayerData.getPlayerDataConfig(plugin, player.getUniqueId());
        boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + player.getUniqueId() + ".nvp", false);
        if (!wantsEnable) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerEffects.pEffect(player, true);
            }
        }.runTaskLater(plugin, 10);
    }

}
