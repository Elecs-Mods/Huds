package elec332.huds.client.hud.armor;

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
            return EMPTY + DamageUtils.getDamage(stack);
        }

    },
    USES_LEFT {
        @Override
        public String getDamageForDisplay(@Nonnull ItemStack stack) {
            return EMPTY + (DamageUtils.getMaxDamage(stack) - DamageUtils.getDamage(stack));
        }

    },
    PERCENT {
        private final DecimalFormat format = new DecimalFormat("###.#");

        @Override
        public String getDamageForDisplay(@Nonnull ItemStack stack) {
            int maxDamage = DamageUtils.getMaxDamage(stack);
            int damage = DamageUtils.getDamage(stack);
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
