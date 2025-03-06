package com.starshootercity.originsmonsters.abilities.metamorphosis;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.AddonLoader;
import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.events.PlayerSwapOriginEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformIntoHuskAndDrowned implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("You transform into a Husk if you're in the desert for too long, and a Drowned if you're in the water for too long.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Metamorphosis", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("monsterorigins:transform_into_husk_and_drowned");
    }

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        if (event.getTickNumber() % 20 != 0) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            AbilityRegister.runForAbility(player, getKey(), () -> {
                if (MetamorphosisTemperature.getTemperature(player) >= 75) {
                    switchToHusk(player);
                }
            });
        }
    }

    private void switchToHusk(Player player) {
        player.getLocation().getWorld().playSound(player, Sound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, SoundCategory.PLAYERS, 1, 1);
        OriginSwapper.setOrigin(player, AddonLoader.getOrigin("husk"), PlayerSwapOriginEvent.SwapReason.PLUGIN, false, "origin");
        player.sendMessage(Component.text("You have transformed into a husk!")
                .color(NamedTextColor.YELLOW));
    }

    private final Map<Player, Integer> lastOutOfAirTime = new HashMap<>();

    @EventHandler
    public void onEntityAirChange(EntityAirChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            AbilityRegister.runForAbility(player, getKey(), () -> {
                if (event.getAmount() > 0) {
                    lastOutOfAirTime.remove(player);
                } else {
                    event.setAmount(0);
                    lastOutOfAirTime.putIfAbsent(player, Bukkit.getCurrentTick());
                    if (Bukkit.getCurrentTick() - lastOutOfAirTime.get(player) >= 300) {
                        switchToDrowned(player);
                    }
                }
            });
        }
    }

    private void switchToDrowned(Player player) {
        player.getLocation().getWorld().playSound(player, Sound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, SoundCategory.PLAYERS, 1, 1);
        MetamorphosisTemperature.setTemperature(player, Math.min(20, MetamorphosisTemperature.getTemperature(player)));
        OriginSwapper.setOrigin(player, AddonLoader.getOrigin("drowned"), PlayerSwapOriginEvent.SwapReason.PLUGIN, false, "origin");
        player.sendMessage(Component.text("You have transformed into a drowned!")
                .color(NamedTextColor.YELLOW));
    }
}
