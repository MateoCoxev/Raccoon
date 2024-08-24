package net.coxev.raccoon.sound;

import net.coxev.raccoon.Raccoon;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent RACCOON_IDLE = registerSoundEvent("raccoon_idle");
    public static final SoundEvent RACCOON_HURT = registerSoundEvent("raccoon_hurt");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(Raccoon.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Raccoon.LOGGER.info("Registering Sounds for " + Raccoon.MOD_ID);
    }
}
