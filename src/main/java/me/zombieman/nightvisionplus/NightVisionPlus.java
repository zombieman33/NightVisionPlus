package me.zombieman.nightvisionplus;

import me.zombieman.nightvisionplus.commands.MainCommands;
import me.zombieman.nightvisionplus.commands.NightVisionCommand;
import me.zombieman.nightvisionplus.data.PlayerData;
import me.zombieman.nightvisionplus.data.PlayerManager;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import me.zombieman.nightvisionplus.listeners.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class NightVisionPlus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

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

        new PlayerData();
        new PlayerManager(this);
        new PlayerEffects();

        new JoinListener(this);
        new DeathListener(this);
        new MilkConsumeEvent(this);
        new IgnoredCommands(this);
        new PlayerListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
