package me.zombieman.nightvisionplus.listeners;

import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.effects.PlayerEffects;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.UUID;

public class MilkDrinkListener implements Listener {
    private final NightVisionPlus plugin;

    public MilkDrinkListener(NightVisionPlus plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMilkDrink(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        UUID pUUID = p.getUniqueId();
        ItemStack consumedItem = event.getItem();

        if (consumedItem.getType() == Material.MILK_BUCKET) {
            File playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
            FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
            boolean wantsEnable = playerDataConfig.getBoolean("nightVision.player." + pUUID + ".nvp", false);

            if (!wantsEnable) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PlayerEffects playerEffects = new PlayerEffects();
                playerEffects.pEffect(p, true);
            }

        }
    }
}