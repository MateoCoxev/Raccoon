package net.coxev.raccoon.entity.ai;

import net.coxev.raccoon.block.ModBlocks;
import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class RaccoonTwerkGoal extends Goal {
    private final RaccoonEntity raccoon;

    public RaccoonTwerkGoal(RaccoonEntity raccoon) {
        this.raccoon = raccoon;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
