package elec332.huds;

import com.mojang.brigadier.CommandDispatcher;
import elec332.core.api.mod.IElecCoreMod;
import elec332.core.api.mod.SidedProxy;
import elec332.core.util.FMLHelper;
import elec332.huds.proxy.CommonProxy;
import net.minecraft.command.ISuggestionProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Elec332 on 1-4-2017.
 */
@Mod(Huds.MODID)
public class Huds implements IElecCoreMod {

    public Huds() {
        if (instance != null) {
            throw new RuntimeException();
        }
        instance = this;
        logger = LogManager.getLogger(MODNAME);

        IEventBus eventBus = FMLHelper.getActiveModEventBus();
        eventBus.addListener(this::preInit);
    }

    public static final String MODID = "huds";
    public static final String MODNAME = FMLHelper.getModNameEarly(MODID);

    @SidedProxy(clientSide = "elec332.huds.proxy.ClientProxy", serverSide = "elec332.huds.proxy.CommonProxy")
    private static CommonProxy proxy;

    public static Huds instance;
    public static Logger logger;

    private void preInit(FMLCommonSetupEvent event) {
        proxy.preInit();
    }

    @Override
    public void registerClientCommands(CommandDispatcher<? extends ISuggestionProvider> commandRegistry) {
        proxy.registerClientCommands(commandRegistry);
    }

}
