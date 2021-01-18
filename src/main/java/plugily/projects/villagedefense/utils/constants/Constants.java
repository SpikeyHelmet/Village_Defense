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

package plugily.projects.villagedefense.utils.constants;

/**
 * Class that represents some hard coded constant values like file names
 */
public class Constants {

  public enum Files {
    ARENAS("arenas"), CONFIG("config"), KITS("kits"), LANGUAGE("language"),
    SPECIAL_ITEMS("special_items"), MYSQL("mysql"), REWARDS("rewards"), STATS("stats");

    private final String name;

    Files(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

}
