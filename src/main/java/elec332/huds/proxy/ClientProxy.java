package elec332.huds.proxy;

import com.google.common.collect.Sets;
import elec332.core.api.config.IConfigurableElement;
import elec332.core.config.AbstractConfigWrapper;
import elec332.core.config.ConfigWrapper;
import elec332.core.hud.AbstractHud;
import elec332.huds.Huds;
import elec332.huds.client.hud.armor.ArmorHud;
import elec332.huds.client.hud.damage.DamageHud;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created by Elec332 on 1-4-2017.
 */
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        this.huds = Sets.newHashSet();
    }

    public static AbstractConfigWrapper config;
    private final Set<WrappedHud> huds;

    @Override
    public void preInit() {
        Huds.logger.info("PreInitializing...");
        config = new ConfigWrapper(Huds.instance, ModConfig.Type.CLIENT, "huds");
        registerHuds((h, n) -> this.huds.add(new WrappedHud(h, n)));
        for (WrappedHud hud : huds) {
            config.registerConfigurableElement(hud);
        }
        config.register();
    }

    private void registerHuds(BiConsumer<AbstractHud, String> registry) {
        registry.accept(new ArmorHud(), "ArmorHud");
        registry.accept(new DamageHud(), "DamageHud");
    }

    private static class WrappedHud implements IConfigurableElement {

        private WrappedHud(AbstractHud hud, String config) {
            this.hud = hud.setConfigCategory(category = config);
        }

        private final AbstractHud hud;
        private final String category;
        private ForgeConfigSpec.BooleanValue enabled;
        private boolean registered = true;

        @Override
        public void registerProperties(@Nonnull ForgeConfigSpec.Builder config) {
            config.push(category);
            enabled = config
                    .comment("Sets whether this HUD will be shown in-game.")
                    .define("enabled", true);
            hud.registerProperties(config);
            config.pop();
        }

        @Override
        public void load() {
            if (registered != this.enabled.get()) {
                if (enabled.get()) {
                    MinecraftForge.EVENT_BUS.register(hud);
                } else {
                    MinecraftForge.EVENT_BUS.unregister(hud);
                }
            }
            registered = this.enabled.get();
            hud.load();
        }

    }

}
