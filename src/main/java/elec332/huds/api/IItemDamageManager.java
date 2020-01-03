package elec332.huds.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.ToIntFunction;

/**
 * Created by Elec332 on 3-1-2020
 */
public interface IItemDamageManager {

    public void registerDamageGetter(Item item, ToIntFunction<ItemStack> damageGetter);

    public void registerMaxDamageGetter(Item item, ToIntFunction<ItemStack> maxDamageGetter);

}
