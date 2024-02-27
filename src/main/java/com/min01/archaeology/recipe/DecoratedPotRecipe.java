package com.min01.archaeology.recipe;

import org.jetbrains.annotations.NotNull;

import com.min01.archaeology.blockentity.DecoratedPotBlockEntity;
import com.min01.archaeology.init.ArchaelogyTags;
import com.min01.archaeology.init.ArchaeologyRecipeSerializer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DecoratedPotRecipe extends CustomRecipe {
	public DecoratedPotRecipe(final ResourceLocation location) {
		super(location);
	}

	@Override
	public boolean matches(final CraftingContainer container, final @NotNull Level level) {
		if (!this.canCraftInDimensions(container.getWidth(), container.getHeight())) {
			return false;
		} else {
			for (int i = 0; i < container.getContainerSize(); ++i) {
				ItemStack stack = container.getItem(i);

                if (i % 2 == 0) {
                    if (!stack.is(Items.AIR)) {
                        return false;
                    }
                } else {
                    if (!stack.is(ArchaelogyTags.DECORATED_POT_INGREDIENTS)) {
                        return false;
                    }
                }
            }

			return true;
		}
	}

	@Override
	public @NotNull ItemStack assemble(final CraftingContainer container) {
      DecoratedPotBlockEntity.Decorations decorations = new DecoratedPotBlockEntity.Decorations(container.getItem(1).getItem(), container.getItem(3).getItem(), container.getItem(5).getItem(), container.getItem(7).getItem());
      return DecoratedPotBlockEntity.createDecoratedPotItem(decorations);
   }

	public boolean canCraftInDimensions(int width, int height) {
		return width == 3 && height == 3;
	}

	public @NotNull RecipeSerializer<?> getSerializer() {
		return ArchaeologyRecipeSerializer.DECORATED_POT_RECIPE.get();
	}
}