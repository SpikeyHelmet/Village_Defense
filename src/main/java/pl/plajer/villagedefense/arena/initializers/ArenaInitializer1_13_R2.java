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

package pl.plajer.villagedefense.arena.initializers;

import java.util.Random;

import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import pl.plajer.villagedefense.Main;
import pl.plajer.villagedefense.arena.Arena;
import pl.plajer.villagedefense.arena.options.ArenaOption;
import pl.plajer.villagedefense.creatures.CreatureUtils;
import pl.plajer.villagedefense.creatures.v1_13_R2.BabyZombie;
import pl.plajer.villagedefense.creatures.v1_13_R2.FastZombie;
import pl.plajer.villagedefense.creatures.v1_13_R2.GolemBuster;
import pl.plajer.villagedefense.creatures.v1_13_R2.HardZombie;
import pl.plajer.villagedefense.creatures.v1_13_R2.PlayerBuster;
import pl.plajer.villagedefense.creatures.v1_13_R2.RidableIronGolem;
import pl.plajer.villagedefense.creatures.v1_13_R2.RidableVillager;
import pl.plajer.villagedefense.creatures.v1_13_R2.TankerZombie;
import pl.plajer.villagedefense.creatures.v1_13_R2.VillagerSlayer;
import pl.plajer.villagedefense.creatures.v1_13_R2.WorkingWolf;
import pl.plajer.villagedefense.handlers.language.Messages;
import pl.plajerlair.commonsbox.minecraft.compat.XMaterial;

/**
 * @author Plajer
 * <p>
 * Created at 16.07.2018
 */
public class ArenaInitializer1_13_R2 extends Arena {

  private World world;
  private Main plugin;

  public ArenaInitializer1_13_R2(String id, Main plugin) {
    super(id, plugin);
    this.plugin = plugin;
  }

  public void setWorld(Location loc) {
    this.world = ((CraftWorld) loc.getWorld()).getHandle();
  }

  public void spawnFastZombie(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    FastZombie fastZombie = new FastZombie(world);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyAttributes(zombie, this);
    plugin.getHolidayManager().applyHolidayZombieEffects(zombie);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  @Override
  public void spawnHalfInvisibleZombie(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    FastZombie fastZombie = new FastZombie(world);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.setRemoveWhenFarAway(false);
    zombie.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
    CreatureUtils.applyAttributes(zombie, this);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  @Override
  public void spawnKnockbackResistantZombies(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    TankerZombie fastZombie = new TankerZombie(world);
    fastZombie.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setItemInMainHand(XMaterial.GOLDEN_AXE.parseItem());
    zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyAttributes(zombie, this);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  public void spawnBabyZombie(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    BabyZombie fastZombie = new BabyZombie(world);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    CreatureUtils.applyAttributes(zombie, this);
    plugin.getHolidayManager().applyHolidayZombieEffects(zombie);
    zombie.setRemoveWhenFarAway(false);
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  public void spawnHardZombie(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    HardZombie fastZombie = new HardZombie(world);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyAttributes(zombie, this);
    this.addZombie(zombie);
    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  @Override
  public void spawnSoftHardZombie(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    HardZombie fastZombie = new HardZombie(world);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyAttributes(zombie, this);
    this.addZombie(zombie);
    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  public void spawnGolemBuster(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    GolemBuster fastZombie = new GolemBuster(world);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setHelmet(new ItemStack(Material.TNT));
    zombie.getEquipment().setHelmetDropChance(0.0F);
    zombie.getEquipment().setItemInMainHandDropChance(0F);
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyAttributes(zombie, this);
    this.addZombie(zombie);

    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  public void spawnPlayerBuster(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size()));
    PlayerBuster fastZombie = new PlayerBuster(world);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setHelmet(new ItemStack(Material.TNT));
    zombie.getEquipment().setHelmetDropChance(0.0F);
    zombie.getEquipment().setItemInMainHandDropChance(0F);
    zombie.getEquipment().setBoots(XMaterial.GOLDEN_BOOTS.parseItem());
    zombie.getEquipment().setLeggings(XMaterial.GOLDEN_LEGGINGS.parseItem());
    zombie.getEquipment().setChestplate(XMaterial.GOLDEN_CHESTPLATE.parseItem());
    CreatureUtils.applyAttributes(zombie, this);
    this.addZombie(zombie);

    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  public void spawnVillagerSlayer(Random random) {
    Location location = getZombieSpawns().get(random.nextInt(getZombieSpawns().size() - 1));
    VillagerSlayer villagerSlayer = new VillagerSlayer(world);
    villagerSlayer.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(villagerSlayer, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) villagerSlayer.getBukkitEntity();
    zombie.getEquipment().setItemInMainHand(new ItemStack(Material.EMERALD));
    zombie.getEquipment().setItemInMainHandDropChance(0F);
    zombie.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
    CreatureUtils.applyAttributes(zombie, this);
    this.addZombie(zombie);

    super.setOptionValue(ArenaOption.ZOMBIES_TO_SPAWN, getOption(ArenaOption.ZOMBIES_TO_SPAWN) - 1);
  }

  public void spawnVillager(Location location) {
    RidableVillager ridableVillager = new RidableVillager(location.getWorld());
    ridableVillager.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(ridableVillager, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Villager villager = (Villager) ridableVillager.getBukkitEntity();
    villager.setRemoveWhenFarAway(false);
    this.addVillager((Villager) ridableVillager.getBukkitEntity());
  }

  public void spawnGolem(Location location, Player player) {
    RidableIronGolem ironGolem = new RidableIronGolem(location.getWorld());
    ironGolem.setPosition(location.getX(), location.getY(), location.getZ());
    ironGolem.getBukkitEntity().setCustomName(plugin.getChatManager().colorMessage(Messages.SPAWNED_GOLEM_NAME).replace("%player%", player.getName()));
    ironGolem.setCustomNameVisible(true);
    world.addEntity(ironGolem, CreatureSpawnEvent.SpawnReason.CUSTOM);

    this.addIronGolem((org.bukkit.entity.IronGolem) ironGolem.getBukkitEntity());
  }

  public void spawnWolf(Location location, Player player) {
    WorkingWolf wolf = new WorkingWolf(location.getWorld());
    wolf.setPosition(location.getX(), location.getY(), location.getZ());
    world.addEntity(wolf, CreatureSpawnEvent.SpawnReason.CUSTOM);
    wolf.getBukkitEntity().setCustomName(plugin.getChatManager().colorMessage(Messages.SPAWNED_WOLF_NAME).replace("%player%", player.getName()));
    wolf.setCustomNameVisible(true);
    wolf.setInvisible(false);
    ((Wolf) wolf.getBukkitEntity()).setOwner(player);

    this.addWolf((Wolf) wolf.getBukkitEntity());
  }

}
