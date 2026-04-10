package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

public class NoFall extends Feature {
    public NoFall() { super("NoFall", "Отключает урон от падения", Category.MOVEMENT); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        if (client.player.fallDistance > 3.0f) {
            client.player.fallDistance = 0;
        }
    }
}
