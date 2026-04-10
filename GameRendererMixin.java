package com.cosmosclient.mixin;

import com.cosmosclient.CosmosClient;
import com.cosmosclient.features.Feature;
import com.cosmosclient.features.Zoom;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyReturnValue;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private double modifyFov(double original) {
        Feature f = CosmosClient.featureManager.getFeature("Zoom");
        if (f instanceof Zoom z && f.isEnabled()) {
            // Check if Z key is held
            net.minecraft.client.MinecraftClient mc = net.minecraft.client.MinecraftClient.getInstance();
            if (mc != null && mc.options != null) {
                long window = mc.getWindow().getHandle();
                if (org.lwjgl.glfw.GLFW.glfwGetKey(window, org.lwjgl.glfw.GLFW.GLFW_KEY_Z)
                        == org.lwjgl.glfw.GLFW.GLFW_PRESS) {
                    return original * z.fovMultiplier;
                }
            }
        }
        return original;
    }
}
