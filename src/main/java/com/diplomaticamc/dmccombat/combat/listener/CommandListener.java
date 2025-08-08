package com.diplomaticamc.dmccombat.combat.listener;
import com.diplomaticamc.dmccombat.combat.CombatHandler;
import com.diplomaticamc.dmccombat.DMCCombat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    private List<String> blockedCommands;

    public CommandListener(DMCCombat plugin) {
        this.blockedCommands = plugin.getConfig().getStringList("blocked-commands")
                .stream()
                .map(String::toLowerCase)
                .toList();
    }

    public void loadBlockedCommands(DMCCombat plugin) {
        this.blockedCommands = plugin.getConfig().getStringList("blocked-commands")
                .stream()
                .map(String::toLowerCase)
                .toList();
    }

    @EventHandler
    public void onPreProcessCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        // Check if the player is in combat and doesn't have bypass permission
        if (CombatHandler.isTagged(player) && !player.hasPermission("dmccombat.combattag.bypass")) {
            String message = event.getMessage().substring(1).toLowerCase(); // Remove leading "/"

            // Check if the command is blacklisted
            for (String blacklistedCommand : blockedCommands) {
                if (message.equals(blacklistedCommand) || message.startsWith(blacklistedCommand + " ")) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You can't use that command while being in combat.");
                    return;
                }
            }
        }
    }
}
