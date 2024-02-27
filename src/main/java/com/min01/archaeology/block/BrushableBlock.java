package com.min01.archaeology.block;

import com.min01.archaeology.blockentity.BrushableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrushableBlock extends BaseEntityBlock implements Fallable {
   public static final IntegerProperty DUSTED = IntegerProperty.create("dusted", 0, 3);
   public static final int TICK_DELAY = 2;
   private final Block turnsInto;
   private final SoundEvent brushSound;
   private final SoundEvent brushCompletedSound;

   public BrushableBlock(final Block turnsInto, final BlockBehaviour.Properties properties, final SoundEvent brushSound, final SoundEvent brushCompletedSound) {
      super(properties);
      this.turnsInto = turnsInto;
      this.brushSound = brushSound;
      this.brushCompletedSound = brushCompletedSound;

      registerDefaultState(stateDefinition.any().setValue(DUSTED, 0));
   }

   protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(DUSTED);
   }
   
   @Override
   public @NotNull PushReaction getPistonPushReaction(@NotNull final BlockState state) {
	   return PushReaction.DESTROY;
   }

   public @NotNull RenderShape getRenderShape(@NotNull final BlockState state) {
      return RenderShape.MODEL;
   }

   public void onPlace(@NotNull final BlockState state, final Level level, @NotNull final BlockPos position, @NotNull final BlockState oldState, boolean isMoving) {
      level.scheduleTick(position, this, TICK_DELAY);
   }

   public @NotNull BlockState updateShape(@NotNull final BlockState state, @NotNull final Direction direction, @NotNull final BlockState neighborState, final LevelAccessor level, @NotNull final BlockPos currentPosition, @NotNull final BlockPos neighborPosition) {
      level.scheduleTick(currentPosition, this, TICK_DELAY);
      return super.updateShape(state, direction, neighborState, level, currentPosition, neighborPosition);
   }

   public void tick(@NotNull final BlockState state, final ServerLevel serverLevel, @NotNull final BlockPos position, @NotNull final RandomSource random) {
      if (serverLevel.getBlockEntity(position) instanceof BrushableBlockEntity brushableblockentity) {
         brushableblockentity.checkReset();
      }

      if (FallingBlock.isFree(serverLevel.getBlockState(position.below())) && position.getY() >= serverLevel.getMinBuildHeight()) {
         FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(serverLevel, position, state);
         fallingblockentity.dropItem = false;
      }
   }

   public void onBrokenAfterFall(final Level level, @NotNull final BlockPos position, final FallingBlockEntity fallingBlock) {
      Vec3 center = fallingBlock.getBoundingBox().getCenter();
      level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, new BlockPos(center), Block.getId(fallingBlock.getBlockState()));
      level.gameEvent(fallingBlock, GameEvent.BLOCK_DESTROY, center);
   }

   public void animateTick(@NotNull final BlockState state, @NotNull final Level level, @NotNull final BlockPos position, final RandomSource random) {
      if (random.nextInt(16) == 0) {
         if (FallingBlock.isFree(level.getBlockState(position.below()))) {
            double x = (double) position.getX() + random.nextDouble();
            double y = (double) position.getY() - 0.05D;
            double z = (double) position.getZ() + random.nextDouble();
            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), x, y, z, 0.0D, 0.0D, 0.0D);
         }
      }
   }

   public @Nullable BlockEntity newBlockEntity(@NotNull final BlockPos position, @NotNull final BlockState state) {
      return new BrushableBlockEntity(position, state);
   }

   public Block getTurnsInto() {
      return turnsInto;
   }

   public SoundEvent getBrushSound() {
      return brushSound;
   }

   public SoundEvent getBrushCompletedSound() {
      return brushCompletedSound;
   }
}