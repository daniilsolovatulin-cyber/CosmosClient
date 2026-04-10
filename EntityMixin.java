package com.cosmosclient.mixin;

import com.cosmosclient.CosmosClient;
import com.cosmosclient.features.Feature;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyReturnValue;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyReturnValue(method = "getVisibilityBoundingBox", at = @At("RETURN"))
    private Box expandHitbox(Box original) {
        Feature ka = CosmosClient.featureManager.getFeature("KillAura");
        if (ka != null && ka.isEnabled()) {
            return original.expand(0.2);
        }
        return original;
    }
}
