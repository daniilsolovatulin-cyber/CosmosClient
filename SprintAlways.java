package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

public class SprintAlways extends Feature {
    public SprintAlways() { super("SprintAlways", "Постоянный спринт", Category.MOVEMENT); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        client.player.setSprinting(true);
    }
}
