package com.cosmosclient.gui;

import com.cosmosclient.CosmosClient;
import com.cosmosclient.features.Feature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;
import java.util.stream.Collectors;

public class CosmosHUD {

    public void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options.debugEnabled) return;

        List<Feature> active = CosmosClient.featureManager.getFeatures()
                .stream().filter(Feature::isEnabled).collect(Collectors.toList());

        int x = 2;
        int y = 2;

        // Watermark
        ctx.drawTextWithShadow(mc.textRenderer, "§b§l☄ §3§lCosmos §b§lClient", x, y, 0xFFFFFF);
        y += 12;

        for (Feature f : active) {
            // gradient color based on index
            int color = getRainbowColor(System.currentTimeMillis(), active.indexOf(f) * 200L);
            ctx.drawTextWithShadow(mc.textRenderer, f.getName(), x, y, color);
            y += 10;
        }
    }

    private int getRainbowColor(long time, long offset) {
        float hue = ((time + offset) % 3000) / 3000f;
        int rgb = java.awt.Color.HSBtoRGB(hue, 0.8f, 1.0f);
        return rgb | 0xFF000000;
    }
}
