package com.min01.archaeology.block;

import com.min01.archaeology.blockentity.DecoratedPotBlockEntity;
import com.min01.archaeology.init.ArchaelogyTags;
import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyParticleTypes;
import com.min01.archaeology.init.ArchaeologySounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class DecoratedPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final ResourceLocation SHERDS_DYNAMIC_DROP_ID = new ResourceLocation("sherds");
    private static final VoxelShape BOUNDING_BOX = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final BooleanProperty CRACKED = BooleanProperty.create("cracked");
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public DecoratedPotBlock(final BlockBehaviour.Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(CRACKED, Boolean.FALSE));
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction(@NotNull final BlockState state) {
        return PushReaction.DESTROY;
    }

    public @NotNull BlockState updateShape(final BlockState state, @NotNull final Direction direction, @NotNull final BlockState neighborState, @NotNull final LevelAccessor level, @NotNull final BlockPos currentPosition, @NotNull final BlockPos neighborPosition) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPosition, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPosition, neighborPosition);
    }

    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                .setValue(CRACKED, Boolean.FALSE);
    }

    @Override
    public @NotNull InteractionResult use(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos position, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull BlockHitResult hitResult) {
        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getItemInHand(hand);
            InteractionResult result = handleWithItem(level, position, player, stack);

            if (result == null) {
                return handleWithoutItem(level, position, player);
            }

            return result;
        } else {
            return super.use(state, level, position, player, hand, hitResult);
        }
    }

    private @NotNull InteractionResult handleWithoutItem(final @NotNull Level level, final @NotNull BlockPos position, final @NotNull Player player) {
        if (level.getBlockEntity(position) instanceof DecoratedPotBlockEntity potEntity) {
            level.playSound(null, position, ArchaeologySounds.DECORATED_POT_INSERT_FAIL.get(), SoundSource.BLOCKS, 1, 1);
            potEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, position);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    private @Nullable InteractionResult handleWithItem(final @NotNull Level level, final @NotNull BlockPos position, final @NotNull Player player, final @NotNull ItemStack stack) {
        if (level.getBlockEntity(position) instanceof DecoratedPotBlockEntity potEntity) {
            if (level.isClientSide()) {
                return InteractionResult.CONSUME;
            }

            ItemStack potStack = potEntity.getTheItem();

            if (!stack.isEmpty() && (potStack.isEmpty() || ItemStack.isSameItemSameTags(stack, potStack) && potStack.getCount() < potStack.getMaxStackSize())) {
                potEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.POSITIVE);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                ItemStack newPotStack = player.isCreative() ? new ItemStack(stack.getItem(), 1) : stack.split(1);
                float intensity;

                if (potEntity.isEmpty()) {
                    potEntity.setTheItem(newPotStack);
                    intensity = (float) newPotStack.getCount() / newPotStack.getMaxStackSize();
                } else {
                    potStack.grow(1);
                    intensity = (float) potStack.getCount() / potStack.getMaxStackSize();
                }

                level.playSound(null, position, ArchaeologySounds.DECORATED_POT_INSERT.get(), SoundSource.BLOCKS, 1, 0.7f + 0.5f * intensity);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ArchaeologyParticleTypes.DUST_PLUME.get(), position.getX() + 0.5, position.getY() + 1.2, position.getZ() + 0.5, 7, 0, 0, 0, 0);
                }

                potEntity.setChanged();
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, position);
                return InteractionResult.SUCCESS;
            }

            return null;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(@NotNull final Level level, @NotNull final BlockPos position, @NotNull final BlockState state, final @Nullable LivingEntity placer, @NotNull final ItemStack stack) {
        if (level.isClientSide()) {
            level.getBlockEntity(position, ArchaeologyBlockEntityType.DECORATED_POT.get()).ifPresent(potEntity -> potEntity.setFromItem(stack));
        }
    }

    public boolean isPathfindable(@NotNull final BlockState state, @NotNull final BlockGetter blockGetter, @NotNull final BlockPos position, @NotNull final PathComputationType type) {
        return false;
    }

    public @NotNull VoxelShape getShape(@NotNull final BlockState state, @NotNull final BlockGetter blockGetter, @NotNull final BlockPos position, @NotNull final CollisionContext context) {
        return BOUNDING_BOX;
    }

    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, WATERLOGGED, CRACKED);
    }

    public @Nullable BlockEntity newBlockEntity(@NotNull final BlockPos position, @NotNull final BlockState state) {
        return new DecoratedPotBlockEntity(position, state);
    }

    @Override
    public void onRemove(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos position, final @NotNull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(position) instanceof DecoratedPotBlockEntity potEntity) {
            Containers.dropContents(level, position, potEntity);
            level.updateNeighbourForOutputSignal(position, state.getBlock());
        }

        super.onRemove(state, level, position, newState, isMoving);
    }

    public @NotNull List<ItemStack> getDrops(@NotNull final BlockState state, final LootContext.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof DecoratedPotBlockEntity potEntity) {
            builder.withDynamicDrop(SHERDS_DYNAMIC_DROP_ID, (context, stack) -> potEntity.getDecorations().sorted().map(Item::getDefaultInstance).forEach(stack));

            // FIXME (Workaround) :: The CRACKED will still be `false` even though it was set to `true` (does not happen for projectile breaks)
            List<ItemStack> drops = super.getDrops(state, builder);

            if (!state.getValue(CRACKED) && builder.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof Player player && player.getMainHandItem().is(ArchaelogyTags.BREAKS_DECORATED_POTS)) {
                boolean removed = drops.removeIf(stack -> stack.is(asItem()));

                if (removed) {
                    drops.addAll(potEntity.getDecorations().sorted().map(Item::getDefaultInstance).toList());
                }
            }

            return drops;
        }

        return super.getDrops(state, builder);
    }

    public void playerWillDestroy(@NotNull final Level level, @NotNull final BlockPos position, @NotNull final BlockState state, final Player player) {
        ItemStack stack = player.getMainHandItem();
        BlockState newState = state;

        if (stack.is(ArchaelogyTags.BREAKS_DECORATED_POTS) && stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) <= 0) {
            newState = state.setValue(CRACKED, Boolean.TRUE);
            level.setBlock(position, newState, Block.UPDATE_INVISIBLE);
        }

        super.playerWillDestroy(level, position, newState, player);
    }

    public @NotNull FluidState getFluidState(final BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public @NotNull SoundType getSoundType(final BlockState state) {
        return state.getValue(CRACKED) ? ArchaeologySounds.DECORATED_POT_CRACKED : ArchaeologySounds.DECORATED_POT;
    }

    public void appendHoverText(@NotNull final ItemStack stack, @Nullable final BlockGetter blockGetter, @NotNull final List<Component> tooltips, @NotNull final TooltipFlag flag) {
        super.appendHoverText(stack, blockGetter, tooltips, flag);
        DecoratedPotBlockEntity.Decorations decorations = DecoratedPotBlockEntity.Decorations.load(BlockItem.getBlockEntityData(stack));

        if (!decorations.equals(DecoratedPotBlockEntity.Decorations.EMPTY)) {
            tooltips.add(CommonComponents.EMPTY);
            Stream.of(decorations.front(), decorations.left(), decorations.right(), decorations.back()).forEach((item) -> tooltips.add((new ItemStack(item, 1)).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY)));
        }
    }

    @Override
    public void onProjectileHit(@NotNull final Level level, @NotNull final BlockState state, @NotNull final BlockHitResult hitResult, @NotNull final Projectile projectile) {
        if (level.isClientSide()) {
            return;
        }

        BlockPos position = hitResult.getBlockPos();

        if (projectile.mayInteract(level, position) && projectile.getType().is(ArchaelogyTags.IMPACT_PROJECTILES)) {
            level.setBlock(position, state.setValue(CRACKED, Boolean.TRUE), Block.UPDATE_INVISIBLE);
            level.destroyBlock(position, true, projectile);
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull final BlockGetter blockGetter, @NotNull final BlockPos position, @NotNull final BlockState state) {
        if (blockGetter.getBlockEntity(position) instanceof DecoratedPotBlockEntity potEntity) {
            return potEntity.getPotAsItem();
        }

        return super.getCloneItemStack(blockGetter, position, state);
    }

    @Override
    public boolean hasAnalogOutputSignal(final @NotNull BlockState position) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull final BlockState state, final Level level, @NotNull final BlockPos position) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(position));
    }
}