package embinmc.mod.optionsapi;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VanillaOptionsAPI implements ModInitializer {
    public static final String MOD_ID = "optionsapi";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static final OptionInstance<Boolean> TEST_SETTING = OptionInstance.createBoolean("voapi_test", false);
    public static final Map<Identifier, Supplier<OptionInstance<?>>> OPTIONS = HashMap.newHashMap(32);
    public static final Map<OptionsMenuLocation, List<Identifier>> OPTIONS_MENU = Util.make(HashMap.newHashMap(32), map -> {
        for (OptionsMenuLocation menuLocation : OptionsMenuLocation.values()) {
            map.put(menuLocation, new ArrayList<>(8));
        }
    });

    public static Identifier register(Identifier identifier, OptionsMenuLocation menu, Supplier<OptionInstance<?>> supplier) {
        VanillaOptionsAPI.OPTIONS.put(identifier, supplier);
        VanillaOptionsAPI.OPTIONS_MENU.get(menu).add(identifier);
        return identifier;
    }

    public static OptionInstance<?>[] convertListToArray(List<OptionInstance<?>> list) {
        final var array = new OptionInstance<?>[list.size()];
        int index = 0;
        for (OptionInstance<?> optionInstance : list) {
            array[index] = optionInstance;
            index++;
        }
        return array;
    }

    @Override
    public void onInitialize() {
        VanillaOptionsAPI.register(Identifier.fromNamespaceAndPath(MOD_ID, "test"), OptionsMenuLocation.ACCESSIBILITY, VanillaOptionsAPI::testSetting);
    }

    private static OptionInstance<Boolean> testSetting() {
        return VanillaOptionsAPI.TEST_SETTING;
    }
}