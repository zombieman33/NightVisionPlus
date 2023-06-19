package me.zombieman.fewernightvision;

import me.zombieman.fewernightvision.commands.MainCommands;
import me.zombieman.fewernightvision.commands.NightVisionCommand;
import me.zombieman.fewernightvision.data.PlayerData;
import me.zombieman.fewernightvision.effects.PlayerEffects;
import me.zombieman.fewernightvision.listeners.JoinListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class NightVisionPlus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        File playerDataFile = new File(getDataFolder(), "playerData.yml");
        if (!playerDataFile.exists()) {
            getLogger().info("Player Data file not found, creating...");
            saveResource("playerData.yml", false);
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Config file not found, creating...");
            saveResource("config.yml", false);
        }

        // Commands
        NightVisionCommand nightVisionCommand = new NightVisionCommand(this);
        PluginCommand nightVisionCmd = getCommand("night-vision");
        if (nightVisionCmd != null) nightVisionCmd.setExecutor(nightVisionCommand);

        MainCommands mainCommands = new MainCommands(this);
        PluginCommand mainCmd = getCommand("nightvisionplus");
        if (mainCmd != null) mainCmd.setExecutor(mainCommands);

        new PlayerData(this);
        new PlayerEffects();
        new JoinListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
