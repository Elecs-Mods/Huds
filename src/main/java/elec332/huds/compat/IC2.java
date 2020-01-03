package elec332.huds.compat;

import elec332.core.api.module.ElecModule;
import elec332.huds.Huds;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

/**
 * Created by Elec332 on 3-1-2020
 */
@ElecModule(owner = Huds.MODID, name = "IC2Compat", modDependencies = "ic2")
public class IC2 {

    @ElecModule.EventHandler
    public void postInit(InterModProcessEvent event) {
    }

}
