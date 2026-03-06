package embinmc.mod.optionsapi.mixin;

import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import embinmc.mod.optionsapi.VoapiUtils;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.MouseSettingsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MouseSettingsScreen.class)
public abstract class MouseScreenMixin extends OptionsSubScreen {
    private MouseScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "addOptions", at = @At(value = "TAIL"))
    private void options_fix(CallbackInfo ci) {
        var options = VanillaOptionsAPI.getOptionsMap();
        VanillaOptionsAPI.getIdsForMenu(OptionsMenuLocation.MOUSE).forEach(identifier -> this.list.addSmall(options.get(identifier).get()));
    }
}
