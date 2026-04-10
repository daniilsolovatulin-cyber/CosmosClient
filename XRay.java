package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class XRay extends Feature {
    public XRay() { super("XRay", "Подсвечивает руду через стены (Glowing)", Category.WORLD); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        // Uses night vision to see underground better + relies on mixin for block highlight
        client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300, 0, false, false));
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
