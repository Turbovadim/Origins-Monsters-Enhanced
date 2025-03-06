package com.starshootercity.originsmonsters.abilities.metamorphosis;

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

public class HuskTransformIntoZombie implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("You transform into a Zombie if you're in water for too long.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Metamorphosis", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("monsterorigins:husk_transform_into_zombie");
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
                        switchToZombie(player);
                    }
                }
            });
        }
    }

    private void switchToZombie(Player player) {
        player.getLocation().getWorld().playSound(player, Sound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, SoundCategory.PLAYERS, 1, 1);
        MetamorphosisTemperature.setTemperature(player, Math.min(70, MetamorphosisTemperature.getTemperature(player)));
        OriginSwapper.setOrigin(player, AddonLoader.getOrigin("zombie"), PlayerSwapOriginEvent.SwapReason.PLUGIN, false, "origin");
        player.sendMessage(Component.text("You have transformed into a zombie!")
                .color(NamedTextColor.YELLOW));
    }
}
