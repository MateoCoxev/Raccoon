package net.coxev.raccoon.event;

import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface RaccoonChonkyEvent {
    Event<RaccoonChonkyEvent> EVENT = EventFactory.createArrayBacked(RaccoonChonkyEvent.class,
            (listeners) -> (player, raccoon) -> {
                for (RaccoonChonkyEvent listener : listeners) {
                    ActionResult result = listener.interact(player, raccoon);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, RaccoonEntity raccoon);
}
