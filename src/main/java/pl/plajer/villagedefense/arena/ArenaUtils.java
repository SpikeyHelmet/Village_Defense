/*
 * Village Defense - Protect villagers from hordes of zombies
 * Copyright (C) 2019  Plajer's Lair - maintained by Plajer and Tigerpanzer
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

package pl.plajer.villagedefense.arena;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import pl.plajer.villagedefense.Main;
import pl.plajer.villagedefense.api.StatsStorage;
import pl.plajer.villagedefense.arena.initializers.ArenaInitializer1_11_R1;
import pl.plajer.villagedefense.arena.initializers.ArenaInitializer1_12_R1;
import pl.plajer.villagedefense.arena.initializers.ArenaInitializer1_13_R1;
import pl.plajer.villagedefense.arena.initializers.ArenaInitializer1_13_R2;
import pl.plajer.villagedefense.handlers.PermissionsManager;
import pl.plajer.villagedefense.user.User;
import pl.plajerlair.core.services.exception.ReportedException;

/**
 * @author Plajer
 * <p>
 * Created at 13.03.2018
 */
public class ArenaUtils {

  private static Main plugin = JavaPlugin.getPlugin(Main.class);

  public static void hidePlayer(Player p, Arena arena) {
    for (Player player : arena.getPlayers()) {
      player.hidePlayer(p);
    }
  }

  public static void showPlayer(Player p, Arena arena) {
    for (Player player : arena.getPlayers()) {
      player.showPlayer(p);
    }
  }

  public static void hidePlayersOutsideTheGame(Player player, Arena arena) {
    for (Player players : plugin.getServer().getOnlinePlayers()) {
      if (arena.getPlayers().contains(players)) {
        continue;
      }
      player.hidePlayer(players);
      players.hidePlayer(player);
    }
  }

  public static void bringDeathPlayersBack(Arena arena) {
    try {
      for (Player player : arena.getPlayers()) {
        if (arena.getPlayersLeft().contains(player)) {
          return;
        }
        User user = plugin.getUserManager().getUser(player);
        user.setSpectator(false);

        arena.teleportToStartLocation(player);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.SPEED);
        arena.showPlayers();
        player.getInventory().clear();
        user.getKit().giveKitItems(player);
        player.sendMessage(plugin.getChatManager().colorMessage("In-Game.Back-In-Game"));
      }
    } catch (Exception e) {
      new ReportedException(plugin, e);
    }
  }

  @Deprecated //move somewhere else
  public static void updateLevelStat(Player player, Arena arena) {
    try {
      User user = plugin.getUserManager().getUser(player);
      if (Math.pow(50 * user.getStat(StatsStorage.StatisticType.LEVEL), 1.5) < user.getStat(StatsStorage.StatisticType.XP)) {
        user.addStat(StatsStorage.StatisticType.LEVEL, 1);
        player.sendMessage(plugin.getChatManager().getPrefix() + plugin.getChatManager().formatMessage(arena, plugin.getChatManager().colorMessage("In-Game.You-Leveled-Up"), user.getStat(StatsStorage.StatisticType.LEVEL)));
      }
    } catch (Exception e) {
      new ReportedException(plugin, e);
    }
  }

  public static Arena initializeArena(String id) {
    Arena arena;
    if (plugin.is1_11_R1()) {
      arena = new ArenaInitializer1_11_R1(id, plugin);
    } else if (plugin.is1_12_R1()) {
      arena = new ArenaInitializer1_12_R1(id, plugin);
    } else if (plugin.is1_13_R1()) {
      arena = new ArenaInitializer1_13_R1(id, plugin);
    } else {
      arena = new ArenaInitializer1_13_R2(id, plugin);
    }
    return arena;
  }

  public static void setWorld(Arena arena) {
    if (plugin.is1_11_R1()) {
      ((ArenaInitializer1_11_R1) arena).setWorld(arena.getStartLocation());
    } else if (plugin.is1_12_R1()) {
      ((ArenaInitializer1_12_R1) arena).setWorld(arena.getStartLocation());
    } else if (plugin.is1_13_R1()) {
      ((ArenaInitializer1_13_R1) arena).setWorld(arena.getStartLocation());
    } else {
      ((ArenaInitializer1_13_R2) arena).setWorld(arena.getStartLocation());
    }
  }

  @Deprecated //move somewhere else
  public static void addExperience(Player player, int i) {
    User user = plugin.getUserManager().getUser(player);
    user.addStat(StatsStorage.StatisticType.XP, i);
    if (player.hasPermission(PermissionsManager.getVip())) {
      user.addStat(StatsStorage.StatisticType.XP, (int) Math.ceil(i / 2));
    }
    if (player.hasPermission(PermissionsManager.getMvp())) {
      user.addStat(StatsStorage.StatisticType.XP, (int) Math.ceil(i / 2));
    }
    if (player.hasPermission(PermissionsManager.getElite())) {
      user.addStat(StatsStorage.StatisticType.XP, (int) Math.ceil(i / 2));
    }
    ArenaUtils.updateLevelStat(player, ArenaRegistry.getArena(player));
  }

  @Deprecated //move somewhere else
  public static void addStat(Player player, StatsStorage.StatisticType stat) {
    User user = plugin.getUserManager().getUser(player);
    user.addStat(stat, 1);
    ArenaUtils.updateLevelStat(player, ArenaRegistry.getArena(player));
  }

}