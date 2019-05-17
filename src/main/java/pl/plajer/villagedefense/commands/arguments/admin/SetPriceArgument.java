/*
 * Village Defense - Protect villagers from hordes of zombies
 * Copyright (C) 2019  Plajer's Lair - maintained by Plajer and contributors
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

package pl.plajer.villagedefense.commands.arguments.admin;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.plajer.villagedefense.commands.arguments.ArgumentsRegistry;
import pl.plajer.villagedefense.commands.arguments.data.CommandArgument;
import pl.plajer.villagedefense.commands.arguments.data.LabelData;
import pl.plajer.villagedefense.commands.arguments.data.LabeledCommandArgument;
import pl.plajer.villagedefense.handlers.language.Messages;
import pl.plajerlair.commonsbox.minecraft.item.ItemBuilder;

/**
 * @author Plajer
 * <p>
 * Created at 24.11.2018
 */
public class SetPriceArgument {

  public SetPriceArgument(ArgumentsRegistry registry) {
    registry.mapArgument("villagedefenseadmin", new LabeledCommandArgument("setprice", "villagedefense.admin.setprice", CommandArgument.ExecutorType.PLAYER,
        new LabelData("/vda setprice &6<amount>", "/vda setprice <amount>",
            "&7Set price of holding item, it's required for game shop\n&6Permission: &7villagedefense.admin.setprice")) {
      @Override
      public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
          sender.sendMessage(registry.getPlugin().getChatManager().getPrefix() + ChatColor.RED + "Please type price of item!");
          return;
        }
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().equals(Material.AIR)) {
          player.sendMessage(registry.getPlugin().getChatManager().getPrefix() + registry.getPlugin().getChatManager().colorMessage(Messages.COMMANDS_HOLD_ANY_ITEM));
          return;
        }
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
          player.getInventory().setItemInMainHand(new ItemBuilder(item)
              .lore(ChatColor.GOLD + args[1] + " " + registry.getPlugin().getChatManager().colorMessage(Messages.SHOP_MESSAGES_CURRENCY_IN_SHOP)).build());
          player.sendMessage(registry.getPlugin().getChatManager().getPrefix() + registry.getPlugin().getChatManager().colorMessage(Messages.COMMANDS_COMMAND_EXECUTED));
        }
        //check any price from lore
        ItemMeta meta = item.getItemMeta();
        List<String> lore = item.getItemMeta().getLore();
        for (String search : lore) {
          if (search.contains(registry.getPlugin().getChatManager().colorMessage(Messages.SHOP_MESSAGES_CURRENCY_IN_SHOP))) {
            lore.remove(search);
            break;
          }
        }
        lore.add(0, ChatColor.GOLD + args[1] + " " + registry.getPlugin().getChatManager().colorMessage(Messages.SHOP_MESSAGES_CURRENCY_IN_SHOP));
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.sendMessage(registry.getPlugin().getChatManager().getPrefix() + registry.getPlugin().getChatManager().colorMessage(Messages.COMMANDS_COMMAND_EXECUTED_ITEM_UPDATED));
      }
    });
  }

}
