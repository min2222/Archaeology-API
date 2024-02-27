package com.min01.archaeology.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public interface ContainerSingleItem extends Container {
	ItemStack getTheItem();

	default ItemStack splitTheItem(int amount) {
		return this.getTheItem().split(amount);
	}

	void setTheItem(final ItemStack stack);

	default ItemStack removeTheItem() {
		return this.splitTheItem(this.getMaxStackSize());
	}

	@Override
	default int getContainerSize() {
		return 1;
	}

	@Override
	default boolean isEmpty() {
		return this.getTheItem().isEmpty();
	}

	@Override
	default void clearContent() {
		this.removeTheItem();
	}

	@Override
	default ItemStack removeItemNoUpdate(int i) {
		return this.removeItem(i, this.getMaxStackSize());
	}

	@Override
	default ItemStack getItem(int i) {
		return i == 0 ? this.getTheItem() : ItemStack.EMPTY;
	}

	@Override
	default ItemStack removeItem(int slot, int amount) {
		return slot != 0 ? ItemStack.EMPTY : this.splitTheItem(amount);
	}

	@Override
	default void setItem(int slot, final @NotNull ItemStack stack) {
		if (slot == 0) {
			this.setTheItem(stack);
		}
	}

	interface BlockContainerSingleItem extends ContainerSingleItem {
		BlockEntity getContainerBlockEntity();

		@Override
		default boolean stillValid(final @NotNull Player player) {
			BlockEntity blockEntity = getContainerBlockEntity();

			if (blockEntity.getLevel() == null) {
				return false;
			}

			if (blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) != blockEntity) {
				return false;
			}

			return player.canInteractWith(blockEntity.getBlockPos(), 4);
		}
	}
}