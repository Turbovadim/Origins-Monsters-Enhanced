package com.starshootercity.originsmonsters.abilities;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.OriginSwapper;
import com.starshootercity.SavedPotionEffect;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwimSpeed implements Listener, VisibleAbility {
    Map<Player, SavedPotionEffect> storedEffects = new HashMap<>();

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            AbilityRegister.runForAbility(player, getKey(), () -> {
                if (player.isUnderWater()) {
                    PotionEffect effect = player.getPotionEffect(PotionEffectType.DOLPHINS_GRACE);
                    boolean ambient = false;
                    boolean showParticles = false;
                    if (effect != null) {
                        ambient = effect.isAmbient();
                        showParticles = effect.hasParticles();
                        if (effect.getAmplifier() != -1) {
                            storedEffects.put(player, new SavedPotionEffect(effect, Bukkit.getCurrentTick()));
                            player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                        }
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, PotionEffect.INFINITE_DURATION, -1, ambient, showParticles));
                } else {
                    if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                        PotionEffect effect = player.getPotionEffect(PotionEffectType.DOLPHINS_GRACE);
                        if (effect != null) {
                            if (effect.getAmplifier() == -1) player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                        }
                    }
                    if (storedEffects.containsKey(player)) {
                        SavedPotionEffect effect = storedEffects.get(player);
                        storedEffects.remove(player);
                        PotionEffect potionEffect = effect.effect();
                        int time = potionEffect.getDuration() - (Bukkit.getCurrentTick() - effect.currentTime());
                        if (time > 0) {
                            player.addPotionEffect(new PotionEffect(
                                    potionEffect.getType(),
                                    time,
                                    potionEffect.getAmplifier(),
                                    potionEffect.isAmbient(),
                                    potionEffect.hasParticles()
                            ));
                        }
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.MILK_BUCKET) {
            storedEffects.remove(event.getPlayer());
        }
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("monsterorigins:swim_speed");
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Your underwater speed is increased.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Fast Swimmer", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }
}
