package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;

public class NoSlowdown extends Feature {
    public NoSlowdown() { super("NoSlowdown", "Нет замедления при еде/луке/щите", Category.MOVEMENT); }
    // Implemented via ClientPlayerEntityMixin
}
