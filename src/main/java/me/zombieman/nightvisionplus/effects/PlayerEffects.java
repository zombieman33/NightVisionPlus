package me.zombieman.nightvisionplus.effects;

import me.zombieman.nightvisionplus.NightVisionPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerEffects {

    public void pEffect(Player p, boolean b) {
        PotionEffectType effectType = PotionEffectType.NIGHT_VISION;
        int durationTicks = Integer.MAX_VALUE;
        int amplifier = 1;
        PotionEffect effect = new PotionEffect(effectType, durationTicks, amplifier, true, false);
        if (b) {
            if (!p.hasPotionEffect(effectType)) {
                p.addPotionEffect(effect, true);
            }
        } else {
            p.removePotionEffect(effectType);
        }
    }

}
