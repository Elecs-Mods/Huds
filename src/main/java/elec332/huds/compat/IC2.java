package elec332.huds.compat;

import elec332.core.api.module.ElecModule;
import elec332.core.util.RegistryHelper;
import elec332.huds.Huds;
import elec332.huds.util.ItemDamageHelper;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Created by Elec332 on 3-1-2020
 */
@ElecModule(owner = Huds.MODID, name = "IC2Compat", modDependencies = "ic2")
public class IC2 {

    @ElecModule.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        final IElectricItemManager manager = ElectricItem.manager;
        if (manager != null) {
            RegistryHelper.getItemRegistry().forEach(item -> {
                if (item instanceof IElectricItem) {
                    ItemDamageHelper.INSTANCE.registerDamageGetter(item, itemStack -> (int) Math.max(manager.getMaxCharge(itemStack) - manager.getCharge(itemStack), 0));
                    ItemDamageHelper.INSTANCE.registerMaxDamageGetter(item, itemStack -> (int) manager.getMaxCharge(itemStack));
                }
            });
        }
    }

}
