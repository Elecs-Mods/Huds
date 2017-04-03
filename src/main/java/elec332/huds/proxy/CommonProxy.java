package elec332.huds.proxy;

import elec332.core.api.registry.ISingleRegister;
import elec332.huds.Huds;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Elec332 on 1-4-2017.
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event){
		Huds.logger.info("This mod is ClientOnly, aborting loading...");
	}

	public void registerClientCommands(ISingleRegister<ICommand> commandRegistry) {
	}

}
