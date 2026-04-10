package com.cosmosclient.features;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public class AutoTool extends Feature {
    public AutoTool() { super("AutoTool", "Автоматически выбирает лучший инструмент", Category.PLAYER); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.crosshairTarget == null) return;
        if (!(client.crosshairTarget instanceof net.minecraft.util.hit.BlockHitResult bhr)) return;
        BlockState state = client.world.getBlockState(bhr.getBlockPos());
        float best = 0;
        int bestSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > best) { best = speed; bestSlot = i; }
        }
        if (bestSlot != -1) client.player.getInventory().selectedSlot = bestSlot;
    }
}
