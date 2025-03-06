package com.starshootercity.originsmonsters.abilities.metamorphosis;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.AddonLoader;
import com.starshootercity.OriginSwapper;
import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.events.PlayerSwapOriginEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformIntoZombifiedPiglin implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("You transform into a Zombified Piglin if you're out of the Nether for too long.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Metamorphosis", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("monsterorigins:transform_into_zombified_piglin");
    }

    private final Map<Player, Integer> overworldTime = new HashMap<>();
    private final World nether;

    public TransformIntoZombifiedPiglin() {
        nether = Bukkit.getWorld(OriginsReborn.getInstance().getConfig().getString("worlds.world_nether", "world_nether"));
    }

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        if (event.getTickNumber() % 20 != 0) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            AbilityRegister.runForAbility(player, getKey(), () -> {
                if (player.getWorld() == nether) {
                    overworldTime.put(player, 0);
                } else {
                    overworldTime.put(player, overworldTime.getOrDefault(player, 0) + 1);
                }
                if (overworldTime.getOrDefault(player, 0) >= 15) {
                    switchToZombifiedPiglin(player);
                }
            });
        }
    }

    private void switchToZombifiedPiglin(Player player) {
        overworldTime.put(player, 0);
        player.getLocation().getWorld().playSound(player, Sound.ENTITY_PIGLIN_CONVERTED_TO_ZOMBIFIED, SoundCategory.PLAYERS, 1, 1);
        OriginSwapper.setOrigin(player, AddonLoader.getOrigin("zombified piglin"), PlayerSwapOriginEvent.SwapReason.PLUGIN, false, "origin");
        player.sendMessage(Component.text("You have transformed into a Zombified Piglin!")
                .color(NamedTextColor.YELLOW));
    }
}
