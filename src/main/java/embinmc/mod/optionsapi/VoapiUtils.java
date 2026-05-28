package embinmc.mod.optionsapi;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.function.Supplier;

public final class VoapiUtils {
    public static void basicSettingInjection(CallbackInfoReturnable<OptionInstance<?>[]> cir, OptionsMenuLocation location) {
        List<Identifier> optionIds = VanillaOptionsAPI.getIdsForMenu(location);
        int arraySize = cir.getReturnValue().length + optionIds.size();
        cir.setReturnValue(VoapiUtils.convertListToArray(Util.make(new ArrayList<>(arraySize), list -> {
            var options = VanillaOptionsAPI.getOptionsMap();
            list.addAll(Arrays.asList(cir.getReturnValue()));
            for (Identifier optionId : optionIds) {
                Supplier<OptionInstance<?>> optionInstance = options.get(optionId);
                list.add(optionInstance.get());
            }
        })));
    }

    public static OptionInstance<?>[] convertListToArray(List<OptionInstance<?>> list) {
        return list.toArray(OptionInstance<?>[]::new);
    }

    public static String modNameElseId(String modId) {
        Optional<ModContainer> potentialMod = FabricLoader.getInstance()
                .getAllMods()
                .stream()
                .filter(modContainer -> modContainer.getMetadata().getId().equals(modId))
                .findAny();
        if (potentialMod.isPresent()) {
            ModContainer foundMod = potentialMod.orElseThrow();
            return foundMod.getMetadata().getName();
        }
        return modId;
    }

    public static Map<String, List<Supplier<OptionInstance<?>>>> optionsByNamespace() {
        return Util.make(LinkedHashMap.newLinkedHashMap(VanillaOptionsAPI.OPTIONS.size()), map -> {
            VanillaOptionsAPI.forEachOption((identifier, optionInstanceSupplier) -> {
                String namespace = identifier.getNamespace();
                if (!map.containsKey(namespace)) {
                    map.put(namespace, new ArrayList<>(12));
                }
                map.get(namespace).add(optionInstanceSupplier);
            });
        });
    }

    public static Map<String, List<Supplier<OptionInstance<?>>>> optionsByNamespaceInMenu(OptionsMenuLocation location) {
        return Util.make(LinkedHashMap.newLinkedHashMap(16), map -> {
            List<Identifier> optionsLocationMap = VanillaOptionsAPI.getIdsForMenu(location);
            VanillaOptionsAPI.forEachOption((identifier, optionInstanceSupplier) -> {
                String namespace = identifier.getNamespace();
                if (optionsLocationMap.contains(identifier)) {
                    if (!map.containsKey(namespace)) {
                        map.put(namespace, new ArrayList<>());
                    }
                    map.get(namespace).add(optionInstanceSupplier);
                }
            });
        });
    }

    public static Button openScreenButton(Component message, Supplier<Screen> toScreen) {
        Minecraft minecraft = Minecraft.getInstance();
        return Button.builder(message, (button) -> minecraft.setScreenAndShow(toScreen.get())).build();
    }
}
