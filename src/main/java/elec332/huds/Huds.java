package elec332.huds;

import elec332.core.api.mod.IElecCoreMod;
import elec332.core.api.registry.ISingleRegister;
import elec332.huds.proxy.CommonProxy;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Elec332 on 1-4-2017.
 */
@Mod(modid = Huds.MODID, name = "Huds", version = "#VERSION#", dependencies = "required-after:eleccore@[#ELECCORE_VER#,);",
        acceptedMinecraftVersions = "[1.12,)", useMetadata = true, canBeDeactivated = true, clientSideOnly = true, guiFactory = "elec332.huds.client.GuiFactory")
public class Huds implements IElecCoreMod {

    public static final String MODID = "huds";

    @SidedProxy(clientSide = "elec332.huds.proxy.ClientProxy", serverSide = "elec332.huds.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static Huds instance;
    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = LogManager.getLogger("Huds");
        proxy.preInit(event);
    }

    @Override
    public void registerClientCommands(ISingleRegister<ICommand> commandRegistry) {
        proxy.registerClientCommands(commandRegistry);
    }

}
