package embinmc.mod.optionsapi.mixin;

import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import embinmc.mod.optionsapi.VoapiUtils;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(SkinCustomizationScreen.class)
public abstract class SkinScreenMixin extends OptionsSubScreen {

    private SkinScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "addOptions", at = @At(value = "TAIL"))
    private void options_fix(CallbackInfo ci) {
        List<Identifier> optionIds = VanillaOptionsAPI.getIdsForMenu(OptionsMenuLocation.SKIN);
        this.list.addSmall(VoapiUtils.convertListToArray(Util.make(new ArrayList<>(optionIds.size()), list -> {
            var options = VanillaOptionsAPI.getOptionsMap();
            for (Identifier optionId : optionIds) {
                Supplier<OptionInstance<?>> optionInstance = options.get(optionId);
                list.add(optionInstance.get());
            }
        })));
    }
}
