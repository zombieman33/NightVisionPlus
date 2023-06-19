package me.zombieman.nightvisionplus.data;

import me.zombieman.nightvisionplus.NightVisionPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {
    private final NightVisionPlus plugin;

    public PlayerData(NightVisionPlus plugin) {
        this.plugin = plugin;
    }

    public void savePlayerData(Player p, boolean b) {
        File playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
        FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        UUID pUUID = p.getUniqueId();
        String pName = p.getName();
        String path = "nightVision.players." + pUUID;

        if (b) {
            playerDataConfig.set(path + ".ign", pName);
            playerDataConfig.set(path + ".nvp", true);
            saveDataConfig(playerDataConfig, playerDataFile);
        } else {
            playerDataConfig.set(path + ".ign", pName);
            playerDataConfig.set(path + ".nvp", false);
            saveDataConfig(playerDataConfig, playerDataFile);
        }
    }

    private void saveDataConfig(FileConfiguration config, File configFile) {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while saving the player data config.", e);
        }
    }
}
