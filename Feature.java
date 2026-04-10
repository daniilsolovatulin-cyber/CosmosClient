package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

public abstract class Feature {

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    public Feature(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public void onTick(MinecraftClient client) {}
    public void onEnable() {}
    public void onDisable() {}

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable(); else onDisable();
    }

    public enum Category {
        COMBAT("⚔ Combat"),
        MOVEMENT("🚀 Movement"),
        WORLD("🌍 World"),
        RENDER("👁 Render"),
        PLAYER("🧑 Player"),
        MISC("✨ Misc");

        private final String displayName;
        Category(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }
}
