package rubyboat.modbattle.customBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rubyboat.modbattle.Main;

import java.util.Random;

public class GrowingPlotBlock extends Block {
    public static EnumProperty<FertilizerTypes> FERTILIZER = EnumProperty.of("fertilizer", FertilizerTypes.class);
    public static EnumProperty<CropTypes> CROP = EnumProperty.of("crop", CropTypes.class);
    public static IntProperty AGE = IntProperty.of("age",0,7);

    public GrowingPlotBlock(Settings settings) {
        super(settings);
    }
    public ItemStack fertilizerToItemStack(FertilizerTypes type)
    {
        return new ItemStack(type == FertilizerTypes.DIRT ? Items.DIRT : type == FertilizerTypes.COLD ? Items.ICE : Items.AIR);
    }

    public boolean plotCanAcceptSeeds(BlockState state)
    {
        return state.get(CROP) == CropTypes.NONE && state.get(FERTILIZER) != FertilizerTypes.NONE;
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(state.get(FERTILIZER) == FertilizerTypes.NONE)
        {
            if(player.getStackInHand(hand).getItem() == Items.DIRT) {
                world.setBlockState(pos, state.with(FERTILIZER, FertilizerTypes.DIRT));
                player.getStackInHand(hand).decrement(1);
                return ActionResult.SUCCESS;
            }
            if(player.getStackInHand(hand).getItem() == Items.ICE) {
                world.setBlockState(pos, state.with(FERTILIZER, FertilizerTypes.COLD));
                player.getStackInHand(hand).decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        else if(plotCanAcceptSeeds(state))
        {
            if(player.getStackInHand(hand).getItem() == Items.WHEAT_SEEDS) {
                player.getStackInHand(hand).decrement(1);
                world.setBlockState(pos, state.with(CROP, CropTypes.WHEAT_SEEDS));
                return ActionResult.SUCCESS;
            }
            if(player.getStackInHand(hand).getItem() == Main.BROCCOLI_SEEDS) {
                player.getStackInHand(hand).decrement(1);
                world.setBlockState(pos, state.with(CROP, CropTypes.BROCCOLI));
                return ActionResult.SUCCESS;
            }
        }else if(state.get(CROP) != CropTypes.NONE && state.get(AGE) == 7)
        {
            if(state.get(CROP) == CropTypes.BROCCOLI)
            {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Main.BROCCOLI_SEEDS, 2)));

                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),new ItemStack(Main.BROCCOLI, 2)));
            }
            if(state.get(CROP) == CropTypes.WHEAT_SEEDS)
            {
                new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.WHEAT_SEEDS, 2));
                new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),new ItemStack(Items.WHEAT, 3));
            }

            //set the age to 0
            world.setBlockState(pos, state.with(AGE, 0));
        }

        return ActionResult.PASS;
    }

    public int getCropEfficiency(BlockState state)
    {
        //get the crop type and the fertilizer type
        CropTypes crop = state.get(CROP);
        FertilizerTypes fertilizer = state.get(FERTILIZER);
        //if the fertilizer matches the crop's best type then return 5 else return 1
        return fertilizer == crop.bestType ? 5 : 2;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(random.nextInt(5) < getCropEfficiency(state) && state.get(CROP) != CropTypes.NONE)
        {
            //increment the age but only if its less than 7
            world.setBlockState(pos, state.with(AGE, Math.min(state.get(AGE) + random.nextInt(1,2), 7)));
        }
    }

    //ENUMS
    public enum FertilizerTypes implements StringIdentifiable {
        NONE("none"),
        DIRT("dirt"),
        COLD("cold"),
        SOUL_SAND("soul_sand");

        private final String name;

        FertilizerTypes(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }
    public enum CropTypes implements StringIdentifiable {
        NONE("none", null),
        WHEAT_SEEDS("wheat", FertilizerTypes.DIRT),
        BEETROOT_SEEDS("beetroot", FertilizerTypes.DIRT),
        CARROT("carrot", FertilizerTypes.DIRT),
        POTATO("potato", FertilizerTypes.DIRT),
        NETHER_WART("nether_wart", FertilizerTypes.SOUL_SAND),
        BROCCOLI("broccoli", FertilizerTypes.COLD);

        private final String name;
        private final FertilizerTypes bestType;

        CropTypes(String name, FertilizerTypes bestType) {
            this.name = name;
            this.bestType = bestType;
        }

        public String toString() {
            return this.name;
        }



        public String asString() {
            return this.name;
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FERTILIZER);
        builder.add(CROP);
        builder.add(AGE);
    }
}