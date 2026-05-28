package embinmc.mod.optionsapi.mixin;

import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import embinmc.mod.optionsapi.VoapiUtils;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoScreenMixin extends OptionsSubScreen {

    private VideoScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "addOptions", at = @At(value = "TAIL"))
    private void options_fix(CallbackInfo ci) {
        List<Identifier> optionIds = VanillaOptionsAPI.getIdsForMenu(OptionsMenuLocation.VIDEO);
        if (!optionIds.isEmpty() && this.list != null) {
            Map<String, List<Supplier<OptionInstance<?>>>> options = VoapiUtils.optionsByNamespaceInMenu(OptionsMenuLocation.VIDEO);
            options.forEach((namespace, optionSuppliers) -> {
                this.list.addHeader(Component.literal(VoapiUtils.modNameElseId(namespace)));
                this.list.addSmall(optionSuppliers.stream().map(Supplier::get).toArray(OptionInstance<?>[]::new));
            });
        }
    }
}
