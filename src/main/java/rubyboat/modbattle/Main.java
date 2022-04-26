package rubyboat.modbattle;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldEvents;
import rubyboat.modbattle.customBlocks.BroccoliPlantBlock;
import rubyboat.modbattle.customBlocks.GrowingPlotBlock;
import rubyboat.modbattle.customBlocks.WinterBerryBushBlock;
import rubyboat.modbattle.items.FarmingElytra;
import rubyboat.modbattle.items.FarmingElytraEquipmentProvider;
import rubyboat.modbattle.items.FrostScythe;
import rubyboat.modbattle.items.seedPackages.SeedBundle;

import java.util.Random;

public class Main implements ModInitializer {
    /*
    THEME: Flying, Frozen, Farming
    IDEAS:
        Elytra that plants crops as you fly
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

    public static final WinterBerryBushBlock WINTERBERRY_BUSH = new WinterBerryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).nonOpaque().collidable(false));
    public static final FoodComponent WINTERBERRY_FOOD = new FoodComponent.Builder().hunger(6).saturationModifier(0.6f).build();
    public static final Item WINTERBERRY = new BlockItem(WINTERBERRY_BUSH, new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(64).food(WINTERBERRY_FOOD));
    public static final Item FROST_SCYTHE = new FrostScythe(ToolMaterials.DIAMOND, 3, -2, new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1).maxDamage(1250));

    public static final SeedBundle POTATO_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.POTATO);
    public static final SeedBundle CARROT_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.CARROT);
    public static final SeedBundle WHEAT_SEEDS_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.WHEAT_SEEDS);
    public static final SeedBundle BEETROOT_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.BEETROOT_SEEDS);
    public static final SeedBundle BAMBOO_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.BAMBOO);
    public static final SeedBundle NETHER_WART_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Items.NETHER_WART);
    public static final SeedBundle BROCCOLI_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Main.BROCCOLI_SEEDS);
    public static final SeedBundle WINTERBERRY_BUNDLE = new SeedBundle(new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(1), (BlockItem) Main.WINTERBERRY);

    public static final GrowingPlotBlock GROWING_PLOT_BLOCK = new GrowingPlotBlock(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).nonOpaque().ticksRandomly().strength(3, 1).drops(new Identifier(MOD_ID, "blocks/growing_plot")));
    public static final Item GROWING_PLOT_ITEM = new BlockItem(GROWING_PLOT_BLOCK, new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(64));

    public static final Block FROSTED_BLUE_ICE_BLOCK = new Block(FabricBlockSettings.of(Material.ICE).strength(0.5f, 0.5f).sounds(BlockSoundGroup.GLASS).nonOpaque().collidable(true).slipperiness(0.995f).drops(new Identifier(MOD_ID, "blocks/frosted_blue_ice")));
    public static final Item FROSTED_BLUE_ICE = new BlockItem(FROSTED_BLUE_ICE_BLOCK, new FabricItemSettings().group(Main.RB_MODBATTLE_GROUP).maxCount(64).rarity(Rarity.UNCOMMON));

    public static Item[] crops = new Item[] {
            Items.WHEAT_SEEDS,
            Items.BEETROOT_SEEDS,
            Items.CARROT,
            Items.POTATO,
            Items.BAMBOO,
            Items.NETHER_WART,
            Main.BROCCOLI_SEEDS
    };

    /*public void dispenserBehavior(BlockPointer pointer, ItemStack stack) {
        BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
        ServerWorld world = pointer.getWorld();
        BlockState blockState = world.getBlockState(blockPos);
        if(blockState.isOf(Main.GROWING_PLOT_BLOCK) && blockState.get(GrowingPlotBlock.AGE) >= blockState.get(GrowingPlotBlock.CROP).maxAge){
            GrowingPlotBlock plot = (GrowingPlotBlock) (blockState).getBlock();
            plot.DropSeeds(blockState, world, blockPos);
        }
    }
    */

    @Override
    public void onInitialize() {
        //bundles
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "farming_elytra"), FARMING_ELYTRA);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "potato_bundle"), POTATO_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "carrot_bundle"), CARROT_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "wheat_seeds_bundle"), WHEAT_SEEDS_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "beetroot_bundle"), BEETROOT_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bamboo_bundle"), BAMBOO_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "nether_wart_bundle"), NETHER_WART_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "broccoli_bundle"), BROCCOLI_BUNDLE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "winterberry_bundle"), WINTERBERRY_BUNDLE);
        //Broccoli
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "broccoli_seeds"), BROCCOLI_SEEDS);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "broccoli"), BROCCOLI);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "broccoli"), BROCCOLI_PLANT);
        //Growing Plot
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "growing_plot"), GROWING_PLOT_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "growing_plot"), GROWING_PLOT_ITEM);
        //Winterberry
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "winterberry_bush"), WINTERBERRY_BUSH);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "winterberry"), WINTERBERRY);
        //scythe
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "frost_scythe"), FROST_SCYTHE);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "frosted_blue_ice"), FROSTED_BLUE_ICE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "frosted_blue_ice"), FROSTED_BLUE_ICE);


        //Model Predicates
        ModelPredicateProviderRegistry.register(POTATO_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(CARROT_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(WHEAT_SEEDS_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(BEETROOT_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(BAMBOO_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(NETHER_WART_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(BROCCOLI_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(WINTERBERRY_BUNDLE, new Identifier(MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        //Dispenser Behavior
        DispenserBlock.registerBehavior(Items.WOODEN_HOE, new GrowingPlotDispenserBehavior());
        DispenserBlock.registerBehavior(Items.STONE_HOE, new GrowingPlotDispenserBehavior());
        DispenserBlock.registerBehavior(Items.IRON_HOE, new GrowingPlotDispenserBehavior());
        DispenserBlock.registerBehavior(Items.DIAMOND_HOE, new GrowingPlotDispenserBehavior());
        DispenserBlock.registerBehavior(Items.NETHERITE_HOE, new GrowingPlotDispenserBehavior());
        DispenserBlock.registerBehavior(Items.BONE_MEAL, new FallibleItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                this.setSuccess(true);
                ServerWorld world = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                if (world.getBlockState(blockPos).getBlock() instanceof GrowingPlotBlock && world.getBlockState(blockPos).get(GrowingPlotBlock.AGE) < world.getBlockState(blockPos).get(GrowingPlotBlock.CROP).maxAge) {
                    if (!world.isClient) {
                        world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                        ((GrowingPlotBlock) world.getBlockState(blockPos).getBlock()).incrementAge(world, blockPos, world.getBlockState(blockPos), new Random());
                    }
                } else {
                    this.setSuccess(false);
                }
                stack.decrement(1);
                return stack;
            }
        });

        //new TotemItem(new Item.Settings().group(Main.RB_MODBATTLE_GROUP), new Identifier(MOD_ID, "frozen_totem"), new ArrayList<StatusEffectInstance>() {{add(new StatusEffectInstance(StatusEffects.INVISIBILITY, 60*20, 0));add(new StatusEffectInstance(StatusEffects.REGENERATION, 15*20, 1));}},10, new AndEffect(new FreezeEffect(20), new AOEEffect(5,new StatusEffectInstance(StatusEffects.SLOWNESS, 15*20, 1))));
    }
}
