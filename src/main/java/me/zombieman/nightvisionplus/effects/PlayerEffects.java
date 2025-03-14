package me.zombieman.nightvisionplus.effects;

import me.zombieman.nightvisionplus.NightVisionPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerEffects {

    private static NightVisionPlus plugin;

    public PlayerEffects(NightVisionPlus plugin) {
        PlayerEffects.plugin = plugin;
    }

    public static void pEffect(Player player, boolean b) {
        PotionEffectType effectType = PotionEffectType.NIGHT_VISION;
        int durationTicks = Integer.MAX_VALUE;
        int amplifier = 1;
        PotionEffect effect = new PotionEffect(effectType, durationTicks, amplifier, true, false);
        if (b) {
            if (!player.hasPotionEffect(effectType)) player.addPotionEffect(effect, true);
            return;
        }

        player.removePotionEffect(effectType);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.hasPotionEffect(effectType)) {
                System.out.println("The player: " + player.getName() + " still had night vision after disabling it. Force removing now...");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/effect clear " + player.getName() + " minecraft:night_vision");
            }
        }, 5L);
    }

}
