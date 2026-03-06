package embinmc.mod.optionsapi;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class VanillaOptionsAPI implements ClientModInitializer {
    public static final String MOD_ID = "vanillaoptionsapi";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static final OptionInstance<OptionsMenuLocation> TEST_SETTING = OptionUtil.enumSlider(
            "options.vanillaoptionsapi.test_setting",
            (caption, value) -> caption.copy().append(": ").append(value.getSerializedName()),
            OptionsMenuLocation::values, OptionsMenuLocation.CODEC, OptionsMenuLocation.NONE
    );
    static final Map<Identifier, Supplier<OptionInstance<?>>> OPTIONS = HashMap.newHashMap(OptionsMenuLocation.values().length);
    private static final Map<OptionsMenuLocation, List<Identifier>> OPTIONS_MENU = Util.make(HashMap.newHashMap(OptionsMenuLocation.values().length), map -> {
        for (OptionsMenuLocation menuLocation : OptionsMenuLocation.values()) {
            map.put(menuLocation, new ArrayList<>(menuLocation == OptionsMenuLocation.NONE ? 64 : 24));
        }
    });
    public static boolean DEBUG = true;

    /**
     * Registers a {@link OptionInstance} to save, load, and appear in a specified menu.
     * <p>If you don't want your option to appear in any vanilla menu, use {@link OptionsMenuLocation#NONE}.
     * <p>The setting will be saved to {@code options.txt} with the name {@code namespace.path}
     *
     * @param identifier The {@link Identifier} your setting uses for saving and loading.
     * @param menu The menu you want the setting to appear in.
     * @param supplier Supplier for the {@link OptionInstance} you want to register.
     * @return The {@link Identifier} of the registered option.
     */
    public static Identifier register(Identifier identifier, OptionsMenuLocation menu, Supplier<OptionInstance<?>> supplier) {
        VanillaOptionsAPI.OPTIONS.put(identifier, supplier);
        VanillaOptionsAPI.OPTIONS_MENU.get(menu).add(identifier);
        VanillaOptionsAPI.LOGGER.debug("Registering setting {}", identifier);
        return identifier;
    }

    @Override
    public void onInitializeClient() {
        if (VanillaOptionsAPI.DEBUG) {
            registerTest("sounds", OptionsMenuLocation.SOUNDS);
            registerTest("accessibility", OptionsMenuLocation.ACCESSIBILITY);
            registerTest("chat", OptionsMenuLocation.CHAT);
            registerTest("controls", OptionsMenuLocation.CONTROLS);
            registerTest("skin", OptionsMenuLocation.SKIN);
            registerTest("mouse", OptionsMenuLocation.MOUSE);
            registerTest("online", OptionsMenuLocation.ONLINE);
            registerTest("video", OptionsMenuLocation.VIDEO);
            registerTest("test", OptionsMenuLocation.VIDEO, Identifier.DEFAULT_NAMESPACE);
            registerTest("test2", OptionsMenuLocation.VIDEO, Identifier.DEFAULT_NAMESPACE);
            registerTest("test3", OptionsMenuLocation.VIDEO, Identifier.REALMS_NAMESPACE);
            registerTest("test5", OptionsMenuLocation.VIDEO, Identifier.DEFAULT_NAMESPACE);
        }
    }

    private static OptionInstance<OptionsMenuLocation> testSetting() {
        return VanillaOptionsAPI.TEST_SETTING;
    }

    private static void registerTest(String name, OptionsMenuLocation location) {
        VanillaOptionsAPI.register(Identifier.fromNamespaceAndPath(MOD_ID, name), location, VanillaOptionsAPI::testSetting);
    }

    private static void registerTest(String name, OptionsMenuLocation location, String namespace) {
        VanillaOptionsAPI.register(Identifier.fromNamespaceAndPath(namespace, name), location, VanillaOptionsAPI::testSetting);
    }

    /**
     * @return A copy of the internal map used to store the registered settings and their id's
     */
    public static Map<Identifier, Supplier<OptionInstance<?>>> getOptionsMap() {
        return Map.copyOf(VanillaOptionsAPI.OPTIONS);
    }

    public static List<Identifier> getIdsForMenu(OptionsMenuLocation location) {
        return List.copyOf(VanillaOptionsAPI.OPTIONS_MENU.get(location));
    }

    public static void forEachOption(BiConsumer<? super Identifier, ? super Supplier<OptionInstance<?>>> biConsumer) {
        VanillaOptionsAPI.OPTIONS.forEach(biConsumer);
    }
}