package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillAura extends Feature {

    public float range = 4.5f;
    public boolean onlyHostile = false;
    public boolean onlyPlayers = false;
    private int attackCooldown = 0;

    public KillAura() {
        super("KillAura", "Автоматически атакует ближайших существ", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (client.player.getAttackCooldownProgress(0) < 0.9f) return;

        Box box = client.player.getBoundingBox().expand(range);
        List<LivingEntity> targets = new ArrayList<>();

        for (Entity e : client.world.getEntitiesByClass(LivingEntity.class, box, en -> true)) {
            if (e == client.player) continue;
            if (e instanceof PlayerEntity && (onlyHostile)) continue;
            if (onlyHostile && !(e instanceof HostileEntity)) continue;
            if (onlyPlayers && !(e instanceof PlayerEntity)) continue;
            if (!e.isAlive()) continue;
            if (client.player.distanceTo(e) > range) continue;
            targets.add((LivingEntity) e);
        }

        targets.sort(Comparator.comparingDouble(e -> client.player.distanceTo(e)));

        if (!targets.isEmpty()) {
            LivingEntity target = targets.get(0);
            client.player.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES,
                    target.getPos().add(0, target.getHeight() / 2.0, 0));
            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        }
    }
}
