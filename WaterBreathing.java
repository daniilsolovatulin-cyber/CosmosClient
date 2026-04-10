package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class WaterBreathing extends Feature {
    public WaterBreathing() { super("WaterBreathing", "Дыхание под водой", Category.PLAYER); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 60, 0, false, false));
        client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 60, 0, false, false));
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.WATER_BREATHING);
            mc.player.removeStatusEffect(StatusEffects.DOLPHINS_GRACE);
        }
    }
}
