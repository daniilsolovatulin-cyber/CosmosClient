package com.cosmosclient.mixin;

import com.cosmosclient.CosmosClient;
import com.cosmosclient.features.Feature;
import com.cosmosclient.features.NoSlowdown;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "getUseItemProgress", at = @At("HEAD"), cancellable = true)
    private void onSlowdown(CallbackInfoReturnable<Float> cir) {
        Feature f = CosmosClient.featureManager.getFeature("NoSlowdown");
        if (f != null && f.isEnabled()) {
            cir.setReturnValue(0f);
        }
    }
}
