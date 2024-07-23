package me.zombieman.nightvisionplus.data;

import me.zombieman.nightvisionplus.NightVisionPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerManager {

    private NightVisionPlus plugin;
    public PlayerManager(NightVisionPlus plugin) {
        this.plugin = plugin;
    }

    public void savePlayerData(Player p, boolean b) {
        UUID pUUID = p.getUniqueId();
        String pName = p.getName();
        String path = "nightVision.players." + pUUID;
        FileConfiguration playerDataConfig = PlayerData.getPlayerDataConfig(plugin, pUUID);

        if (b) {
            playerDataConfig.set(path + ".ign", pName);
            playerDataConfig.set(path + ".nvp", true);
            PlayerData.savePlayerData(plugin, p);
        } else {
            playerDataConfig.set(path + ".ign", pName);
            playerDataConfig.set(path + ".nvp", false);
            PlayerData.savePlayerData(plugin, p);
        }
    }
}
