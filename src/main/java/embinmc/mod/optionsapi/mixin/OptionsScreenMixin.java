package embinmc.mod.optionsapi.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.optionsapi.AllCustomOptionsScreen;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin {
    @Shadow
    protected abstract Button openScreenButton(Component message, Supplier<Screen> screenToScreen);

    //@Local(name = "helper", type = GridLayout.RowHelper.class)
    @Inject(method = "init", at = @At(
            value = "INVOKE",
            ordinal = 9, // credits and attribution
            target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",
            shift = At.Shift.AFTER
    ), locals = LocalCapture.CAPTURE_FAILHARD)
    private void voapi_addNewOptionButton(CallbackInfo ci, LinearLayout header, LinearLayout subHeader, GridLayout gridLayout, GridLayout.RowHelper helper, Button telemetryButton) {
        if (!VanillaOptionsAPI.getOptionsMap().isEmpty()) {
            OptionsScreen myself = (OptionsScreen)(Object)this;
            helper.addChild(this.openScreenButton(AllCustomOptionsScreen.BUTTON, () -> new AllCustomOptionsScreen(myself)));
        }
    }
}
