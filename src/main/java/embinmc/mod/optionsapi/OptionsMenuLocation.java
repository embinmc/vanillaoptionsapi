package embinmc.mod.optionsapi;

public enum OptionsMenuLocation {
    ACCESSIBILITY, CHAT,

    /**
     * Settings will still appear in the "Mod Options" menu.
     */
    NONE,

    /**
     * Settings will appear BEFORE the vanilla "Raw Input" mouse setting.
     */
    MOUSE,

    SKIN, SOUNDS, CONTROLS, ONLINE,

    /**
     * Settings will appear under a sub category with the name of the mod that registered it.
     * <p>The category's name will fall back to the setting's namespace if there is no mod with that id.
     */
    VIDEO
}
