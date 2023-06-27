package elec332.huds.client.hud.damage;

import com.mojang.blaze3d.platform.GlStateManager;
import elec332.core.client.RenderHelper;
import elec332.core.client.util.GuiDraw;
import elec332.core.hud.AbstractHud;
import elec332.core.hud.drawing.EntityDrawer;
import elec332.core.hud.drawing.IDrawer;
import elec332.core.hud.position.Alignment;
import elec332.core.hud.position.HorizontalStartingPoint;
import elec332.core.hud.position.VerticalStartingPoint;
import elec332.huds.Huds;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

/**
 * Created by Elec332 on 13-1-2017.
 */
public class DamageHud extends AbstractHud {

    public DamageHud() {
        super(Alignment.LEFT, HorizontalStartingPoint.LEFT, VerticalStartingPoint.TOP);
    }

    @Nullable
    private LivingEntity entity;
    private int noSelectTime;
    private ForgeConfigSpec.IntValue noSelTimeConf, deathShowConf;

    @Override
    public int getHudHeight() {
        return 50;
    }

    @Override
    public void registerProperties(@Nonnull ForgeConfigSpec.Builder config, ModConfig.Type type) {
        this.noSelTimeConf = config
                .comment("The time to show the mob-hud whilst not hovering over it anymore. (In ticks, 1/20th of a second)")
                .defineInRange("noSelectTime", 200, 0, 1000);
        this.deathShowConf = config
                .comment("The time to show the mob-hud of an entity whilst it's dead. (In ticks, 1/20th of a second)")
                .defineInRange("timeShowDeath", 20, 0, 1000);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("all")
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Entity entity = GuiDraw.mc.getRenderManager().pointedEntity;
        if (this.entity != null && this.entity.deathTime > 0 && this.noSelectTime < noSelTimeConf.get() - deathShowConf.get()) {
            this.noSelectTime = noSelTimeConf.get() - deathShowConf.get();
        }
        if (entity != null && entity instanceof LivingEntity) {
            if (this.entity == null || this.entity.getEntityId() != entity.getEntityId()) {
                this.entity = (LivingEntity) entity;
                this.noSelectTime = 0;
            }
        } else if (this.entity != null) {
            noSelectTime++;
            if (noSelectTime >= noSelTimeConf.get()) {
                this.entity = null;
                this.noSelectTime = 0;
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHud(@Nonnull ClientPlayerEntity player, @Nonnull World world, @Nonnull Alignment alignment, int startX, int startY, float partialTicks) {
        if (entity != null) {
            RenderHelper.bindTexture(hud);
            GlStateManager.color4f(1, 1, 1, 1);

            GuiDraw.drawScaledCustomSizeModalRect(startX, startY, 0, 0, 120, 120, 40, 40, 350, 128);
            GuiDraw.drawScaledCustomSizeModalRect(startX + 43, startY, 120, 0, 240, 75, 80, 25, 350, 128);

            int s = 125, e = 345;
            float scale = entity.getHealth() / entity.getMaxHealth();
            GuiDraw.drawScaledCustomSizeModalRect(startX + 43 + 1, startY + 36 / 3, s, 76, (int) ((e - s) * scale), 37, (int) (((e - s + 2) * scale) / 3), 32 / 3, 350, 128);

            GlStateManager.pushMatrix();
            GlStateManager.translatef(startX + 82, startY + 4, 0);
            GlStateManager.scalef(0.65f, 0.65f, 0.65f);
            FontRenderer font = RenderHelper.getMCFontrenderer();
            String txt = entity.getName().getFormattedText();
            if (entity.isChild() && !entity.hasCustomName()) {
                txt = "Baby " + txt;
            }
            font.drawString(txt, -(font.getStringWidth(txt) / 2f), 0, Color.WHITE.getRGB());
            txt = (int) entity.getHealth() + "/" + (int) entity.getMaxHealth();
            font.drawString(txt, -(font.getStringWidth(txt) / 2f), 17, Color.WHITE.getRGB());
            GlStateManager.popMatrix();

            float entS = 1;
            if (entity.getHeight() > 2) {
                entS = entity.getHeight() / 2;
            }
            if (entity.getWidth() > 1.5f) {
                entS = Math.max(entS, entity.getWidth() / 1.5f);
            }

            //int i = 15728880;
            //int j = i % 65536;
            //int k = i / 65536;
            //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.color4f(1, 1, 1, 1);
            float playerAngle = (float) Math.toDegrees(Math.atan2(player.getPosZ() - entity.getPosZ(), player.getPosX() - entity.getPosX()));
            alignment.renderHudPart(hudEntityDrawer, entity, startX, startY, playerAngle - 90, entS);
        }
    }

    private static final IDrawer<Entity> hudEntityDrawer;
    private static final ResourceLocation hud = new ResourceLocation(Huds.MODID, "mobhudbackbgound.png");

    static {
        hudEntityDrawer = new EntityDrawer(25, 33, 15);
    }

}
