package net.coxev.raccoon.advancement;

import net.minecraft.advancement.criterion.Criteria;

public class ModAdvancements {

    public static ChonkyCriterion CHONKED = new ChonkyCriterion();

    public static void registerCriterions() {
        Criteria.register(CHONKED);
    }

}
