package elec332.huds.client.hud.armor;

import elec332.huds.util.ItemDamageHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

/**
 * Created by Elec332 on 8-1-2017.
 */
public enum DamageDisplayType implements IDamageDisplayType {

    DAMAGE {
        @Override
        public String getDamageForDisplay(@Nonnull ItemStack stack) {
            return EMPTY + ItemDamageHelper.INSTANCE.getDamage(stack);
        }

    },
    USES_LEFT {
        @Override
        public String getDamageForDisplay(@Nonnull ItemStack stack) {
            return EMPTY + (ItemDamageHelper.INSTANCE.getMaxItemDamage(stack) - ItemDamageHelper.INSTANCE.getDamage(stack));
        }

    },
    PERCENT {

        private final DecimalFormat format = new DecimalFormat("###.#");

        @Override
        public String getDamageForDisplay(@Nonnull ItemStack stack) {
            int damage = ItemDamageHelper.INSTANCE.getDamage(stack);
            int maxDamage = ItemDamageHelper.INSTANCE.getMaxItemDamage(stack);
            return format.format((maxDamage - damage) / (float) maxDamage * 100) + "%";
        }

    },
    NONE {
        @Override
        public String getDamageForDisplay(@Nonnull ItemStack stack) {
            return null;
        }

    };

    private static final String EMPTY = "";

}
