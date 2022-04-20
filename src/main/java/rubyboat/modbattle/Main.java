package rubyboat.modbattle;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import rubyboat.modbattle.customBlocks.BroccoliPlantBlock;
import rubyboat.modbattle.customBlocks.GrowingPlotBlock;
import rubyboat.modbattle.customBlocks.TallCropBlock;
import rubyboat.modbattle.items.FarmingElytra;
import rubyboat.modbattle.items.FarmingElytraEquipmentProvider;
import rubyboat.modbattle.items.seedPackages.SeedBundle;

public class Main implements ModInitializer {
    /*
    THEME: Flying, Frozen, Farming
    IDEAS:
        (: - Elytra that plants crops as you fly
        Frozen crops that give you effects based on what they are grown in
        growing plots
        Irigated growing plots
        Frozen: brocoli, brussels, Cherry, Pomegranate
        Mild: Potato, Carrot, Wheat
        Temprate: Corn, Olives, Beetroot
        Stronger Bonemeal
        Plot Fertilizer (frozen, temperate, and mild types)
        plot sprinkler
     */

    public static final String MOD_ID = "rbmodbattle";
    public static final FarmingElytraEquipmentProvider FARMING_ELYTRA_EQUIPMENT_PROVIDER = new FarmingElytraEquipmentProvider();

    public static ItemGroup RB_MODBATTLE_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "rbmodbattle_group"), () -> new ItemStack(Items.WHEAT_SEEDS));


    public static final FarmingElytra FARMING_ELYTRA = new FarmingElytra(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1).equipmentSlot(FARMING_ELYTRA_EQUIPMENT_PROVIDER).maxDamage(400));

    public static final FoodComponent BROCCOLI_FOOD = new FoodComponent.Builder().hunger(6).saturationModifier(0.6f).build();
    public static final Item BROCCOLI = new Item(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(64).food(BROCCOLI_FOOD));
    public static final BroccoliPlantBlock BROCCOLI_PLANT = new BroccoliPlantBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).nonOpaque().collidable(false));
    public static final Item BROCCOLI_SEEDS = new BlockItem(BROCCOLI_PLANT, new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(64));

    public static final Block CORN_PLANT = new TallCropBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).nonOpaque().collidable(false));
    public static final Item CORN_SEEDS = new TallBlockItem(CORN_PLANT, new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(64));

    public static final SeedBundle POTATO_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.POTATO);
    public static final SeedBundle CARROT_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.CARROT);
    public static final SeedBundle WHEAT_SEEDS_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.WHEAT_SEEDS);
    public static final SeedBundle BEETROOT_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.BEETROOT_SEEDS);
    public static final SeedBundle BAMBOO_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.BAMBOO);
    public static final SeedBundle NETHER_WART_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.NETHER_WART);
    public static final SeedBundle BROCCOLI_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Main.BROCCOLI_SEEDS);

    public static final GrowingPlotBlock GROWING_PLOT_BLOCK = new GrowingPlotBlock(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).nonOpaque().ticksRandomly());
    public static final Item GROWING_PLOT_ITEM = new BlockItem(GROWING_PLOT_BLOCK, new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(64));
    public static Item[] crops = new Item[] {
            Items.WHEAT_SEEDS, //done
            Items.BEETROOT_SEEDS, //done
            Items.CARROT, //done
            Items.POTATO, //done
            Items.BAMBOO, //done
            Items.NETHER_WART,
            Main.BROCCOLI_SEEDS
    };
    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "farming_elytra"), FARMING_ELYTRA);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "potato_bundle"), POTATO_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "carrot_bundle"), CARROT_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "wheat_seeds_bundle"), WHEAT_SEEDS_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "beetroot_bundle"), BEETROOT_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bamboo_bundle"), BAMBOO_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "nether_wart_bundle"), NETHER_WART_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "broccoli_bundle"), BROCCOLI_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "broccoli_seeds"), BROCCOLI_SEEDS);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "broccoli"), BROCCOLI);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "corn_seeds"), CORN_SEEDS);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "broccoli"), BROCCOLI_PLANT);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "corn"), CORN_PLANT);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "growing_plot"), GROWING_PLOT_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "growing_plot"), GROWING_PLOT_ITEM);
        ModelPredicateProviderRegistry.register(POTATO_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(CARROT_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(WHEAT_SEEDS_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(BEETROOT_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(BAMBOO_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(NETHER_WART_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(BROCCOLI_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
    }
}
