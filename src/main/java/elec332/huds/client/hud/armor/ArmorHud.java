package elec332.huds.client.hud.armor;

import com.electronwill.nightconfig.core.EnumGetMethod;
import elec332.core.hud.AbstractHud;
import elec332.core.hud.drawing.ItemStackDrawer;
import elec332.core.hud.position.Alignment;
import elec332.core.hud.position.HorizontalStartingPoint;
import elec332.core.hud.position.VerticalStartingPoint;
import elec332.core.util.ItemStackHelper;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 8-1-2017.
 */
@OnlyIn(Dist.CLIENT)
public class ArmorHud extends AbstractHud {

    public ArmorHud() {
        super(Alignment.LEFT, HorizontalStartingPoint.LEFT, VerticalStartingPoint.MIDDLE);
    }

    private ForgeConfigSpec.EnumValue<DamageDisplayType> displayType;
    private ForgeConfigSpec.BooleanValue showArmor, showTools;

    @Override
    public void registerProperties(@Nonnull ForgeConfigSpec.Builder config, ModConfig.Type type) {
        displayType = config
                .comment("This defines the way that the tool/armor damage will be displayed.")
                .defineEnum("displayType", DamageDisplayType.USES_LEFT, EnumGetMethod.NAME);
        showArmor = config
                .comment("Whether to enable the armor part of the HUD")
                .define("showArmor", true);
        showTools = config
                .comment("Whether to enable the tools part of the HUD")
                .define("showTools", true);
    }

    @Override
    public int getHudHeight() {
        return 117;
    }

    @Override
    public void renderHud(@Nonnull ClientPlayerEntity player, @Nonnull World world, @Nonnull Alignment alignment, int startX, int startY, float partialTicks) {
        int h = startY + 81;

        for (EquipmentSlotType eeqs : EquipmentSlotType.values()) {
            EquipmentSlotType.Group type = eeqs.getSlotType();
            boolean hand = type == EquipmentSlotType.Group.HAND;
            if (hand && !showTools.get()) {
                continue;
            }
            if (!hand && !showArmor.get()) {
                continue;
            }
            ItemStack stack = player.getItemStackFromSlot(eeqs);
            if (ItemStackHelper.isStackValid(stack)) {
                int h2 = h;
                if (hand) {
                    h2 += 18 * eeqs.ordinal();
                } else {
                    h2 += 9 - 18 * eeqs.ordinal();
                }
                String s = null;
                if (stack.isDamageable()) {
                    s = displayType.get().getDamageForDisplay(stack);
                }
                alignment.renderHudPart(ItemStackDrawer.INSTANCE, stack, s, startX, h2);
            }
        }
    }

}
