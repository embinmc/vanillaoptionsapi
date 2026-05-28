package embinmc.mod.optionsapi;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.function.Supplier;

public class OptionUtil {
    public static Component percentValueLabel(final Component caption, final double value) {
        return Component.translatable("options.percent_value", caption, (int) (value * 100D));
    }

    public static <T> OptionInstance<T> enumOption(String captionId, OptionInstance.TooltipSupplier<T> tooltipSupplier, OptionInstance.CaptionBasedToString<T> toString, Supplier<T[]> values, Codec<T> codec, T _default, OptionInstance.ValueUpdateListener<T> onUpdate) {
        return new OptionInstance<>(captionId, tooltipSupplier, toString, new OptionInstance.Enum<>(Arrays.asList(values.get()), codec), _default, onUpdate);
    }

    public static <T> OptionInstance<T> enumOption(String captionId, OptionInstance.CaptionBasedToString<T> toString, Supplier<T[]> values, Codec<T> codec, T _default, OptionInstance.ValueUpdateListener<T> onUpdate) {
        return OptionUtil.enumOption(captionId, OptionInstance.noTooltip(), toString, values, codec, _default, onUpdate);
    }

    public static <T> OptionInstance<T> enumOption(String captionId, OptionInstance.CaptionBasedToString<T> toString, Supplier<T[]> values, Codec<T> codec, T _default) {
        return OptionUtil.enumOption(captionId, toString, values, codec, _default, _ -> {});
    }

    public static <T> OptionInstance<T> enumSlider(String captionId, OptionInstance.TooltipSupplier<T> tooltipSupplier, OptionInstance.CaptionBasedToString<T> toString, Supplier<T[]> values, Codec<T> codec, T _default, OptionInstance.ValueUpdateListener<T> onUpdate) {
        return new OptionInstance<>(captionId, tooltipSupplier, toString, new OptionInstance.SliderableEnum<>(Arrays.asList(values.get()), codec), _default, onUpdate);
    }

    public static <T> OptionInstance<T> enumSlider(String captionId, OptionInstance.CaptionBasedToString<T> toString, Supplier<T[]> values, Codec<T> codec, T _default, OptionInstance.ValueUpdateListener<T> onUpdate) {
        return OptionUtil.enumSlider(captionId, OptionInstance.noTooltip(), toString, values, codec, _default, onUpdate);
    }

    public static <T> OptionInstance<T> enumSlider(String captionId, OptionInstance.CaptionBasedToString<T> toString, Supplier<T[]> values, Codec<T> codec, T _default) {
        return OptionUtil.enumSlider(captionId, toString, values, codec, _default, _ -> {});
    }
}
