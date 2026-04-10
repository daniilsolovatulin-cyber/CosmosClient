package com.cosmosclient.mixin;

import com.cosmosclient.CosmosClient;
import com.cosmosclient.features.Feature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
    private void onKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || !self.equals(mc.player)) return;
        Feature f = CosmosClient.featureManager.getFeature("AntiKnockback");
        if (f != null && f.isEnabled()) {
            ci.cancel();
        }
    }
}
