package embinmc.mod.optionsapi.mixin;

import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

@Mixin(AccessibilityOptionsScreen.class)
public abstract class AccessibilityScreenMixin {

    @Inject(method = "options", at = @At(value = "RETURN"), cancellable = true)
    private static void options_fix(final Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        cir.setReturnValue(VanillaOptionsAPI.convertListToArray(Util.make(new ArrayList<>(), list -> {
            list.addAll(Arrays.asList(cir.getReturnValue()));
            for (Identifier optionId : VanillaOptionsAPI.OPTIONS_MENU.get(OptionsMenuLocation.ACCESSIBILITY)) {
                Supplier<OptionInstance<?>> optionInstance = VanillaOptionsAPI.OPTIONS.get(optionId);
                list.add(optionInstance.get());
            }
        })));
    }
}
