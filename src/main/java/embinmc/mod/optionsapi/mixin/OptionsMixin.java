package embinmc.mod.optionsapi.mixin;

import embinmc.mod.optionsapi.VanillaOptionsAPI;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class OptionsMixin {

	@Inject(method = "processOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options$FieldAccess;process(Ljava/lang/String;Lnet/minecraft/client/OptionInstance;)V", ordinal = 4))
	private void process(final Options.FieldAccess access, CallbackInfo info) {
        VanillaOptionsAPI.OPTIONS.forEach((identifier, optionInstanceSupplier) -> {
            String formattedKey = identifier.toLanguageKey();
            access.process(formattedKey, optionInstanceSupplier.get());
        });
	}
}