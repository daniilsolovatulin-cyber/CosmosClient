package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Regeneration extends Feature {
    public int level = 5;
    public Regeneration() { super("Regeneration", "Быстрое восстановление здоровья", Category.PLAYER); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, level - 1, false, false));
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.REGENERATION);
    }
}
