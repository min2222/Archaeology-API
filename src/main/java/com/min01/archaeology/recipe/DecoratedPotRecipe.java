package com.min01.archaeology.recipe;

import com.min01.archaeology.block.DecoratedPotBlock;
import com.min01.archaeology.blockentity.DecoratedPotBlockEntity;
import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaelogyTags;
import com.min01.archaeology.init.ArchaeologyItems;
import com.min01.archaeology.init.ArchaeologyRecipeSerializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DecoratedPotRecipe extends CustomRecipe {
	public DecoratedPotRecipe(ResourceLocation p_273671_) {
		super(p_273671_);
	}

	@Override
	public boolean matches(CraftingContainer p_272882_, Level p_272812_) {
		if (!this.canCraftInDimensions(p_272882_.getWidth(), p_272882_.getHeight())) {
			return false;
		} else {
			for (int i = 0; i < p_272882_.getContainerSize(); ++i) {
				ItemStack itemstack = p_272882_.getItem(i);
				switch (i) {
				case 1:
				case 3:
				case 5:
				case 7:
					if (!itemstack.is(ArchaelogyTags.DECORATED_POT_INGREDIENTS)) {
						return false;
					}
					break;
				case 2:
				case 4:
				case 6:
				default:
					if (!itemstack.is(Items.AIR)) {
						return false;
					}
				}
			}

			return true;
		}
	}

	@Override
	public ItemStack assemble(CraftingContainer p_272861) {
      DecoratedPotBlockEntity.Decorations decoratedpotblockentity$decorations = new DecoratedPotBlockEntity.Decorations(p_272861.getItem(1).getItem(), p_272861.getItem(3).getItem(), p_272861.getItem(5).getItem(), p_272861.getItem(7).getItem());
      return DecoratedPotBlockEntity.createDecoratedPotItem(decoratedpotblockentity$decorations);
   }

	public boolean canCraftInDimensions(int p_273734_, int p_273516_) {
		return p_273734_ == 3 && p_273516_ == 3;
	}

	public RecipeSerializer<?> getSerializer() {
		return ArchaeologyRecipeSerializer.DECORATED_POT_RECIPE.get();
	}
}