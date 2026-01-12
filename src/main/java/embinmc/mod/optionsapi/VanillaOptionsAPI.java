package embinmc.mod.optionsapi;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class VanillaOptionsAPI implements ModInitializer {
	public static final String MOD_ID = "optionsapi";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final Map<Identifier, Supplier<OptionInstance<?>>> OPTIONS = HashMap.newHashMap(32);
    public static final Map<Identifier, OptionsMenuLocation> OPTIONS_MENU = HashMap.newHashMap(32);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}

    public static void register(Identifier identifier, OptionsMenuLocation menu, Supplier<OptionInstance<?>> supplier) {
        VanillaOptionsAPI.OPTIONS.put(identifier, supplier);
        VanillaOptionsAPI.OPTIONS_MENU.put(identifier, menu);
    }
}