package com.min01.archaeology.blockentity;

import com.min01.archaeology.block.BrushableBlock;
import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.misc.CustomLevelEvent;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;

public class BrushableBlockEntity extends BlockEntity {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final String LOOT_TABLE_TAG = "LootTable";
   private static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";
   private static final String HIT_DIRECTION_TAG = "hit_direction";
   private static final String ITEM_TAG = "item";
   private static final int BRUSH_COOLDOWN_TICKS = 10;
   private static final int BRUSH_RESET_TICKS = 40;
   private static final int REQUIRED_BRUSHES_TO_BREAK = 10;
   private int brushCount;
   private long brushCountResetsAtTick;
   private long coolDownEndsAtTick;
   private ItemStack item = ItemStack.EMPTY;
   private @Nullable Direction hitDirection;
   private @Nullable ResourceLocation lootTable;
   private long lootTableSeed;

   public BrushableBlockEntity(final BlockPos position, final BlockState state) {
      super(ArchaeologyBlockEntityType.BRUSHABLE_BLOCK.get(), position, state);
   }

   public boolean brush(long gameTime, final Player player, final Direction hitDirection) {
      if (hitDirection == null) {
         this.hitDirection = null;
      }

      brushCountResetsAtTick = gameTime + BRUSH_RESET_TICKS;

      if (gameTime >= coolDownEndsAtTick && level instanceof ServerLevel) {
         coolDownEndsAtTick = gameTime + BRUSH_COOLDOWN_TICKS;
         unpackLootTable(player);
         int oldState = getCompletionState();
         brushCount++;

         if (brushCount >= REQUIRED_BRUSHES_TO_BREAK) {
            brushingCompleted(player);
            return true;
         } else {
            level.scheduleTick(getBlockPos(), getBlockState().getBlock(), BRUSH_RESET_TICKS);
            int newState = getCompletionState();

            if (oldState != newState) {
               level.setBlock(getBlockPos(), getBlockState().setValue(BrushableBlock.DUSTED, newState), Block.UPDATE_ALL);
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public void unpackLootTable(final Player player) {
      if (lootTable != null && player instanceof ServerPlayer serverPlayer) {
         LootTable loottable = serverPlayer.getLevel().getServer().getLootTables().get(lootTable);
         CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, lootTable);
         LootContext context = new LootContext.Builder(serverPlayer.getLevel())
                 .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition))
                 .withLuck(player.getLuck())
                 .withParameter(LootContextParams.THIS_ENTITY, player)
                 .create(LootContextParamSets.CHEST);
         ObjectArrayList<ItemStack> items = loottable.getRandomItems(context);

         item = switch (items.size()) {
            case 0 -> ItemStack.EMPTY;
            case 1 -> items.get(0);
            default -> {
               LOGGER.warn("Expected max 1 loot from loot table " + lootTable + " got " + items.size());
               yield items.get(0);
            }
         };

         lootTable = null;
         setChanged();
      }
   }

   private void brushingCompleted(final Player player) {
      if (level != null && level.getServer() != null) {
         dropContent(player);
         BlockState blockstate = getBlockState();
         level.levelEvent(CustomLevelEvent.PARTICLES_AND_SOUND_BRUSH_BLOCK_COMPLETE, getBlockPos(), Block.getId(blockstate));
         Block brushedBlock = blockstate.getBlock() instanceof BrushableBlock brushable ? brushable.getTurnsInto() : Blocks.AIR;
         level.setBlock(worldPosition, brushedBlock.defaultBlockState(), Block.UPDATE_ALL);
      }
   }

   private void dropContent(final Player player) {
      if (level != null && level.getServer() != null) {
         unpackLootTable(player);

         if (!item.isEmpty()) {
            double width = EntityType.ITEM.getWidth();
            double offset = (1 - width) + (width / 2);
            Direction direction = Objects.requireNonNullElse(hitDirection, Direction.UP);
            BlockPos blockpos = worldPosition.relative(direction, 1);
            double x = (double) blockpos.getX() + 0.5D * offset;
            double y = (double) blockpos.getY() + 0.5D + (double) (EntityType.ITEM.getHeight() / 2.0F);
            double z = (double) blockpos.getZ() + 0.5D * offset;
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, item.split(level.random.nextInt(21) + 10));
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
            item = ItemStack.EMPTY;
         }
      }
   }

   public void checkReset() {
      if (level != null) {
         if (brushCount != 0 && level.getGameTime() >= brushCountResetsAtTick) {
            int oldState = getCompletionState();
            brushCount = Math.max(0, brushCount - 2);
            int newState = getCompletionState();

            if (oldState != newState) {
               level.setBlock(getBlockPos(), getBlockState().setValue(BrushableBlock.DUSTED, newState), Block.UPDATE_ALL);
            }

            brushCountResetsAtTick = level.getGameTime() + 4;
         }

         if (brushCount == 0) {
            hitDirection = null;
            brushCountResetsAtTick = 0;
            coolDownEndsAtTick = 0;
         } else {
            level.scheduleTick(getBlockPos(), getBlockState().getBlock(), (int) (brushCountResetsAtTick - level.getGameTime()));
         }
      }
   }

   private boolean tryLoadLootTable(final CompoundTag tag) {
      if (tag.contains(LOOT_TABLE_TAG, ListTag.TAG_STRING)) {
         lootTable = new ResourceLocation(tag.getString(LOOT_TABLE_TAG));
         lootTableSeed = tag.getLong(LOOT_TABLE_SEED_TAG);
         return true;
      } else {
         return false;
      }
   }

   private boolean trySaveLootTable(final CompoundTag tag) {
      if (lootTable == null) {
         return false;
      } else {
         tag.putString(LOOT_TABLE_TAG, lootTable.toString());

         if (lootTableSeed != 0) {
            tag.putLong(LOOT_TABLE_SEED_TAG, lootTableSeed);
         }

         return true;
      }
   }

   public @NotNull CompoundTag getUpdateTag() {
      CompoundTag compoundtag = super.getUpdateTag();

      if (hitDirection != null) {
         compoundtag.putInt(HIT_DIRECTION_TAG, hitDirection.ordinal());
      }

      compoundtag.put(ITEM_TAG, item.save(new CompoundTag()));
      return compoundtag;
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public void load(@NotNull final CompoundTag tag) {
      if (!tryLoadLootTable(tag) && tag.contains(ITEM_TAG)) {
         item = ItemStack.of(tag.getCompound(ITEM_TAG));
      }

      if (tag.contains(HIT_DIRECTION_TAG)) {
         hitDirection = Direction.values()[tag.getInt(HIT_DIRECTION_TAG)];
      }
   }

   protected void saveAdditional(@NotNull final CompoundTag tag) {
      if (!trySaveLootTable(tag)) {
         tag.put(ITEM_TAG, item.save(new CompoundTag()));
      }
   }

   public void setLootTable(final ResourceLocation lootTable, long lootTableSeed) {
      this.lootTable = lootTable;
      this.lootTableSeed = lootTableSeed;
   }

   private int getCompletionState() {
      if (brushCount == 0) {
         return 0;
      } else if (brushCount < 3) {
         return 1;
      } else {
         return brushCount < 6 ? 2 : 3;
      }
   }

   public @Nullable Direction getHitDirection() {
      return hitDirection;
   }

   public ItemStack getItem() {
      return item;
   }
}