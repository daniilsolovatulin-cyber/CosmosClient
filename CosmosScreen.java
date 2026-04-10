package com.cosmosclient.gui;

import com.cosmosclient.CosmosClient;
import com.cosmosclient.features.Feature;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Random;

public class CosmosScreen extends Screen {

    // Layout
    private static final int WIN_W = 520;
    private static final int WIN_H = 320;
    private static final int TAB_H = 28;
    private static final int SIDEBAR_W = 130;
    private static final int CARD_W = 140;
    private static final int CARD_H = 38;
    private static final int CARD_PAD = 6;

    private Feature.Category selectedCategory = Feature.Category.COMBAT;
    private Feature hoveredFeature = null;

    // Stars
    private final float[][] stars = new float[120][3];

    // Animation
    private float animTick = 0f;

    public CosmosScreen() {
        super(Text.literal("Cosmos Client"));
        Random rng = new Random();
        for (float[] s : stars) {
            s[0] = rng.nextFloat();
            s[1] = rng.nextFloat();
            s[2] = rng.nextFloat(); // twinkle phase
        }
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        animTick += delta;
        int ox = (this.width - WIN_W) / 2;
        int oy = (this.height - WIN_H) / 2;

        renderStarfield(ctx, ox, oy);
        renderWindow(ctx, ox, oy, mx, my);
        super.render(ctx, mx, my, delta);
    }

    // ─── Starfield ────────────────────────────────────────────────────────────
    private void renderStarfield(DrawContext ctx, int ox, int oy) {
        // Deep space background behind window
        fillRect(ctx, ox, oy, WIN_W, WIN_H, 0xFF03010F);

        // Nebula blobs (additive-ish via alpha layers)
        fillCircleGlow(ctx, ox + 80,  oy + 60,  90, 0x14_4A0070);
        fillCircleGlow(ctx, ox + 400, oy + 200, 110, 0x10_001A50);
        fillCircleGlow(ctx, ox + 260, oy + 160, 130, 0x0C_000830);

        // Stars
        for (float[] s : stars) {
            int sx = ox + (int)(s[0] * WIN_W);
            int sy = oy + (int)(s[1] * WIN_H);
            float twinkle = (float)Math.sin(animTick * 0.04f + s[2] * 6.28f);
            int alpha = (int)(120 + 80 * twinkle);
            int bright = (int)(180 + 60 * twinkle);
            int col = (alpha << 24) | (bright << 16) | (bright << 8) | bright;
            ctx.fill(sx, sy, sx + 1, sy + 1, col);
        }
    }

    // ─── Window chrome ────────────────────────────────────────────────────────
    private void renderWindow(DrawContext ctx, int ox, int oy, int mx, int my) {
        // Outer glow border
        drawBorder(ctx, ox - 1, oy - 1, WIN_W + 2, WIN_H + 2, 0xFF1A0A40);
        drawBorder(ctx, ox,     oy,     WIN_W,     WIN_H,     0xFF5B2FCF);

        // Title bar
        fillRect(ctx, ox, oy, WIN_W, TAB_H, 0xE8_0D0625);
        drawHorizontalGradient(ctx, ox, oy, WIN_W, TAB_H, 0xCC_5B2FCF, 0xCC_0D0625);

        // Cosmos icon + title
        long t = System.currentTimeMillis();
        int titleColor = rainbow(t, 0);
        ctx.drawTextWithShadow(this.textRenderer, "☄  COSMOS CLIENT", ox + 10, oy + 9, titleColor);

        // Version tag
        ctx.drawTextWithShadow(this.textRenderer, "§81.21.1", ox + WIN_W - 36, oy + 9, 0xFF888888);

        // Sidebar (category tabs)
        renderSidebar(ctx, ox, oy, mx, my);

        // Feature grid
        renderFeatureGrid(ctx, ox, oy, mx, my);

        // Bottom bar
        fillRect(ctx, ox, oy + WIN_H - 18, WIN_W, 18, 0xCC_0D0625);
        ctx.drawTextWithShadow(this.textRenderer,
                "§7RIGHT SHIFT §8— §7Открыть/закрыть    §7Нажми на чит чтобы включить/выключить",
                ox + 6, oy + WIN_H - 12, 0xFF555577);
    }

    // ─── Sidebar ──────────────────────────────────────────────────────────────
    private void renderSidebar(DrawContext ctx, int ox, int oy, int mx, int my) {
        int x = ox;
        int y = oy + TAB_H;
        int h = WIN_H - TAB_H - 18;
        fillRect(ctx, x, y, SIDEBAR_W, h, 0xCC_080318);
        drawBorder(ctx, x, y, SIDEBAR_W, h, 0xFF1E0A4A);

        int tabY = y + 8;
        for (Feature.Category cat : Feature.Category.values()) {
            boolean sel = cat == selectedCategory;
            boolean hover = mx >= x + 4 && mx < x + SIDEBAR_W - 4 && my >= tabY && my < tabY + 22;
            int bg = sel ? 0xEE_5B2FCF : (hover ? 0x88_2A1060 : 0x44_110330);
            fillRect(ctx, x + 4, tabY, SIDEBAR_W - 8, 22, bg);
            if (sel) drawBorder(ctx, x + 4, tabY, SIDEBAR_W - 8, 22, 0xFF8855FF);

            int count = CosmosClient.featureManager.getByCategory(cat).stream()
                    .filter(Feature::isEnabled).mapToInt(f -> 1).sum();

            long t = System.currentTimeMillis();
            int labelColor = sel ? rainbow(t, cat.ordinal() * 300L) : (hover ? 0xFFCCAAFF : 0xFFAA88CC);
            ctx.drawTextWithShadow(this.textRenderer, cat.getDisplayName(), x + 10, tabY + 7, labelColor);
            if (count > 0) {
                String badge = String.valueOf(count);
                ctx.drawTextWithShadow(this.textRenderer, "§a" + badge, x + SIDEBAR_W - 16, tabY + 7, 0xFF00FF88);
            }
            tabY += 26;
        }
    }

    // ─── Feature grid ─────────────────────────────────────────────────────────
    private void renderFeatureGrid(DrawContext ctx, int ox, int oy, int mx, int my) {
        int gx = ox + SIDEBAR_W + 6;
        int gy = oy + TAB_H + 6;
        int gridW = WIN_W - SIDEBAR_W - 10;
        int gridH = WIN_H - TAB_H - 24;
        fillRect(ctx, gx - 2, gy - 2, gridW + 4, gridH + 4, 0x44_0D0625);

        List<Feature> features = CosmosClient.featureManager.getByCategory(selectedCategory);
        int cols = (gridW) / (CARD_W + CARD_PAD);
        if (cols < 1) cols = 1;

        hoveredFeature = null;

        for (int i = 0; i < features.size(); i++) {
            Feature f = features.get(i);
            int col = i % cols;
            int row = i / cols;
            int cx = gx + col * (CARD_W + CARD_PAD);
            int cy = gy + row * (CARD_H + CARD_PAD);

            boolean hover = mx >= cx && mx < cx + CARD_W && my >= cy && my < cy + CARD_H;
            if (hover) hoveredFeature = f;

            renderFeatureCard(ctx, f, cx, cy, hover);
        }

        // Tooltip
        if (hoveredFeature != null) {
            int tx = ox + SIDEBAR_W + 4;
            int ty = oy + WIN_H - 36;
            ctx.drawTextWithShadow(this.textRenderer,
                    "§b" + hoveredFeature.getName() + " §7— §f" + hoveredFeature.getDescription(),
                    tx, ty, 0xFFCCCCCC);
        }
    }

    private void renderFeatureCard(DrawContext ctx, Feature f, int x, int y, boolean hover) {
        boolean on = f.isEnabled();
        long t = System.currentTimeMillis();

        // Card BG
        int bg = on
                ? (hover ? 0xEE_2A0E6A : 0xCC_1A0850)
                : (hover ? 0x88_1E0A3C : 0x66_0A0320);
        fillRect(ctx, x, y, CARD_W, CARD_H, bg);

        // Left accent bar
        int accent = on ? rainbow(t, f.getName().hashCode() * 100L) : 0xFF333355;
        fillRect(ctx, x, y, 3, CARD_H, accent);

        // Border
        drawBorder(ctx, x, y, CARD_W, CARD_H, on ? 0xFF5B2FCF : 0xFF2A1440);

        // Toggle dot
        int dotColor = on ? 0xFF00FF88 : 0xFF443355;
        fillRect(ctx, x + CARD_W - 14, y + CARD_H / 2 - 3, 6, 6, dotColor);
        if (on) {
            // Glow ring
            drawBorder(ctx, x + CARD_W - 15, y + CARD_H / 2 - 4, 8, 8, 0x6600FF88);
        }

        // Name
        int nameColor = on ? rainbow(t, f.getName().hashCode() * 80L) : 0xFFAA99CC;
        ctx.drawTextWithShadow(this.textRenderer, f.getName(), x + 7, y + 8, nameColor);

        // Category micro-label
        ctx.drawTextWithShadow(this.textRenderer,
                on ? "§aВКЛ" : "§8ВЫКЛ",
                x + 7, y + CARD_H - 13, 0xFFFFFFFF);
    }

    // ─── Mouse click ──────────────────────────────────────────────────────────
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int ox = (this.width - WIN_W) / 2;
        int oy = (this.height - WIN_H) / 2;

        // Category tabs
        int tx = ox;
        int ty = oy + TAB_H + 8;
        for (Feature.Category cat : Feature.Category.values()) {
            if (mx >= tx + 4 && mx < tx + SIDEBAR_W - 4 && my >= ty && my < ty + 22) {
                selectedCategory = cat;
                return true;
            }
            ty += 26;
        }

        // Feature cards
        int gx = ox + SIDEBAR_W + 6;
        int gy = oy + TAB_H + 6;
        int gridW = WIN_W - SIDEBAR_W - 10;
        int cols = (gridW) / (CARD_W + CARD_PAD);
        if (cols < 1) cols = 1;
        List<Feature> features = CosmosClient.featureManager.getByCategory(selectedCategory);
        for (int i = 0; i < features.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int cx = gx + col * (CARD_W + CARD_PAD);
            int cy = gy + row * (CARD_H + CARD_PAD);
            if (mx >= cx && mx < cx + CARD_W && my >= cy && my < cy + CARD_H) {
                features.get(i).toggle();
                return true;
            }
        }

        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    // ─── Drawing helpers ──────────────────────────────────────────────────────
    private void fillRect(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + h, color);
    }

    private void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x,         y,         x + w,     y + 1,     color); // top
        ctx.fill(x,         y + h - 1, x + w,     y + h,     color); // bottom
        ctx.fill(x,         y,         x + 1,     y + h,     color); // left
        ctx.fill(x + w - 1, y,         x + w,     y + h,     color); // right
    }

    private void drawHorizontalGradient(DrawContext ctx, int x, int y, int w, int h, int colorL, int colorR) {
        ctx.fillGradient(x, y, x + w, y + h, colorL, colorR);
    }

    private void fillCircleGlow(DrawContext ctx, int cx, int cy, int r, int color) {
        int steps = r / 4;
        for (int i = 0; i < steps; i++) {
            int cr = r - i * 4;
            int alpha = ((color >> 24) & 0xFF) * i / steps;
            int c = (color & 0x00FFFFFF) | (alpha << 24);
            ctx.fill(cx - cr, cy - cr / 2, cx + cr, cy + cr / 2, c);
        }
    }

    private int rainbow(long time, long offset) {
        float hue = ((time + offset) % 4000) / 4000f;
        int rgb = java.awt.Color.HSBtoRGB(hue, 0.75f, 1.0f);
        return rgb | 0xFF000000;
    }
}
