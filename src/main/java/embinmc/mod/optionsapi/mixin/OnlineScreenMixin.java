package embinmc.mod.optionsapi.mixin;

import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VoapiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OnlineOptionsScreen.class)
public abstract class OnlineScreenMixin {

    @Inject(method = "options", at = @At(value = "RETURN"), cancellable = true)
    private void options_fix(Options options, Minecraft minecraft, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        VoapiUtils.basicSettingInjection(cir, OptionsMenuLocation.ONLINE);
    }
}
