package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

public class Zoom extends Feature {
    public float fovMultiplier = 0.25f;
    public Zoom() { super("Zoom", "Приближение как у OptiFine (удерживай Z)", Category.MISC); }
    // Actual FOV change handled via GameRendererMixin
}
