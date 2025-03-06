package com.starshootercity.originsmonsters

import com.starshootercity.OriginsAddon
import com.starshootercity.OriginsReborn
import com.starshootercity.abilities.Ability
import com.starshootercity.originsmonsters.abilities.*
import com.starshootercity.originsmonsters.abilities.metamorphosis.*
import org.bukkit.Bukkit
import java.util.List

class OriginsMonsters : OriginsAddon() {
    override fun onRegister() {
        saveDefaultConfig()
        instance = this
        initializeNMSInvoker()

        if (!config.contains("creeper-explosion-breaks-blocks")) {
            config.set("creeper-explosion-breaks-blocks", true)
            saveConfig()
        }
    }

    override fun getNamespace(): String {
        return "monsterorigins"
    }

    override fun getAbilities(): MutableList<Ability> {
        return mutableListOf(
            CreeperAlly(),
            Explosive(),
            FearCats(),
            DrownedTransformIntoZombie(),
            HuskTransformIntoZombie(),
            TransformIntoHuskAndDrowned(),
            TransformIntoStray(),
            TransformIntoSkeleton(),
            MetamorphosisTemperature.INSTANCE,
            Blindness(),
            SenseMovement(),
            DoubleHealth(),
            DoubleDamage(),
            SonicBoom(),
            WaterVision(),
            LandNightVision(),
            DoubleFireDamage(),
            BurnInDay(),
            Undead(),
            TridentExpert(),
            ZombieHunger(),
            WitherImmunity(),
            HalfMaxSaturation(),
            GuardianAlly(),
            WaterCombatant(),
            UndeadAlly(),
            ApplyWitherEffect(),
            InfiniteArrows(),
            SlownessArrows(),
            ApplyHungerEffect(),
            SkeletonBody(),
            Slowness(),
            LandSlowness(),
            WaterBreathing(),
            SwimSpeed(),
            FreezeImmune(),
            HeatSlowness(),
            BetterAim(),
            ColdSlowness(),
            ZombieTouch(),
            ScareVillagers(),
            TransformIntoZombifiedPiglin(),
            TransformIntoPiglin(),
            BetterGoldArmour(),
            BetterGoldWeapons(),
            ZombifiedPiglinAllies(),
            SuperBartering(),
            PiglinAlly()
        )
    }

    companion object {
        private fun initializeNMSInvoker() {
            nMSInvoker = when (Bukkit.getMinecraftVersion()) {
                "1.19" -> MonstersNMSInvokerV1_19()
                "1.19.1" -> MonstersNMSInvokerV1_19_1()
                "1.19.2" -> MonstersNMSInvokerV1_19_2()
                "1.19.3" -> MonstersNMSInvokerV1_19_3()
                "1.19.4" -> MonstersNMSInvokerV1_19_4()
                "1.20" -> MonstersNMSInvokerV1_20()
                "1.20.1" -> MonstersNMSInvokerV1_20_1()
                "1.20.2" -> MonstersNMSInvokerV1_20_2()
                "1.20.3" -> MonstersNMSInvokerV1_20_3()
                "1.20.4" -> MonstersNMSInvokerV1_20_4()
                "1.20.5", "1.20.6" -> MonstersNMSInvokerV1_20_6()
                "1.21" -> MonstersNMSInvokerV1_21()
                "1.21.1" -> MonstersNMSInvokerV1_21_1()
                "1.21.2", "1.21.3" -> MonstersNMSInvokerV1_21_3()
                "1.21.4" -> MonstersNMSInvokerV1_21_4()
                else -> throw IllegalStateException("Unexpected version: " + Bukkit.getMinecraftVersion() + " only versions 1.19 - 1.21.4 are supported")
            }
        }

        @JvmStatic
        lateinit var instance: OriginsMonsters
            private set

        @JvmStatic
        var nMSInvoker: MonstersNMSInvoker? = null
            private set
    }
}