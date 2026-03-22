package embinmc.mod.optionsapi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.function.Supplier;

public class AllCustomOptionsScreen extends OptionsSubScreen {
    public static final Component BUTTON = Component.translatable("options.vanillaoptionsapi.all_options");
    public static final Component TITLE = Component.translatable("options.vanillaoptionsapi.all_options.title");

    public AllCustomOptionsScreen(Screen lastScreen) {
        super(lastScreen, Minecraft.getInstance().options, TITLE);
    }

    @Override
    protected void addOptions() {
        this.withLayouts(VanillaOptionsAPI.MENU_LAYOUTS);
    }

    @Override
    public void tick() {
        super.tick();
        this.withLayouts(VanillaOptionsAPI.ON_TICK);
    }

    public void withLayouts(Map<String, VanillaOptionsAPI.MenuLayout> menuLayouts) {
        Map<String, List<Supplier<OptionInstance<?>>>> optionByNamespace = VoapiUtils.optionsByNamespace();
        menuLayouts.forEach((namespace, menuLayout) -> {
            List<Supplier<OptionInstance<?>>> options = optionByNamespace.get(namespace);
            menuLayout.consume(this, this.list, options != null ? options : List.of());
        });
    }
}
