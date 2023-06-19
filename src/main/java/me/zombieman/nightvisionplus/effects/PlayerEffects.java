package me.zombieman.nightvisionplus.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerEffects {

    public void pEffect(Player p, boolean b) {
        PotionEffectType effectType = PotionEffectType.NIGHT_VISION;
        int durationSeconds = 999999999;
        int amplifier = 1;
        PotionEffect effect = new PotionEffect(effectType, durationSeconds, amplifier);
        if (b) {
            p.addPotionEffect(effect, true);
        } else {
            p.removePotionEffect(effectType);
        }
    }
}
