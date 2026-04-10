package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

public class NoWeather extends Feature {
    public NoWeather() { super("NoWeather", "Убирает дождь и гром визуально", Category.WORLD); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.world == null) return;
        client.world.setRainGradient(0f);
        client.world.setThunderGradient(0f);
    }
}
