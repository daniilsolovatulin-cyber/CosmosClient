package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {

    private final List<Feature> features = new ArrayList<>();

    public FeatureManager() {
        // Combat
        register(new KillAura());
        register(new Strength());
        register(new AntiKnockback());

        // Movement
        register(new Flight());
        register(new Speed());
        register(new NoFall());
        register(new HighJump());
        register(new SprintAlways());
        register(new NoSlowdown());

        // Player
        register(new Regeneration());
        register(new AntiHunger());
        register(new AutoEat());
        register(new WaterBreathing());
        register(new NoFire());
        register(new AutoTool());

        // World
        register(new FastMine());
        register(new NoWeather());
        register(new XRay());

        // Render
        register(new FullBright());
        register(new EntityESP());
        register(new Zoom());

        // Misc
    }

    private void register(Feature feature) {
        features.add(feature);
    }

    public void onTick(MinecraftClient client) {
        for (Feature f : features) {
            if (f.isEnabled()) f.onTick(client);
        }
    }

    public List<Feature> getFeatures() { return features; }

    public List<Feature> getByCategory(Feature.Category category) {
        List<Feature> result = new ArrayList<>();
        for (Feature f : features) {
            if (f.getCategory() == category) result.add(f);
        }
        return result;
    }

    public Feature getFeature(String name) {
        for (Feature f : features) {
            if (f.getName().equalsIgnoreCase(name)) return f;
        }
        return null;
    }
}
