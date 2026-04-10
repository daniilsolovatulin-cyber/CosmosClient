package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityESP extends Feature {
    public boolean mobs = true;
    public boolean animals = false;
    public boolean players = true;

    public EntityESP() { super("EntityESP", "Подсвечивает сущностей через стены", Category.RENDER); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.world == null) return;
        for (Entity e : client.world.getEntities()) {
            if (!(e instanceof LivingEntity le)) continue;
            if (le == client.player) continue;
            boolean shouldGlow = false;
            if (players && le instanceof PlayerEntity) shouldGlow = true;
            if (mobs && le instanceof HostileEntity) shouldGlow = true;
            if (animals && le instanceof AnimalEntity) shouldGlow = true;
            le.setGlowing(shouldGlow);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return;
        for (Entity e : mc.world.getEntities()) {
            e.setGlowing(false);
        }
    }
}
