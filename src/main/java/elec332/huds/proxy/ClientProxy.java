package elec332.huds.proxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import elec332.core.ElecCore;
import elec332.core.api.config.IConfigurableElement;
import elec332.core.api.registry.ISingleRegister;
import elec332.core.config.ConfigWrapper;
import elec332.core.hud.AbstractHud;
import elec332.huds.Huds;
import elec332.huds.client.GuiFactory;
import elec332.huds.client.hud.armor.ArmorHud;
import elec332.huds.client.hud.damage.DamageHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 1-4-2017.
 */
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        this.huds = Sets.newHashSet();
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static ClientProxy instance;
    public static ConfigWrapper config;
    private final Set<WrappedHud> huds;

    public static List<IConfigElement> getCategories() {
        return instance.huds.stream().map(wrappedHud -> new ConfigElement(config.getConfiguration().getCategory(wrappedHud.category))).collect(Collectors.toList());
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Huds.MODID)) {
            config.refresh(false);
        }
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Huds.logger.info("PreInitializing...");
        config = new ConfigWrapper(new Configuration(event.getSuggestedConfigurationFile()));
        registerHuds(this.huds);
        for (WrappedHud hud : huds) {
            config.registerConfigurableElement(hud);
        }
        config.refresh();
    }

    private void registerHuds(Set<WrappedHud> huds) {
        huds.add(new WrappedHud(new ArmorHud(), "ArmorHud"));
        huds.add(new WrappedHud(new DamageHud(), "DamageHud"));
    }

    @Override
    public void registerClientCommands(ISingleRegister<ICommand> commandRegistry) {
        commandRegistry.register(new CommandBase() {

            static final String show = "show", reload = "reload";

            @Nonnull
            @Override
            public String getName() {
                return "hudconfig";
            }

            @Nonnull
            @Override
            public String getUsage(@Nonnull ICommandSender sender) {
                return "hudconfig <" + show + "|" + reload + ">";
            }

            @Nonnull
            @Override
            public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
                return Lists.newArrayList(show, reload);
            }

            @Override
            public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
                return true;
            }

            @Override
            public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
                if (!(sender instanceof EntityPlayerSP)) {
                    return;
                }
                if (args.length != 1) {
                    throw new CommandException(args.length == 0 ? "No argument provided." : "Too many arguments provided: " + Lists.newArrayList(args));
                }
                String s = args[0];
                switch (s) {
                    case show:
                        ElecCore.tickHandler.registerCall(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiFactory.ConfigGui(Minecraft.getMinecraft().currentScreen)), Side.CLIENT);
                        break;
                    case reload:
                        config.refresh();
                        break;
                    default:
                        break;
                }
            }

        });
    }

    private class WrappedHud implements IConfigurableElement {

        private WrappedHud(AbstractHud hud, String config) {
            this.hud = hud.setConfigCategory(category = config);
        }

        private final AbstractHud hud;
        private final String category;
        private boolean enabled = true;
        private boolean registered = true;

        @Override
        public void reconfigure(Configuration config) {
            hud.reconfigure(config);
            this.enabled = config.getBoolean("enabled", hud.getConfigCategory(), true, "Sets whether this HUD will be shown in-game.");
            check();
        }

        private void check() {
            if (registered != this.enabled) {
                if (enabled) {
                    MinecraftForge.EVENT_BUS.register(hud);
                } else {
                    MinecraftForge.EVENT_BUS.unregister(hud);
                }
            }
            registered = this.enabled;
        }

    }

}
