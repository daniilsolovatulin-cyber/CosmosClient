package com.cosmosclient.mixin;

import com.cosmosclient.CosmosClient;
import com.cosmosclient.features.Feature;
import com.cosmosclient.features.Zoom;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        Feature f = CosmosClient.featureManager.getFeature("Zoom");
        if (!(f instanceof Zoom z) || !f.isEnabled()) return;
        long mcWindow = net.minecraft.client.MinecraftClient.getInstance().getWindow().getHandle();
        if (window != mcWindow) return;
        if (org.lwjgl.glfw.GLFW.glfwGetKey(window, org.lwjgl.glfw.GLFW.GLFW_KEY_Z)
                == org.lwjgl.glfw.GLFW.GLFW_PRESS) {
            z.fovMultiplier = Math.max(0.05f, Math.min(0.9f, z.fovMultiplier - (float)vertical * 0.05f));
            ci.cancel();
        }
    }
}
