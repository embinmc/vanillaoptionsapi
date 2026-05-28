package embinmc.mod.optionsapi;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.options.*;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.network.chat.Component;
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
    private static final Identifier SMS_ID = Identifier.fromNamespaceAndPath(MOD_ID, "show_minecraft_settings");
    private static final Component SMS_TOOLTIP = Component.translatable("options.vanillaoptionsapi.show_minecraft_settings.tooltip");
    private static final OptionInstance<Boolean> SHOW_MINECRAFT_SETTINGS = OptionInstance.createBoolean("options.vanillaoptionsapi.show_minecraft_settings", OptionInstance.cachedConstantTooltip(SMS_TOOLTIP), false);

    static final LinkedHashMap<Identifier, Supplier<OptionInstance<?>>> OPTIONS = Util.make(LinkedHashMap.newLinkedHashMap(128), map -> {
        map.put(SMS_ID, VanillaOptionsAPI::showMinecraftSettings);
    });
    private static final Map<OptionsMenuLocation, List<Identifier>> OPTIONS_MENU = Util.make(HashMap.newHashMap(OptionsMenuLocation.values().length), map -> {
        for (OptionsMenuLocation menuLocation : OptionsMenuLocation.values()) {
            map.put(menuLocation, new ArrayList<>(menuLocation == OptionsMenuLocation.NONE ? 128 : 64));
        }
        map.get(OptionsMenuLocation.NONE).add(SMS_ID);
    });
    static final Map<String, MenuLayout> MENU_LAYOUTS = Util.make(LinkedHashMap.newLinkedHashMap(64), map -> {
        map.put(Identifier.DEFAULT_NAMESPACE, MenuLayout.testMinecraftLayout());
        map.put(MOD_ID, MenuLayout.defaultLayout(MOD_ID));
    });
    static final Map<String, MenuLayout> ON_TICK = LinkedHashMap.newLinkedHashMap(64);

    public static boolean DEBUG = false;

    /**
     * Registers a {@link OptionInstance} to save, load, and appear in a specified menu.
     * <p>If you don't want your option to appear in any vanilla menu, use {@link OptionsMenuLocation#NONE}.
     * <p>The setting will be saved to {@code options.txt} with the name {@code namespace.path}.
     *
     * @param identifier The {@link Identifier} your setting uses for saving and loading.
     * @param menu The menu you want the setting to appear in.
     * @param supplier Supplier for the {@link OptionInstance} you want to register.
     * @return The {@link Identifier} of the registered option.
     */
    public static Identifier register(Identifier identifier, OptionsMenuLocation menu, Supplier<OptionInstance<?>> supplier) {
        VanillaOptionsAPI.OPTIONS.put(identifier, supplier);
        VanillaOptionsAPI.OPTIONS_MENU.get(menu).add(identifier);

        if (!VanillaOptionsAPI.MENU_LAYOUTS.containsKey(identifier.getNamespace()))
            VanillaOptionsAPI.MENU_LAYOUTS.put(identifier.getNamespace(), MenuLayout.defaultLayout(identifier.getNamespace()));

        VanillaOptionsAPI.LOGGER.debug("Registering setting {}", identifier);
        return identifier;
    }

    /**
     * Register a custom layout for how your mod's options appear in the "Mod Options" menu,
     * incase the default layout is not what you want.
     *
     * @param modNamespace The namespace of options to target
     * @param menuLayout The layout for the menu
     */
    public static void registerMenuLayout(String modNamespace, MenuLayout menuLayout) {
        VanillaOptionsAPI.MENU_LAYOUTS.put(modNamespace, menuLayout);
        VanillaOptionsAPI.LOGGER.debug("Registering menu layout for {}", modNamespace);
    }

    /**
     * Register an event for what to happen each tick in the "Mod Options" menu
     */
    public static void registerOnMenuTick(String modNamespace, MenuLayout menuLayout) {
        VanillaOptionsAPI.ON_TICK.put(modNamespace, menuLayout);
        VanillaOptionsAPI.LOGGER.debug("Registering menu tick event for {}", modNamespace);
    }

    @Override
    public void onInitializeClient() {
        if (VanillaOptionsAPI.DEBUG) {
            register(Identifier.fromNamespaceAndPath(MOD_ID, "test"), OptionsMenuLocation.ONLINE, VanillaOptionsAPI::testSetting);
        }
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

    public static MenuLayout getMenuLayout(String modNamespace) {
        return VanillaOptionsAPI.MENU_LAYOUTS.getOrDefault(modNamespace, MenuLayout.defaultLayout(modNamespace));
    }

    public static OptionInstance<Boolean> showMinecraftSettings() {
        return SHOW_MINECRAFT_SETTINGS;
    }

    public interface MenuLayout {
        void consume(final AllCustomOptionsScreen currentScreen, final OptionsList optionsList, final List<Supplier<OptionInstance<?>>> options);

        static MenuLayout defaultLayout(String modNamespace) {
            return (currentScreen, optionsList, options) -> {
                optionsList.addHeader(Component.literal(VoapiUtils.modNameElseId(modNamespace)));
                optionsList.addSmall(options.stream().map(Supplier::get).toArray(OptionInstance<?>[]::new));
            };
        }

        private static MenuLayout testMinecraftLayout() {
            return (currentScreen, optionsList, options) -> {
                Options o = Minecraft.getInstance().options;
                if (!showMinecraftSettings().get()) return;
                optionsList.addHeader(Component.literal("Minecraft")); // god forbid this needs to say anything but "Minecraft"
                optionsList.addSmall(List.of(
                        o.fov().createButton(o),
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.online"),
                                () -> new OnlineOptionsScreen(currentScreen, o)
                        )
                ));
                optionsList.addSmall(List.of(
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.skinCustomisation"),
                                () -> new SkinCustomizationScreen(currentScreen, o)
                        ),
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.sounds"),
                                () -> new SoundOptionsScreen(currentScreen, o)
                        ),
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.video"),
                                () -> new VideoSettingsScreen(currentScreen, Minecraft.getInstance(), o)
                        ),
                        VoapiUtils.openScreenButton(
                                OptionsScreen.CONTROLS,
                                () -> new ControlsScreen(currentScreen, o)
                        ),
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.language"),
                                () -> new LanguageSelectScreen(currentScreen, o, Minecraft.getInstance().getLanguageManager())
                        ),
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.chat"),
                                () -> new ChatOptionsScreen(currentScreen, o)
                        ),
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.resourcepack"),
                                () -> new PackSelectionScreen(Minecraft.getInstance().getResourcePackRepository(), packRepository -> {
                                    o.updateResourcePacks(packRepository);
                                    Minecraft.getInstance().setScreenAndShow(currentScreen);
                                }, Minecraft.getInstance().getResourcePackDirectory(), Component.translatable("resourcePack.title"))
                        ),
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.accessibility"),
                                () -> new AccessibilityOptionsScreen(currentScreen, o)
                        ))
                );

                Button telemetry = VoapiUtils.openScreenButton(
                        Component.translatable("options.telemetry"),
                        () -> new TelemetryInfoScreen(currentScreen, o)
                );
                if (!Minecraft.getInstance().allowsTelemetry()) {
                    telemetry.active = false;
                    telemetry.setTooltip(Tooltip.create(Component.translatable("options.telemetry.disabled")));
                }
                optionsList.addSmall(List.of(
                        telemetry,
                        VoapiUtils.openScreenButton(
                                Component.translatable("options.credits_and_attribution"),
                                () -> new CreditsAndAttributionScreen(currentScreen)
                        )
                ));
            };
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
}