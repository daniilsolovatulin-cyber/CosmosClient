package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

public class Flight extends Feature {
    public float speed = 0.1f;
    public Flight() { super("Flight", "Позволяет летать в выживании", Category.MOVEMENT); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        client.player.getAbilities().allowFlying = true;
        client.player.getAbilities().flying = true;
        client.player.getAbilities().flySpeed = speed;
        client.player.sendAbilitiesUpdate();
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        mc.player.getAbilities().allowFlying = false;
        mc.player.getAbilities().flying = false;
        mc.player.getAbilities().flySpeed = 0.05f;
        mc.player.sendAbilitiesUpdate();
    }
}
