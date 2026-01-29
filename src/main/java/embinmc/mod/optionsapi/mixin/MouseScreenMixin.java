package embinmc.mod.optionsapi.mixin;

import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VoapiUtils;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.MouseSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MouseSettingsScreen.class)
public abstract class MouseScreenMixin {

    @Inject(method = "options", at = @At(value = "RETURN"), cancellable = true)
    private static void options_fix(final Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        VoapiUtils.basicSettingInjection(cir, OptionsMenuLocation.MOUSE);
    }
}
