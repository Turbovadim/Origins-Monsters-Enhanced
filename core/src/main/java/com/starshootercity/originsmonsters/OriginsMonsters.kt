package com.starshootercity.originsmonsters;

import com.starshootercity.OriginsAddon;
import com.starshootercity.abilities.Ability;
import com.starshootercity.originsmonsters.abilities.*;
import com.starshootercity.originsmonsters.abilities.ZombieTouch;
import com.starshootercity.originsmonsters.abilities.metamorphosis.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OriginsMonsters extends OriginsAddon {
    private static MonstersNMSInvoker nmsInvoker;

    private static void initializeNMSInvoker() {
        nmsInvoker = switch (Bukkit.getMinecraftVersion()) {
            case "1.19" -> new MonstersNMSInvokerV1_19();
            case "1.19.1" -> new MonstersNMSInvokerV1_19_1();
            case "1.19.2" -> new MonstersNMSInvokerV1_19_2();
            case "1.19.3" -> new MonstersNMSInvokerV1_19_3();
            case "1.19.4" -> new MonstersNMSInvokerV1_19_4();
            case "1.20" -> new MonstersNMSInvokerV1_20();
            case "1.20.1" -> new MonstersNMSInvokerV1_20_1();
            case "1.20.2" -> new MonstersNMSInvokerV1_20_2();
            case "1.20.3" -> new MonstersNMSInvokerV1_20_3();
            case "1.20.4" -> new MonstersNMSInvokerV1_20_4();
            case "1.20.5", "1.20.6" -> new MonstersNMSInvokerV1_20_6();
            case "1.21" -> new MonstersNMSInvokerV1_21();
            case "1.21.1" -> new MonstersNMSInvokerV1_21_1();
            case "1.21.2", "1.21.3" -> new MonstersNMSInvokerV1_21_3();
            default -> throw new IllegalStateException("Unexpected version: " + Bukkit.getMinecraftVersion() + " only versions 1.20 - 1.20.6 are supported");
        };
    }

    public static MonstersNMSInvoker getNMSInvoker() {
        return nmsInvoker;
    }

    @Override
    public void onRegister() {
        saveDefaultConfig();
        initializeNMSInvoker();

        if (!getConfig().contains("creeper-explosion-breaks-blocks")) {
            getConfig().set("creeper-explosion-breaks-blocks", true);
            saveConfig();
        }
    }

    @Override
    public @NotNull String getNamespace() {
        return "monsterorigins";
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(
                new CreeperAlly(),
                new Explosive(),
                new FearCats(),
                new DrownedTransformIntoZombie(),
                new HuskTransformIntoZombie(),
                new TransformIntoHuskAndDrowned(),
                new TransformIntoStray(),
                new TransformIntoSkeleton(),
                MetamorphosisTemperature.INSTANCE,
                new Blindness(),
                new SenseMovement(),
                new DoubleHealth(),
                new DoubleDamage(),
                new SonicBoom(),
                new WaterVision(),
                new LandNightVision(),
                new DoubleFireDamage(),
                new BurnInDay(),
                new Undead(),
                new TridentExpert(),
                new ZombieHunger(),
                new WitherImmunity(),
                new HalfMaxSaturation(),
                new GuardianAlly(),
                new WaterCombatant(),
                new UndeadAlly(),
                new ApplyWitherEffect(),
                new InfiniteArrows(),
                new SlownessArrows(),
                new ApplyHungerEffect(),
                new SkeletonBody(),
                new Slowness(),
                new LandSlowness(),
                new WaterBreathing(),
                new SwimSpeed(),
                new FreezeImmune(),
                new HeatSlowness(),
                new BetterAim(),
                new ColdSlowness(),
                new ZombieTouch(),
                new ScareVillagers(),
                new TransformIntoZombifiedPiglin(),
                new TransformIntoPiglin(),
                new BetterGoldArmour(),
                new BetterGoldWeapons(),
                new ZombifiedPiglinAllies(),
                new SuperBartering(),
                new PiglinAlly()
        );
    }
}