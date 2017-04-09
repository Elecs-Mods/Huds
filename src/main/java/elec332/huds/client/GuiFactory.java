package elec332.huds.client;

import elec332.huds.Huds;
import elec332.huds.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by Elec332 on 2-4-2017.
 */
public class GuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

	public static class ConfigGui extends GuiConfig {

		public ConfigGui(GuiScreen parentScreen) {
			super(parentScreen, ClientProxy.getCategories(), Huds.MODID, false, false, "Huds config menu");
		}

	}

}
