package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Speed extends Feature {
    public int level = 5;
    public Speed() { super("Speed", "Увеличивает скорость передвижения", Category.MOVEMENT); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        if (!client.player.hasStatusEffect(StatusEffects.SPEED) ||
                client.player.getStatusEffect(StatusEffects.SPEED).getAmplifier() != level - 1) {
            client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, level - 1, false, false));
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.SPEED);
    }
}
