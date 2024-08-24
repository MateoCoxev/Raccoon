package net.coxev.raccoon.entity.custom;

import net.coxev.raccoon.advancement.ModAdvancements;
import net.coxev.raccoon.entity.ModEntities;
import net.coxev.raccoon.entity.ai.RaccoonBegGoal;
import net.coxev.raccoon.entity.ai.RaccoonStealGoal;
import net.coxev.raccoon.item.ModItems;
import net.coxev.raccoon.sound.ModSounds;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EntityView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RaccoonEntity extends TameableEntity implements Angerable {

    private static final TrackedData<Boolean> IS_CHONKY = DataTracker.registerData(RaccoonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(RaccoonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> BEGGING = DataTracker.registerData(RaccoonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> STEALING = DataTracker.registerData(RaccoonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> BANDANA_COLOR = DataTracker.registerData(RaccoonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(RaccoonEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    @Nullable
    private UUID angryAt;

    public boolean willGrowChonky = false;
    public PlayerEntity playerThatChonkedIt;

    public int eatingTime;
    public int stealCooldown = 0;

    public final AnimationState idleAnimationState = new AnimationState();

    public final AnimationState spinningAnimationState = new AnimationState();

    public final AnimationState beggingAnimationState = new AnimationState();
    private int beggingAnimationTimeout = 0;
    private boolean begged = false;

    public final AnimationState sittingAnimationState = new AnimationState();
    private int sittingAnimationTimeout = 0;
    private boolean sat = false;

    public final AnimationState standToBegAnimationState = new AnimationState();

    public final AnimationState standToSitAnimationState = new AnimationState();

    public final AnimationState begToStandAnimationState = new AnimationState();

    public final AnimationState sitToStandAnimationState = new AnimationState();

    public final AnimationState earsWiggleAnimationState = new AnimationState();
    private int earsWiggleAnimationTimeout = 0;

    public final AnimationState twerkAnimationState = new AnimationState();

    private boolean songPlaying;
    private BlockPos songSource;

    public RaccoonEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false);
    }

    private void setupAnimationStates() {
        if(this.hasCustomName() && this.getCustomName().getString().equalsIgnoreCase("pedro")){
            this.spinningAnimationState.startIfNotRunning(this.age);
        }
        this.idleAnimationState.startIfNotRunning(this.age);
        if(this.earsWiggleAnimationTimeout <= 0){
            this.earsWiggleAnimationTimeout = this.random.nextInt(80) + 160;
            this.earsWiggleAnimationState.start(this.age);
        } else {
            --this.earsWiggleAnimationTimeout;
        }

        if (this.isBegging()) {
            this.begged = true;
            if(this.beggingAnimationTimeout <= 0){
                this.beggingAnimationTimeout = 11;
                this.begToStandAnimationState.stop();
                this.standToBegAnimationState.start(this.age);
            } else if(this.beggingAnimationTimeout == 1){
                this.standToBegAnimationState.stop();
                this.beggingAnimationState.startIfNotRunning(this.age);
            } else {
                --beggingAnimationTimeout;
            }
        } else if (this.begged) {
            this.standToBegAnimationState.stop();
            this.beggingAnimationState.stop();
            this.begToStandAnimationState.startIfNotRunning(this.age);
            this.beggingAnimationTimeout = 0;
            this.begged = false;
        }

        if (this.isSitting()) {
            this.sat = true;
            if(this.sittingAnimationTimeout <= 0){
                this.sittingAnimationTimeout = 11;
                this.sitToStandAnimationState.stop();
                this.standToSitAnimationState.start(this.age);
            } else if(this.sittingAnimationTimeout == 1){
                this.standToSitAnimationState.stop();
                this.sittingAnimationState.startIfNotRunning(this.age);
            } else {
                --sittingAnimationTimeout;
            }
        } else if (this.sat) {
            this.standToSitAnimationState.stop();
            this.sittingAnimationState.stop();
            this.sitToStandAnimationState.startIfNotRunning(this.age);
            this.sittingAnimationTimeout = 0;
            this.sat = false;
        }

        if(!this.isStealing() && !this.isBegging() && !this.isSitting() && this.songPlaying){
            this.twerkAnimationState.startIfNotRunning(this.age);
        } else if(!this.songPlaying){
            this.twerkAnimationState.stop();
        }
    }

    @Override
    protected void updateLimbs(float posDelta) {
        float f = this.getPose() == EntityPose.STANDING ? Math.min(posDelta * 6.0f, 1.0f) : 0.0f;
        this.limbAnimator.updateLimbs(f, 0.2f);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    public void setSitting(boolean sitting){
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isBegging() {
        return this.dataTracker.get(BEGGING);
    }

    public void setBegging(boolean begging) {
        this.dataTracker.set(BEGGING, begging);
    }

    public boolean isStealing() {
        return this.dataTracker.get(STEALING);
    }

    public void setStealing(boolean stealing) {
        this.dataTracker.set(STEALING, stealing);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getWorld().isClient()) {
            setupAnimationStates();
        }
        if(stealCooldown > 0){
            --stealCooldown;
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5));

        this.goalSelector.add(2, new SitGoal(this));

        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, true));

        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.3, 10.0f, 2.0f, false));

        this.goalSelector.add(5, new RaccoonBegGoal(this, 1.2D, Ingredient.fromTag(ConventionalItemTags.FOODS)));
        this.goalSelector.add(6, new RaccoonStealGoal(this, 1.5));

        this.goalSelector.add(7, new AnimalMateGoal(this, 1.150));

        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 8f, 1));
        this.goalSelector.add(10, new LookAroundGoal(this));

        this.targetSelector.add(0, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(2, new UniversalAngerGoal<>(this, true));
    }

    public static DefaultAttributeContainer.Builder createRaccoonAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f);
    }

    public DyeColor getBandanaColor() {
        return DyeColor.byId((Integer)this.dataTracker.get(BANDANA_COLOR));
    }

    public void setBandanaColor(DyeColor color) {
        this.dataTracker.set(BANDANA_COLOR, color.getId());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IS_CHONKY, false);
        this.dataTracker.startTracking(SITTING, false);
        this.dataTracker.startTracking(BEGGING, false);
        this.dataTracker.startTracking(STEALING, false);
        this.dataTracker.startTracking(BANDANA_COLOR, DyeColor.BLUE.getId());
        this.dataTracker.startTracking(ANGER_TIME, 0);
    }

    public void setChonky(boolean isChonky) {
        this.dataTracker.set(IS_CHONKY, isChonky);
        this.refreshPosition();
        this.calculateDimensions();
    }

    public boolean isChonky() {
        return this.dataTracker.get(IS_CHONKY);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Chonky", this.isChonky());
        nbt.putByte("BandanaColor", (byte)this.getBandanaColor().getId());
        this.writeAngerToNbt(nbt);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setChonky(nbt.getBoolean("Chonky"));
        if (nbt.contains("BandanaColor", 99)) {
            this.setBandanaColor(DyeColor.byId(nbt.getInt("BandanaColor")));
        }
        this.readAngerFromNbt(this.getWorld(), nbt);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.APPLE);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.RACCOON.create(world);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20.0);
            this.setHealth(20.0f);
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0);
        }
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Random random = world.getRandom();
        this.setChonky(random.nextInt(20) < 1);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void tickMovement() {
        if (this.songSource == null || !this.songSource.isWithinDistance(this.getPos(), 3.46) || !this.getWorld().getBlockState(this.songSource).isOf(Blocks.JUKEBOX)) {
            this.songPlaying = false;
            this.songSource = null;
        }
        if (!this.getWorld().isClient && this.isAlive()) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()) {
                ++this.eatingTime;
                if (this.eatingTime > 60 /*600*/) {
                    ItemStack itemStack2 = itemStack.finishUsing(this.getWorld(), this);
                    if (!itemStack2.isEmpty()) {
                        this.equipStack(EquipmentSlot.MAINHAND, itemStack2);
                    }
                    if(willGrowChonky) {
                        if(this.playerThatChonkedIt instanceof ServerPlayerEntity){
                            ModAdvancements.CHONKED.trigger((ServerPlayerEntity) this.playerThatChonkedIt);
                        }
                        this.setChonky(true);
                    }
                    this.eatingTime = 0;
                } else if (this.eatingTime > 20 /*560*/ && this.random.nextFloat() < 0.1f) {
                    this.playSound(this.getEatSound(itemStack), 1.0f, 1.0f);
                    this.getWorld().sendEntityStatus(this, EntityStatuses.CREATE_EATING_PARTICLES);
                }
            }
        }

        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld)this.getWorld(), true);
        }
        super.tickMovement();
    }

    @Override
    public void setNearbySongPlaying(BlockPos songPosition, boolean playing) {
        this.songSource = songPosition;
        this.songPlaying = playing;
    }

    public boolean isSongPlaying() {
        return this.songPlaying;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.CREATE_EATING_PARTICLES) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!itemStack.isEmpty()) {
                for (int i = 0; i < 8; ++i) {
                    Vec3d vec3d = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0).rotateX(-MathHelper.clamp(this.getPitch(), -20.0f, 20.0f)).rotateY(-MathHelper.clamp(headYaw, -20.0f, 20.0f));
                    this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), this.getX() + this.getRotationVector().x / 2.0, this.getY(), this.getZ() + this.getRotationVector().z / 2.0 - 1, vec3d.x, vec3d.y + 0.05, vec3d.z);
                }
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if(this.getWorld().isClient()){
            if (this.isTamed() && this.isOwner(player)) {
                return ActionResult.SUCCESS;
            } else {
                return !this.isBreedingItem(itemStack) || !(this.getHealth() < this.getMaxHealth()) && this.isTamed() ? ActionResult.PASS : ActionResult.SUCCESS;
            }
        } else {
            ActionResult actionResult;
            if(player.isSneaking() && this.getEquippedStack(EquipmentSlot.HEAD).isOf(ModItems.HAT_AND_GLASSES)){
                this.getEquippedStack(EquipmentSlot.HEAD).decrement(1);
                if(!player.isCreative()){
                    player.giveItemStack(ModItems.HAT_AND_GLASSES.getDefaultStack());
                }
                return ActionResult.SUCCESS;
            } else {
                if(this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty() && ofTagFoods(itemStack) && !this.isBreedingItem(itemStack)) {
                    this.eat(player, hand, itemStack);
                    this.setStackInHand(Hand.MAIN_HAND, item.getDefaultStack());
                    if(Math.random() * 100 < 1){
                        this.willGrowChonky = true;
                        this.playerThatChonkedIt = player;
                    }
                    return ActionResult.SUCCESS;
                } else if (this.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && item == ModItems.HAT_AND_GLASSES){
                    this.eat(player, hand, itemStack);
                    this.equipStack(EquipmentSlot.HEAD, item.getDefaultStack());
                    return ActionResult.SUCCESS;
                } else if(this.isTamed()){
                    if(this.isOwner(player)){
                        if (!(item instanceof DyeItem)) {
                            if (item.isFood() && this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                                this.eat(player, hand, itemStack);
                                this.heal((float)item.getFoodComponent().getHunger());
                                return ActionResult.CONSUME;
                            }

                            actionResult = super.interactMob(player, hand);
                            if (!actionResult.isAccepted() || this.isBaby()) {
                                this.setSitting(!this.isSitting());
                            }

                            return actionResult;
                        }

                        DyeColor dyeColor = ((DyeItem)item).getColor();
                        if (dyeColor != this.getBandanaColor()) {
                            this.setBandanaColor(dyeColor);
                            if (!player.getAbilities().creativeMode) {
                                itemStack.decrement(1);
                            }

                            this.setPersistent();
                            return ActionResult.CONSUME;
                        }
                    }
                } else if (this.isBreedingItem(itemStack)) {
                    this.eat(player, hand, itemStack);
                    if (this.random.nextInt(3) == 0) {
                        this.setOwner(player);
                        this.setSitting(true);
                        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                    } else {
                        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
                    }

                    this.setPersistent();
                    return ActionResult.CONSUME;
                }

                actionResult = super.interactMob(player, hand);
                if (actionResult.isAccepted()) {
                    this.setPersistent();
                }

                return actionResult;
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(this.getDamageSources().mobAttack(this), (int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (bl) {
            this.applyDamageEffects(this, target);
        }
        return bl;
    }

    public void onEatItem() {
        this.heal(10);
        this.getWorld().sendEntityStatus(this, (byte) 92);
        this.emitGameEvent(GameEvent.EAT);
        this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getPitch());
    }


    public static boolean ofTagFoods(ItemStack stack) {
        return stack.isIn(ConventionalItemTags.FOODS);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.RACCOON_IDLE;
    }

    @Override
    public float getSoundPitch() {
        if(this.isBaby()) {
            return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.5f;
        } else if(isChonky()){
            return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.7f;
        }
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.RACCOON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.RACCOON_HURT;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public EntityView method_48926() {
        return super.getWorld();
    }

    @Override
    public int getAngerTime() {
        return this.dataTracker.get(ANGER_TIME);
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.dataTracker.set(ANGER_TIME, angerTime);
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }
}
