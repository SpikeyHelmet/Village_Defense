/*
 * Village Defense - Protect villagers from hordes of zombies
 * Copyright (C) 2021  Plugily Projects - maintained by 2Wild4You, Tigerpanzer_02 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package plugily.projects.villagedefense.commands.arguments.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaRegistry;
import plugily.projects.villagedefense.commands.arguments.ArgumentsRegistry;
import plugily.projects.villagedefense.commands.arguments.data.CommandArgument;
import plugily.projects.villagedefense.commands.arguments.data.LabelData;
import plugily.projects.villagedefense.commands.arguments.data.LabeledCommandArgument;
import plugily.projects.villagedefense.handlers.language.Messages;

/**
 * @author Plajer
 * <p>
 * Created at 24.11.2018
 */
public class TeleportArgument {

  public TeleportArgument(ArgumentsRegistry registry) {
    registry.mapArgument("villagedefenseadmin", new LabeledCommandArgument("tp", "villagedefense.admin.teleport", CommandArgument.ExecutorType.PLAYER,
        new LabelData("/vda tp &6<arena> <location type>", "/vda tp <arena> <location>",
            "&7Teleport you to provided arena location\n&7Valid locations:\n&7• LOBBY - lobby location\n&7• START - starting location\n"
                + "&7• END - ending location\n&6Permission: &7villagedefense.admin.teleport")) {
      @Override
      public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
          sender.sendMessage(registry.getPlugin().getChatManager().getPrefix() + registry.getPlugin().getChatManager().colorMessage(Messages.COMMANDS_TYPE_ARENA_NAME));
          return;
        }
        if (args.length == 2) {
          sender.sendMessage(registry.getPlugin().getChatManager().getPrefix() + ChatColor.RED + "Please type location type: END, START, LOBBY");
          return;
        }
        Player player = (Player) sender;
        try {
          LocationType.valueOf(args[2].toUpperCase());
        } catch (Exception e) {
          sender.sendMessage(registry.getPlugin().getChatManager().getPrefix() + registry.getPlugin().getChatManager().colorMessage(Messages.COMMANDS_TELEPORT_LOCATION_INVALID));
          return;
        }
        for (Arena arena : ArenaRegistry.getArenas()) {
          if (arena.getId().equalsIgnoreCase(args[1])) {
            teleport(player, arena, LocationType.valueOf(args[2].toUpperCase()));
          }
        }
      }
    });
  }

  private void teleport(Player player, Arena arena, LocationType locationType) {
    switch (locationType) {
      case LOBBY:
        if (arena.getLobbyLocation() == null) {
          player.sendMessage(ChatColor.RED + "Lobby location isn't set for this arena!");
          return;
        }
        player.teleport(arena.getLobbyLocation());
        player.sendMessage(ChatColor.GRAY + "Teleported to LOBBY location from arena " + arena.getId());
        break;
      case START:
        if (arena.getLobbyLocation() == null) {
          player.sendMessage(ChatColor.RED + "Start location isn't set for this arena!");
          return;
        }
        player.teleport(arena.getStartLocation());
        player.sendMessage(ChatColor.GRAY + "Teleported to START location from arena " + arena.getId());
        break;
      case END:
        if (arena.getLobbyLocation() == null) {
          player.sendMessage(ChatColor.RED + "End location isn't set for this arena!");
          return;
        }
        arena.teleportToEndLocation(player);
        player.sendMessage(ChatColor.GRAY + "Teleported to END location from arena " + arena.getId());
        break;
      default:
        break; //o.o
    }
  }

  public enum LocationType {
    LOBBY, END, START
  }

}
