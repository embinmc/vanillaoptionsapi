package embinmc.mod.optionsapi;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum OptionsMenuLocation implements StringRepresentable {

    /**
     * Settings will still appear in the "Mod Options" menu.
     */
    NONE("none"),

    ACCESSIBILITY("accessibility"), CHAT("chat"),

    /**
     * Settings will appear BEFORE the vanilla "Raw Input" mouse setting.
     */
    MOUSE("mouse"),

    SKIN("skin"), SOUNDS("sounds"),
    CONTROLS("controls"), ONLINE("online"),

    /**
     * Settings will appear under a sub category with the name of the mod that registered it.
     * <p>The category's name will fall back to the setting's namespace if there is no mod with that id.
     */
    VIDEO("video");

    public final String name;

    public static final Codec<OptionsMenuLocation> CODEC = StringRepresentable.fromEnum(OptionsMenuLocation::values);

    OptionsMenuLocation(String name) {
        this.name = name;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }
}
