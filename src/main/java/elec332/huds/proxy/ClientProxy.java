package elec332.huds.proxy;

import com.google.common.collect.Sets;
import elec332.core.api.config.IConfigurableElement;
import elec332.core.api.registry.ISingleRegister;
import elec332.core.config.ConfigWrapper;
import elec332.core.hud.AbstractHud;
import elec332.core.util.AbstractCommandBase;
import elec332.huds.Huds;
import elec332.huds.client.hud.armor.ArmorHud;
import elec332.huds.client.hud.damage.DamageHud;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 1-4-2017.
 */
public class ClientProxy extends CommonProxy {

	public ClientProxy(){
		this.huds = Sets.newHashSet();
		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}

	private static ClientProxy instance;
	public static ConfigWrapper config;
	private final Set<WrappedHud> huds;

	public static List<IConfigElement> getCategories(){
		return instance.huds.stream().map(wrappedHud -> new ConfigElement(config.getConfiguration().getCategory(wrappedHud.category))).collect(Collectors.toList());
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if (event.getModID().equals(Huds.MODID)) {
			config.refresh(false);
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		Huds.logger.info("PreInitializing...");
		config = new ConfigWrapper(new Configuration(event.getSuggestedConfigurationFile()));
		registerHuds(this.huds);
		for (WrappedHud hud : huds){
			config.registerConfigurableElement(hud);
		}
		config.refresh();
	}

	private void registerHuds(Set<WrappedHud> huds){
		huds.add(new WrappedHud(new ArmorHud(), "armorHud"));
		huds.add(new WrappedHud(new DamageHud(), "damageHud"));
	}

	@Override
	public void registerClientCommands(ISingleRegister<ICommand> commandRegistry) {
		commandRegistry.register(new AbstractCommandBase() {

			@Nonnull
			@Override
			public String getMCCommandName() {
				return "reloadhud";
			}

			@Nonnull
			@Override
			public String getMCCommandUsage(ICommandSender iCommandSender) {
				return "reloadhud";
			}

			@Override
			public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
				config.refresh();
			}

		});
	}

	private class WrappedHud implements IConfigurableElement {

		private WrappedHud(AbstractHud hud, String config){
			this.hud = hud.setConfigCategory(category = config);
		}

		private final AbstractHud hud;
		private final String category;
		private boolean enabled = true;
		private boolean registered = true;

		@Override
		public void reconfigure(Configuration config) {
			hud.configureHud(config);
			this.enabled = config.getBoolean("enabled", hud.getConfigCategory(), true, "Sets whether this HUD will be shown in-game.");
			check();
		}

		private void check(){
			if (registered != this.enabled){
				if (enabled){
					MinecraftForge.EVENT_BUS.register(hud);
				} else {
					MinecraftForge.EVENT_BUS.unregister(hud);
				}
			}
			registered = this.enabled;
		}

	}

}
