package com.cosmosclient.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class AutoEat extends Feature {
    public int hungerThreshold = 15;

    public AutoEat() { super("AutoEat", "Автоматически ест еду при голоде", Category.PLAYER); }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        if (client.player.getHungerManager().getFoodLevel() > hungerThreshold) return;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.isFood()) {
                client.player.getInventory().selectedSlot = i;
                client.options.useKey.setPressed(true);
                return;
            }
        }
        client.options.useKey.setPressed(false);
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) mc.options.useKey.setPressed(false);
    }
}
