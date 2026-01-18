package me.zombieman.nightvisionplus.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.zombieman.nightvisionplus.NightVisionPlus;
import me.zombieman.nightvisionplus.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NightVisionPlaceholder extends PlaceholderExpansion {

    private final NightVisionPlus plugin;
    private HashMap<UUID, Boolean> nvp = new HashMap<>();
    public HashMap<UUID, Long> nvpUpdate = new HashMap<>();

    public NightVisionPlaceholder(NightVisionPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "nightvisionplus";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("nvp")) {
            return getNVP(player.getUniqueId());
        }

        return null;
    }

    private String getNVP(UUID uuid) {
        if (shouldUpdate(uuid)) nvp.put(uuid, PlayerData.getPlayerDataConfig(plugin, uuid).getBoolean("nightVision.player." + uuid + ".nvp"));

        nvp.putIfAbsent(uuid, false);

        return nvp.get(uuid) ? "on" : "off";
    }

    public boolean shouldUpdate(UUID uuid) {
        Long last = nvpUpdate.get(uuid);

        return last == null || System.currentTimeMillis() - last >= 60 * 1000;
    }

    public void updateNVP(UUID uuid, boolean update) {
        nvp.put(uuid, update);
    }
}