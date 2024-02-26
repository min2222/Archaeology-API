package com.min01.archaeology.init;

import com.min01.archaeology.Archaeology;
import com.min01.archaeology.recipe.DecoratedPotRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArchaeologyRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Archaeology.MC_ID);

    public static final RegistryObject<RecipeSerializer<?>> DECORATED_POT_RECIPE = RECIPE_SERIALIZERS.register("crafting_decorated_pot", () -> new SimpleRecipeSerializer<>(DecoratedPotRecipe::new));
}
