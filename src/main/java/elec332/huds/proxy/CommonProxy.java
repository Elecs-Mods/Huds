package elec332.huds.proxy;

import com.mojang.brigadier.CommandDispatcher;
import elec332.huds.Huds;
import net.minecraft.command.ISuggestionProvider;

/**
 * Created by Elec332 on 1-4-2017.
 */
public class CommonProxy {

    public void preInit() {
        Huds.logger.info("This mod is ClientOnly, aborting loading...");
    }

    public void registerClientCommands(CommandDispatcher<? extends ISuggestionProvider> commandRegistry) {
    }

}
