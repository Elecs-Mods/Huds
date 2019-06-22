package elec332.huds.client.hud.armor;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.item.ItemStack;

class DamageUtils {
    static int getDamage(ItemStack itemStack) {
        int damage = itemStack.getItemDamage(); // Vanilla

        IElectricItemManager manager = ElectricItem.manager;
        if (manager != null && itemStack.getItem() instanceof IElectricItem) { // IC2
            damage = (int) Math.max(manager.getMaxCharge(itemStack) - manager.getCharge(itemStack), 0);
        }

        return damage;
    }

    static int getMaxDamage(ItemStack itemStack) {
        int maxDamage = itemStack.getMaxDamage(); // Vanilla

        IElectricItemManager manager = ElectricItem.manager;
        if (manager != null && itemStack.getItem() instanceof IElectricItem) { // IC2
            maxDamage = (int) manager.getMaxCharge(itemStack);
        }

        return maxDamage;
    }
}
