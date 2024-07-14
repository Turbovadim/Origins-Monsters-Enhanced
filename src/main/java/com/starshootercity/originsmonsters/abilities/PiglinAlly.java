package com.starshootercity.originsmonsters.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PiglinAlly implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Piglins don't attack you, unless you attack them first.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Piglin Ally", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("monsterorigins:piglin_ally");
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (event.getEntityType() == EntityType.PIGLIN || event.getEntityType() == EntityType.PIGLIN_BRUTE) {
            if (event.getTarget() instanceof Player player) {
                AbilityRegister.runForAbility(player, getKey(), () -> {
                    if (!attackedEntities.getOrDefault(player, new ArrayList<>()).contains(event.getEntity())) {
                        event.setCancelled(true);
                    }
                });
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player;
        if (event.getDamager() instanceof Player p) player = p;
        else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player p) player = p;
        else return;
        List<Entity> playerHitEntities = attackedEntities.getOrDefault(player, new ArrayList<>());
        playerHitEntities.add(event.getEntity());
        attackedEntities.put(player, playerHitEntities);
    }

    private final Map<Player, List<Entity>> attackedEntities = new HashMap<>();
}
