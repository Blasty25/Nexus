package com.example.gemplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GemDeathListener implements Listener {
    private final GemPlugin plugin;
    private final Map<UUID, Integer> playerDeathCount = new HashMap<>();

    public GemDeathListener(GemPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) { // Player was killed by another player
            int deathCount = playerDeathCount.getOrDefault(player.getUniqueId(), 0) + 1;
            playerDeathCount.put(player.getUniqueId(), deathCount);

            if (deathCount >= 8) {
                player.kickPlayer(ChatColor.RED + "You have died to players 8 times. You are now banned from the server.");
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Banned for dying 8 times to other players.", null, null);
            }
        } else { // Player died from natural causes
            for (ItemStack item : event.getDrops()) {
                if (GemUtils.isSpecialGem(item)) {
                    event.getDrops().remove(item);
                    player.getInventory().addItem(item);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // Give the player their gem back if they died to another player
        // Implementation here would be needed to track which gem the player had and return it
    }
}
