package com.min01.archaeology.container;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public interface RandomizableContainer extends Container {
    String LOOT_TABLE_TAG = "LootTable";
    String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    @Nullable
    ResourceLocation getLootTable();

    void setLootTable(final @Nullable ResourceLocation lootTable);

    default void setLootTable(final ResourceLocation lootTable, long lootTableSeed) {
        this.setLootTable(lootTable);
        this.setLootTableSeed(lootTableSeed);
    }

    long getLootTableSeed();

    void setLootTableSeed(long lootTableSeed);

    BlockPos getBlockPos();

    @Nullable
    Level getLevel();

    static void setBlockEntityLootTable(final BlockGetter blockGetter, final RandomSource random, final BlockPos position, final ResourceLocation lootTable) {
        if (blockGetter.getBlockEntity(position) instanceof RandomizableContainer randomizableContainer) {
            randomizableContainer.setLootTable(lootTable, random.nextLong());
        }
    }

    default boolean tryLoadLootTable(final CompoundTag tag) {
        if (tag.contains(LOOT_TABLE_TAG, ListTag.TAG_STRING)) {
            setLootTable(new ResourceLocation(tag.getString(LOOT_TABLE_TAG)));
            setLootTableSeed(tag.getLong(LOOT_TABLE_SEED_TAG));
            return true;
        } else {
            return false;
        }
    }

    default boolean trySaveLootTable(final CompoundTag tag) {
        ResourceLocation lootTable = getLootTable();

        if (lootTable == null) {
            return false;
        } else {
            tag.putString(LOOT_TABLE_TAG, lootTable.toString());
            long lootTableSeed = getLootTableSeed();

            if (lootTableSeed != 0) {
                tag.putLong(LOOT_TABLE_SEED_TAG, lootTableSeed);
            }

            return true;
        }
    }

    default void unpackLootTable(final @Nullable Player player) {
        Level level = getLevel();
        BlockPos position = getBlockPos();
        ResourceLocation lootTableRaw = getLootTable();

        if (lootTableRaw != null && level instanceof ServerLevel serverLevel) {
            LootTable lootTable = serverLevel.getServer().getLootTables().get(lootTableRaw);
            LootContext.Builder builder = new LootContext.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(position));
            setLootTable(null);

            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, lootTableRaw);
                builder.withLuck(serverPlayer.getLuck()).withParameter(LootContextParams.THIS_ENTITY, serverPlayer);
            }

            lootTable.fill(this, builder.create(LootContextParamSets.CHEST));
        }
    }
}