package com.min01.archaeology.init;

import com.min01.archaeology.Archaeology;
import com.min01.archaeology.client.renderer.DecoratedPotItemRenderer;
import com.min01.archaeology.item.BrushItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ArchaeologyItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Archaeology.MC_ID);

    public static final RegistryObject<Item> BRUSH = ITEMS.register("brush", () -> new BrushItem((new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)).durability(64)));
    public static final RegistryObject<Item> SUSPICIOUS_SAND = ITEMS.register("suspicious_sand", () -> new BlockItem(ArchaeologyBlocks.SUSPICIOUS_SAND.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> SUSPICIOUS_GRAVEL = ITEMS.register("suspicious_gravel", () -> new BlockItem(ArchaeologyBlocks.SUSPICIOUS_GRAVEL.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> DECORATED_POT = ITEMS.register("decorated_pot", () -> new BlockItem(ArchaeologyBlocks.DECORATED_POT.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)) {
        @Override
        public void initializeClient(final @NotNull Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new DecoratedPotItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                }
            });
        }
    });

    public static final RegistryObject<Item> MUSIC_DISC_RELIC = ITEMS.register("music_disc_relic", () -> new RecordItem(0, ArchaeologySounds.MUSIC_DISC_RELIC, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC).stacksTo(1).rarity(Rarity.RARE), 128));

    public static final RegistryObject<Item> ANGLER_POTTERY_SHERD = ITEMS.register("angler_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> ARCHER_POTTERY_SHERD = ITEMS.register("archer_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> ARMS_UP_POTTERY_SHERD = ITEMS.register("arms_up_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BLADE_POTTERY_SHERD = ITEMS.register("blade_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BREWER_POTTERY_SHERD = ITEMS.register("brewer_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BURN_POTTERY_SHERD = ITEMS.register("burn_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> DANGER_POTTERY_SHERD = ITEMS.register("danger_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> EXPLORER_POTTERY_SHERD = ITEMS.register("explorer_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> FRIEND_POTTERY_SHERD = ITEMS.register("friend_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> HEART_POTTERY_SHERD = ITEMS.register("heart_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> HEARTBREAK_POTTERY_SHERD = ITEMS.register("heartbreak_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> HOWL_POTTERY_SHERD = ITEMS.register("howl_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> MINER_POTTERY_SHERD = ITEMS.register("miner_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> MOURNER_POTTERY_SHERD = ITEMS.register("mourner_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> PLENTY_POTTERY_SHERD = ITEMS.register("plenty_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> PRIZE_POTTERY_SHERD = ITEMS.register("prize_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SHEAF_POTTERY_SHERD = ITEMS.register("sheaf_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SHELTER_POTTERY_SHERD = ITEMS.register("shelter_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SKULL_POTTERY_SHERD = ITEMS.register("skull_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SNORT_POTTERY_SHERD = ITEMS.register("snort_pottery_sherd", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
