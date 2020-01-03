package elec332.huds.util;

import com.google.common.collect.Maps;
import elec332.core.api.APIHandlerInject;
import elec332.core.api.IAPIHandler;
import elec332.huds.api.IItemDamageManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.function.ToIntFunction;

/**
 * Created by Elec332 on 3-1-2020
 */
public enum ItemDamageHelper implements IItemDamageManager {

    INSTANCE;

    ItemDamageHelper() {
        damageGetters = Maps.newIdentityHashMap();
        maxDamageGetters = Maps.newIdentityHashMap();
        defaultDamageGetter = ItemStack::getDamage;
        defaultMaxDamageGetter = ItemStack::getMaxDamage;
    }

    private final Map<Item, ToIntFunction<ItemStack>> damageGetters, maxDamageGetters;
    private final ToIntFunction<ItemStack> defaultDamageGetter, defaultMaxDamageGetter;

    @Override
    public void registerDamageGetter(Item item, ToIntFunction<ItemStack> damageGetter) {
        damageGetters.put(item, damageGetter);
    }

    @Override
    public void registerMaxDamageGetter(Item item, ToIntFunction<ItemStack> maxDamageGetter) {
        maxDamageGetters.put(item, maxDamageGetter);
    }

    public int getDamage(ItemStack stack) {
        return damageGetters.getOrDefault(stack.getItem(), defaultDamageGetter).applyAsInt(stack);
    }

    public int getMaxItemDamage(ItemStack stack) {
        return maxDamageGetters.getOrDefault(stack.getItem(), defaultMaxDamageGetter).applyAsInt(stack);
    }

    @APIHandlerInject
    private void injectRenderingRegistry(IAPIHandler apiHandler) {
        apiHandler.inject(INSTANCE, IItemDamageManager.class);
    }

}
