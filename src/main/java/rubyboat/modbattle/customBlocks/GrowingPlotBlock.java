package rubyboat.modbattle.customBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.HopperBlockEntity;
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
import net.minecraft.text.Text;
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
    public static BooleanProperty CAN_AGE = BooleanProperty.of("can_age");

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
        else if(plotCanAcceptSeeds(state) )
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
            if(player.getStackInHand(hand).getItem() == Items.BEETROOT_SEEDS) {
                player.getStackInHand(hand).decrement(1);
                world.setBlockState(pos, state.with(CROP, CropTypes.BEETROOT_SEEDS));
                return ActionResult.SUCCESS;
            }
            if(player.getStackInHand(hand).getItem() == Items.POTATO) {
                player.getStackInHand(hand).decrement(1);
                world.setBlockState(pos, state.with(CROP, CropTypes.POTATO));
                return ActionResult.SUCCESS;
            }
            if(player.getStackInHand(hand).getItem() == Items.CARROT) {
                player.getStackInHand(hand).decrement(1);
                world.setBlockState(pos, state.with(CROP, CropTypes.CARROT));
                return ActionResult.SUCCESS;
            }
        }else if(state.get(CROP) != CropTypes.NONE && state.get(AGE) == state.get(CROP).maxAge && player.getStackInHand(hand).getItem() != Items.BONE_MEAL)
        {
            DropSeeds(state,world,pos);
            return ActionResult.SUCCESS;
        }else if(player.getStackInHand(hand).getItem() == Items.BONE_MEAL && !(state.get(CROP) != CropTypes.NONE && state.get(AGE) == state.get(CROP).maxAge))
        {
            incrementAge(world, pos, state, new Random());
            return ActionResult.SUCCESS;
        }

        //else if(player.getStackInHand(hand).getItem() == )
        //{

        //}

        return ActionResult.PASS;
    }

    public void DropSeeds(BlockState state, World world, BlockPos pos)
    {
        if(state.get(CROP) == CropTypes.BROCCOLI)
        {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Main.BROCCOLI_SEEDS, 2)));
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(),new ItemStack(Main.BROCCOLI, 2)));
        }
        if(state.get(CROP) == CropTypes.WHEAT_SEEDS)
        {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.WHEAT_SEEDS, 2)));
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(),new ItemStack(Items.WHEAT, 3)));
        }
        if(state.get(CROP) == CropTypes.BEETROOT_SEEDS)
        {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BEETROOT_SEEDS, 2)));
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(),new ItemStack(Items.BEETROOT, 3)));
        }
        if(state.get(CROP) == CropTypes.POTATO)
        {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(),new ItemStack(Items.POTATO, 3)));
        }
        if(state.get(CROP) == CropTypes.CARROT)
        {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(),new ItemStack(Items.CARROT, 3)));
        }
        //set the age to 0
        world.setBlockState(pos, state.with(AGE, 0));
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
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return (int) Math.ceil(((float) state.get(AGE)) / state.get(CROP).maxAge * 15);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(random.nextInt(5) < getCropEfficiency(state) && state.get(CROP) != CropTypes.NONE)
        {
            //increment the age but only if its less than 7
            incrementAge(world, pos, state, random);
        }
    }

    public void incrementAge(World world, BlockPos pos, BlockState state, Random random)
    {
        world.updateNeighbors(pos, this);
        if(state.get(CAN_AGE))
        {
            world.setBlockState(pos, state.with(AGE, Math.min(state.get(AGE) + random.nextInt(1,2), state.get(CROP).maxAge)));
        }
    }

    //ENUMS
    public enum FertilizerTypes implements StringIdentifiable {
        NONE("none"),
        DIRT("dirt"),
        COLD("cold");

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
        NONE("none", null, 0),
        WHEAT_SEEDS("wheat", FertilizerTypes.DIRT, 7),
        BEETROOT_SEEDS("beetroots", FertilizerTypes.DIRT, 3),
        CARROT("carrots", FertilizerTypes.DIRT, 3),
        POTATO("potatoes", FertilizerTypes.DIRT, 3),
        BROCCOLI("broccoli", FertilizerTypes.COLD, 3),
        WINTERBERRY("winterberry", FertilizerTypes.COLD, 3);

        private final String name;
        private final FertilizerTypes bestType;
        public final int maxAge;

        CropTypes(String name, FertilizerTypes bestType, int maxAge) {
            this.name = name;
            this.bestType = bestType;
            this.maxAge = maxAge;
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
        builder.add(CAN_AGE);
    }
}