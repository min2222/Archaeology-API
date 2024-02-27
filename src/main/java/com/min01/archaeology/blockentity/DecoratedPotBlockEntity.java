package com.min01.archaeology.blockentity;

import com.min01.archaeology.container.ContainerSingleItem;
import com.min01.archaeology.container.RandomizableContainer;
import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DecoratedPotBlockEntity extends BlockEntity implements RandomizableContainer, ContainerSingleItem.BlockContainerSingleItem {
   public static final String TAG_SHERDS = "sherds";
   public static final String TAG_ITEM = "item";
   public static final int EVENT_POT_WOBBLES = 1;
   public long wobbleStartedAtTick;
   public @Nullable DecoratedPotBlockEntity.WobbleStyle lastWobbleStyle;
   private DecoratedPotBlockEntity.Decorations decorations;
   private ItemStack stack = ItemStack.EMPTY;
   protected @Nullable ResourceLocation lootTable;
   protected long lootTableSeed;

   public DecoratedPotBlockEntity(final BlockPos position, final BlockState state) {
      super(ArchaeologyBlockEntityType.DECORATED_POT.get(), position, state);
      decorations = Decorations.EMPTY;
   }

   protected void saveAdditional(final @NotNull CompoundTag tag) {
      super.saveAdditional(tag);
      decorations.save(tag);

      if (!trySaveLootTable(tag) && !stack.isEmpty()) {
         tag.put(TAG_ITEM, stack.save(new CompoundTag()));
      }
   }

   public void load(final @NotNull CompoundTag tag) {
      super.load(tag);
      decorations = DecoratedPotBlockEntity.Decorations.load(tag);

      if (!tryLoadLootTable(tag)) {
         if (tag.contains(TAG_ITEM, ListTag.TAG_COMPOUND)) {
            stack = ItemStack.of(tag.getCompound(TAG_ITEM));
         } else {
            stack = ItemStack.EMPTY;
         }
      }
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public @NotNull CompoundTag getUpdateTag() {
      return this.saveWithoutMetadata();
   }

   public Direction getDirection() {
      return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
   }

   public DecoratedPotBlockEntity.Decorations getDecorations() {
      return this.decorations;
   }

   public void setFromItem(final ItemStack stack) {
      this.decorations = DecoratedPotBlockEntity.Decorations.load(BlockItem.getBlockEntityData(stack));
   }

   public ItemStack getPotAsItem() {
      return createDecoratedPotItem(decorations);
   }

   public static ItemStack createDecoratedPotItem(final DecoratedPotBlockEntity.Decorations decorations) {
      ItemStack stack = ArchaeologyItems.DECORATED_POT.get().getDefaultInstance();
      CompoundTag tag = decorations.save(new CompoundTag());
      BlockItem.setBlockEntityData(stack, ArchaeologyBlockEntityType.DECORATED_POT.get(), tag);
      return stack;
   }

   @Override
   public @Nullable ResourceLocation getLootTable() {
      return lootTable;
   }

   @Override
   public void setLootTable(final @Nullable ResourceLocation lootTable) {
      this.lootTable = lootTable;
   }

   @Override
   public long getLootTableSeed() {
      return lootTableSeed;
   }

   @Override
   public void setLootTableSeed(long lootTableSeed) {
      this.lootTableSeed = lootTableSeed;
   }

   @Override
   public ItemStack getTheItem() {
      unpackLootTable(null);
      return stack;
   }

   @Override
   public ItemStack splitTheItem(int amount) {
      unpackLootTable(null);
      ItemStack stack = this.stack.split(amount);

      if (this.stack.isEmpty()) {
         this.stack = ItemStack.EMPTY;
      }

      return stack;
   }

   @Override
   public void setTheItem(final ItemStack stack) {
      unpackLootTable(null);
      this.stack = stack;
   }

   @Override
   public BlockEntity getContainerBlockEntity() {
      return this;
   }

   public void wobble(final DecoratedPotBlockEntity.WobbleStyle wobbleStyle) {
      if (level != null && !level.isClientSide()) {
         level.blockEvent(getBlockPos(), getBlockState().getBlock(), EVENT_POT_WOBBLES, wobbleStyle.ordinal());
      }
   }

   @Override
   public boolean triggerEvent(int event, int duration) {
      if (level != null && event == EVENT_POT_WOBBLES && duration >= 0 && duration < DecoratedPotBlockEntity.WobbleStyle.values().length) {
         wobbleStartedAtTick = level.getGameTime();
         lastWobbleStyle = DecoratedPotBlockEntity.WobbleStyle.values()[duration];
         return true;
      } else {
         return super.triggerEvent(event, duration);
      }
   }

   public record Decorations(Item back, Item left, Item right, Item front) {
      public static final DecoratedPotBlockEntity.Decorations EMPTY = new DecoratedPotBlockEntity.Decorations(Items.BRICK, Items.BRICK, Items.BRICK, Items.BRICK);

      @SuppressWarnings("ConstantConditions")
      public CompoundTag save(final CompoundTag tag) {
         ListTag listTag = new ListTag();
         sorted().forEach(item -> listTag.add(StringTag.valueOf(ForgeRegistries.ITEMS.getKey(item).toString())));
         tag.put(TAG_SHERDS, listTag);
         return tag;
      }

      public Stream<Item> sorted() {
         return Stream.of(this.back, this.left, this.right, this.front);
      }

      public static DecoratedPotBlockEntity.Decorations load(final @Nullable CompoundTag tag) {
         if (tag != null && tag.contains(TAG_SHERDS, ListTag.TAG_LIST)) {
            ListTag listTag = tag.getList(TAG_SHERDS, ListTag.TAG_STRING);
            return new DecoratedPotBlockEntity.Decorations(itemFromTag(listTag, 0), itemFromTag(listTag, 1), itemFromTag(listTag, 2), itemFromTag(listTag, 3));
         } else {
            return EMPTY;
         }
      }

      private static Item itemFromTag(final ListTag listTag, int index) {
         if (index >= listTag.size()) {
            return Items.BRICK;
         } else {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation(listTag.get(index).getAsString()));
         }
      }
   }

   public enum WobbleStyle {
      POSITIVE(7),
      NEGATIVE(10);

      public final int duration;

      WobbleStyle(int duration) {
         this.duration = duration;
      }
   }
}