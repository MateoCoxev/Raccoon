package net.coxev.raccoon.advancement;

import com.google.gson.JsonObject;
import net.coxev.raccoon.Raccoon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ChonkyCriterion extends AbstractCriterion<ChonkyCriterion.Conditions> {
    static final Identifier ID = new Identifier(Raccoon.MOD_ID + "/chonked");

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, Conditions::requirementsMet);
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(playerPredicate);
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(LootContextPredicate player) {
            super(ID, player);
        }

        public static Conditions create() {
            return new Conditions(LootContextPredicate.EMPTY);
        }

        public boolean requirementsMet() {
            return true;
        }
    }
}
