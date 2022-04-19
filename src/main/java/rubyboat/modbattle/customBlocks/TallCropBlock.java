package rubyboat.modbattle.customBlocks;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import rubyboat.modbattle.Main;

import java.util.Random;

public class TallCropBlock extends TallPlantBlock implements Fertilizable {
    public static BooleanProperty isGrown = BooleanProperty.of("is_grown");


    public TallCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.getBlock() == Blocks.FARMLAND;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if(state.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            world.setBlockState(pos, state.with(isGrown, true));
        }else {
            world.setBlockState(new BlockPos(pos).add(0,1,0), world.getBlockState(pos.add(0,1,0)).with(isGrown, true));
        }
    }
    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF);
        builder.add(isGrown);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.getBlockState(pos).get(isGrown)) {
            world.setBlockState(new BlockPos(pos).add(0,world.getBlockState(pos).get(HALF) == DoubleBlockHalf.UPPER ? -1 : 1,0), state.with(isGrown, false));
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Main.CORN_SEEDS)));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!state.get(isGrown)) {
            if(random.nextInt(50) == 0) {
                grow(world, random, pos, state);
            }
        }
    }


}
