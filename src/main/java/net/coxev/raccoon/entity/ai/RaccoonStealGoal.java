package net.coxev.raccoon.entity.ai;

import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.coxev.raccoon.util.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class RaccoonStealGoal extends Goal {
    private final RaccoonEntity raccoon;
    private final double escapeSpeed;
    private double initialX;
    private double initialY;
    private double initialZ;

    public BlockPos getClosestTrashBin(int distance){
        BlockPos closestPos = null;
        int raccoonX = MathHelper.floor(this.raccoon.getX());
        int raccoonY = MathHelper.floor(this.raccoon.getY());
        int raccoonZ = MathHelper.floor(this.raccoon.getZ());

        for (int x = raccoonX - distance; x < raccoonX + distance; x++) {
            for (int y = raccoonY - distance; y < raccoonY + distance; y++) {
                for (int z = raccoonZ - distance; z < raccoonZ + distance; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    if (this.raccoon.getWorld().getBlockState(checkPos).isIn(ModTags.Blocks.TRASH_CANS) || this.raccoon.getWorld().getBlockState(checkPos).isOf(Blocks.COMPOSTER)) {
                        if (closestPos == null ||
                                this.raccoon.squaredDistanceTo(raccoonX - checkPos.getX(),
                                        raccoonY - checkPos.getY(),
                                        raccoonZ - checkPos.getZ())
                                        < this.raccoon.squaredDistanceTo(raccoonX - closestPos.getX(),
                                        raccoonY - closestPos.getY(),
                                        raccoonZ - closestPos.getZ())) {
                            closestPos = checkPos;
                        }
                    }
                }
            }
        }
        return closestPos;
    }

    public ItemStack randomItem(){
        List<ItemStack> items = new ArrayList<>();
        items.add(Items.APPLE.getDefaultStack());
        items.add(Items.BREAD.getDefaultStack());
        items.add(Items.COOKIE.getDefaultStack());
        Random randomizer = new Random();
        return items.get(randomizer.nextInt(items.size()));
    }

    public RaccoonStealGoal(RaccoonEntity raccoon, double escapeSpeed) {
        this.raccoon = raccoon;
        this.escapeSpeed = escapeSpeed;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return getClosestTrashBin(10) != null && this.raccoon.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty() && this.raccoon.stealCooldown == 0;
    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

    @Override
    public void start() {
        this.initialX = this.raccoon.getX();
        this.initialY = this.raccoon.getY();
        this.initialZ = this.raccoon.getZ();
    }

    @Override
    public void tick() {
        BlockPos blockPos = getClosestTrashBin(10);
        if (blockPos != null){
            if(this.raccoon.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > 3) {
                this.raccoon.getNavigation().startMovingTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1);
            } else {
                this.raccoon.setStealing(true);
                this.raccoon.equipStack(EquipmentSlot.MAINHAND, randomItem());
                this.raccoon.getNavigation().startMovingTo(initialX - (blockPos.getX() - initialX), initialY, initialZ - (blockPos.getZ() - initialZ), escapeSpeed);
                this.raccoon.stealCooldown = (this.raccoon.getRandom().nextInt(1200)) + 1200;
            }
        }
    }
}
