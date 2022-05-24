package rubyboat.modbattle.customBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import rubyboat.modbattle.Main;

import java.util.Random;

public class PykreteBlock extends PillarBlock {
    public static IntProperty MELTSTATUS = IntProperty.of("melt_status",0,7);

    public PykreteBlock(Settings settings) {
        super(settings);
    }



    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(MELTSTATUS) + 1 == 8) {
            world.setBlockState(pos, Blocks.OAK_LOG.getDefaultState().with(AXIS, state.get(AXIS)));
        }else{
            if(world.getLightLevel(pos) > 10  && !(world.getBlockState(pos.add(0,-1,0)).getBlock() instanceof PykreteBlock || world.getBlockState(pos.add(0,-1,0)).getBlock() == Main.HARDENED_PYKRETE_LOG_BLOCK) && world.getBlockState(pos.add(0,-1,0)).getBlock() != Main.FROSTED_BLUE_ICE_BLOCK) {
                world.setBlockState(pos, state.with(MELTSTATUS, state.get(MELTSTATUS) + 1));
            }
        }
        world.updateNeighbors(pos, this);
    }




    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(MELTSTATUS);
    }
}
