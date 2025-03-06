package com.starshootercity.originsmonsters;

import com.destroystokyo.paper.entity.ai.Goal;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class MonstersNMSInvokerV1_21_4 extends MonstersNMSInvoker {

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void dealExplosionDamage(Player player, int amount) {
        player.damage(amount, DamageSource.builder(DamageType.EXPLOSION).build());
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void dealSonicBoomDamage(LivingEntity entity, int amount, Player source) {
        entity.damage(amount, DamageSource.builder(DamageType.SONIC_BOOM).build());
    }

    @Override
    public @NotNull Enchantment getSmiteEnchantment() {
        return Enchantment.SMITE;
    }

    @Override
    public Goal<@NotNull Villager> getVillagerAfraidGoal(LivingEntity villager, Predicate<Player> hasAbility) {
        return new AvoidEntityGoal<>(
                (PathfinderMob) ((CraftEntity) villager).getHandle(),
                net.minecraft.world.entity.player.Player.class,
                6,
                0.5,
                0.8,
                livingEntity -> {
                    if (livingEntity.getBukkitEntity() instanceof Player player) {
                        return hasAbility.test(player);
                    }
                    return false;
                }
        ).asPaperVanillaGoal();
    }

    @Override
    public @Nullable Player getNearestVisiblePlayer(Piglin piglin) {
        Optional<net.minecraft.world.entity.player.Player> optional = ((CraftPiglin) piglin).getHandle().getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        return optional.map(player -> (Player) player.getBukkitEntity()).orElse(null);
    }

    @Override
    public void throwItem(Piglin piglin, ItemStack itemStack, Location pos) {
        BehaviorUtils.throwItem(((CraftLivingEntity) piglin).getHandle(), CraftItemStack.asNMSCopy(itemStack), new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public void launchArrow(Entity projectile, Entity entity, float roll, float force, float divergence) {
        ((AbstractProjectile) projectile).getHandle().shootFromRotation(((CraftEntity) entity).getHandle(), entity.getPitch(), entity.getYaw(), roll, force, divergence);
    }

    @Override
    public void damageItem(ItemStack item, int amount, Player player) {
        item.damage(amount, player);
    }

    @Override
    public void startAutoSpinAttack(Player player, int duration, float riptideAttackDamage, ItemStack item) {
        ((CraftPlayer) player).getHandle().startAutoSpinAttack(duration, riptideAttackDamage, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public void tridentMove(Player player) {
        ((CraftPlayer) player).getHandle().move(MoverType.SELF, new Vec3(0.0D, 1.1999999284744263D, 0.0D));
    }
}