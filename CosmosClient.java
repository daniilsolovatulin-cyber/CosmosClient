package com.cosmosclient;

import com.cosmosclient.features.FeatureManager;
import com.cosmosclient.gui.CosmosHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CosmosClient implements ClientModInitializer {

    public static final String MOD_ID = "cosmosclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static FeatureManager featureManager;
    public static CosmosHUD hud;

    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("☄ Cosmos Client initializing...");

        featureManager = new FeatureManager();
        hud = new CosmosHUD();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cosmosclient.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.cosmosclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new com.cosmosclient.gui.CosmosScreen());
                }
            }
            if (client.player != null && client.world != null) {
                featureManager.onTick(client);
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
            if (net.minecraft.client.MinecraftClient.getInstance().player != null) {
                hud.render(drawContext);
            }
        });

        LOGGER.info("☄ Cosmos Client ready! Press RIGHT SHIFT to open GUI.");
    }

    public static FeatureManager getFeatureManager() {
        return featureManager;
    }
}
