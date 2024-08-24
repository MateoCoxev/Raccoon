package net.coxev.raccoon.entity.ai;

import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RaccoonBegGoal extends Goal {

    private static final TargetPredicate BEGGING_ENTITY_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(32D).ignoreVisibility();
    private final TargetPredicate predicate;
    protected final RaccoonEntity raccoon;
    private final double speed;
    @Nullable
    protected PlayerEntity closestPlayer;
    private int cooldown;
    private final Ingredient food;

    public RaccoonBegGoal(PathAwareEntity raccoon, double speed, Ingredient food) {
        this.raccoon = (RaccoonEntity) raccoon;
        this.speed = speed;
        this.food = food;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.predicate = BEGGING_ENTITY_PREDICATE.copy().setPredicate(this::isTemptedBy);
    }

    @Override
    public boolean canStart() {
        if (this.cooldown > 0) {
            --this.cooldown;
            return false;
        } else {
            this.closestPlayer = this.raccoon.getWorld().getClosestPlayer(this.predicate, this.raccoon);
            return this.closestPlayer != null && this.raccoon.getMainHandStack().isEmpty();
        }
    }

    private boolean isTemptedBy(LivingEntity entity) {
        return this.food.test(entity.getMainHandStack()) || this.food.test(entity.getOffHandStack());
    }

    @Override
    public boolean shouldContinue() {
        return this.raccoon.getMainHandStack().isEmpty() && this.canStart();
    }

    @Override
    public void stop() {
        this.closestPlayer = null;
        this.raccoon.getNavigation().stop();
        this.cooldown = toGoalTicks(100);
        this.raccoon.setBegging(false);
    }

    @Override
    public void tick() {
        this.raccoon.getLookControl().lookAt(this.closestPlayer, (float)(this.raccoon.getMaxLookYawChange()), (float)this.raccoon.getMaxLookPitchChange());
        if (this.raccoon.distanceTo(this.closestPlayer) < 3D) {
            this.raccoon.getNavigation().stop();
            this.raccoon.setBegging(true);
        } else {
            this.raccoon.setBegging(false);
            this.raccoon.getNavigation().startMovingTo(this.closestPlayer, this.speed);
        }
    }
}
