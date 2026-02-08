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
    private static final Component TITLE = Component.translatable("options.vanillaoptionsapi.all_options.title");

    public AllCustomOptionsScreen(Screen lastScreen) {
        super(lastScreen, Minecraft.getInstance().options, TITLE);
    }

    @Override
    protected void addOptions() {
        Map<String, List<Supplier<OptionInstance<?>>>> optionByNamespace = VoapiUtils.optionsByNamespace();
        optionByNamespace.forEach((namespace, options) -> {
            this.list.addHeader(Component.literal(VoapiUtils.modNameElseId(namespace)));
            this.list.addSmall(options.stream().map(Supplier::get).toArray(OptionInstance<?>[]::new));
        });
    }
}
