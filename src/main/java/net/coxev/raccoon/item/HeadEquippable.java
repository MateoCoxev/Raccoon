package net.coxev.raccoon.item;

import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class HeadEquippable implements EquipmentSlotProvider {
    @Override
    public EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
