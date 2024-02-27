package com.min01.archaeology.item;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.archaeology.block.BrushableBlock;
import com.min01.archaeology.blockentity.BrushableBlockEntity;
import com.min01.archaeology.init.ArchaeologyArmPose;
import com.min01.archaeology.init.ArchaeologySounds;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.ForgeEventFactory;

public class BrushItem extends Item {
    public static final int ANIMATION_DURATION = 10;
    private static final int USE_DURATION = 200;
    private static final double MAX_BRUSH_DISTANCE = Math.sqrt(ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE) - 1;

    public BrushItem(final Item.Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResult useOn(final UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(context.getHand());
        }

        return InteractionResult.CONSUME;
    }

    public @NotNull UseAnim getUseAnimation(final @NotNull ItemStack stack) {
        return UseAnim.CUSTOM; // UseAnim.BRUSH;
    }

    @Override
    public void initializeClient(final Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public boolean applyForgeHandTransform(final PoseStack pose, final LocalPlayer player, final HumanoidArm arm, final ItemStack stackInHand, float partialTick, float equipProcess, float swingProcess) {
                if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0) {
                    int i = arm == HumanoidArm.RIGHT ? 1 : -1;
                    pose.translate((float) i * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
                    float f = (float) (player.getUseItemRemainingTicks() % ANIMATION_DURATION);
                    float f1 = f - partialTick + 1.0F;
                    float f2 = 1.0F - f1 / 10.0F;
                    float f7 = -15.0F + 75.0F * Mth.cos(f2 * 2.0F * (float) Math.PI);

                    if (arm != HumanoidArm.RIGHT) {
                        pose.translate(0.1D, 0.83D, 0.35D);
                        pose.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
                        pose.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
                        pose.mulPose(Vector3f.XP.rotationDegrees(f7));
                        pose.translate(-0.3D, 0.22D, 0.35D);
                    } else {
                        pose.translate(-0.25D, 0.22D, 0.35D);
                        pose.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
                        pose.mulPose(Vector3f.YP.rotationDegrees(90.0F));
                        pose.mulPose(Vector3f.ZP.rotationDegrees(0.0F));
                        pose.mulPose(Vector3f.XP.rotationDegrees(f7));
                    }
                }

                return false;
            }

            @Override
            public @Nullable ArmPose getArmPose(final LivingEntity entity, final InteractionHand hand, final ItemStack stack) {
                return ArchaeologyArmPose.BRUSH;
            }
        });
    }

    public int getUseDuration(@NotNull final ItemStack stack) {
        return USE_DURATION;
    }

    public void onUseTick(final @NotNull Level level, final @NotNull LivingEntity entity, final @NotNull ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration >= 0 && entity instanceof Player player) {
            HitResult hitresult = this.calculateHitResult(entity);
            if (hitresult instanceof BlockHitResult blockhitresult) {
                if (hitresult.getType() == HitResult.Type.BLOCK) {
                    int useDuration = getUseDuration(stack) - remainingUseDuration + 1;

                    if (useDuration % ANIMATION_DURATION == 5) {
                        BlockPos position = blockhitresult.getBlockPos();
                        BlockState state = level.getBlockState(position);
                        HumanoidArm humanoidarm = entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();

                        spawnDustParticles(level, blockhitresult, state, entity.getViewVector(0), humanoidarm);
                        SoundEvent soundevent = state.getBlock() instanceof BrushableBlock brushable ? brushable.getBrushSound() : ArchaeologySounds.BRUSH_GENERIC.get();
                        level.playSound(player, position, soundevent, SoundSource.BLOCKS, 1, 1);

                        if (!level.isClientSide()) {
                            if (level.getBlockEntity(position) instanceof BrushableBlockEntity brushableEntity) {
                                if (brushableEntity.brush(level.getGameTime(), player, blockhitresult.getDirection())) {
                                    EquipmentSlot equipmentslot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                                    stack.hurtAndBreak(1, entity, breaker -> {
                                        breaker.broadcastBreakEvent(equipmentslot);
                                        ForgeEventFactory.onPlayerDestroyItem(player, stack, breaker.getUsedItemHand());
                                    });
                                }
                            }
                        }
                    }

                    return;
                }
            }

            entity.releaseUsingItem();
        } else {
            entity.releaseUsingItem();
        }
    }

    private HitResult calculateHitResult(final LivingEntity livingEntity) {
        return getHitResultOnViewVector(livingEntity, entity -> !entity.isSpectator() && entity.isPickable(), MAX_BRUSH_DISTANCE);
    }

    public static HitResult getHitResultOnViewVector(final Entity entity, final Predicate<Entity> filter, double distance) {
        Vec3 view = entity.getViewVector(0).scale(distance);
        return getHitResult(entity.getEyePosition(), entity, filter, view, entity.level);
    }

    private static HitResult getHitResult(final Vec3 from, final Entity entity, final Predicate<Entity> filter, final Vec3 view, final Level level) {
        Vec3 to = from.add(view);
        HitResult blockHitResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        if (blockHitResult.getType() != HitResult.Type.MISS) {
            to = blockHitResult.getLocation();
        }

        HitResult entityHitResult = ProjectileUtil.getEntityHitResult(level, entity, from, to, entity.getBoundingBox().expandTowards(view).inflate(1), filter);

        if (entityHitResult != null) {
            blockHitResult = entityHitResult;
        }

        return blockHitResult;
    }

    public void spawnDustParticles(final Level level, final BlockHitResult hitResult, final BlockState state, final Vec3 vector, final HumanoidArm arm) {
        double speedMultiplier = 3.0D;
        double min = 0.000001;

        int armType = arm == HumanoidArm.RIGHT ? 1 : -1;
        int particleAmount = level.getRandom().nextInt(7, 12);
        BlockParticleOption blockparticleoption = new BlockParticleOption(ParticleTypes.BLOCK, state);
        Direction direction = hitResult.getDirection();
        BrushItem.DustParticlesDelta particleDelta = BrushItem.DustParticlesDelta.fromDirection(vector, direction);
        Vec3 location = hitResult.getLocation();

        for (int i = 0; i < particleAmount; i++) {
            double x = location.x - (direction == Direction.WEST ? min : 0);
            double z = location.z - (direction == Direction.NORTH ? min : 0);
            double xSpeed = particleDelta.xd() * (double) armType * speedMultiplier * level.getRandom().nextDouble();
            double zSpeed = particleDelta.zd() * (double) armType * speedMultiplier * level.getRandom().nextDouble();
            level.addParticle(blockparticleoption, x, location.y, z, xSpeed, 0, zSpeed);
        }

    }

    record DustParticlesDelta(double xd, double yd, double zd) {
        private static final double ALONG_SIDE_DELTA = 1.0D;
        private static final double OUT_FROM_SIDE_DELTA = 0.1D;

        public static BrushItem.DustParticlesDelta fromDirection(final Vec3 vector, final Direction direction) {
            return switch (direction) {
                case DOWN, UP -> new DustParticlesDelta(vector.z(), 0, -vector.x());
                case NORTH -> new DustParticlesDelta(ALONG_SIDE_DELTA, 0, -OUT_FROM_SIDE_DELTA);
                case SOUTH -> new DustParticlesDelta(-ALONG_SIDE_DELTA, 0, OUT_FROM_SIDE_DELTA);
                case WEST -> new DustParticlesDelta(-OUT_FROM_SIDE_DELTA, 0, -ALONG_SIDE_DELTA);
                case EAST -> new DustParticlesDelta(OUT_FROM_SIDE_DELTA, 0, ALONG_SIDE_DELTA);
            };
        }
    }
}