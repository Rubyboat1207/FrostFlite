package rubyboat.modbattle.customBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import rubyboat.modbattle.Main;

public class BroccoliPlantBlock extends CropBlock {


    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            //age 0
            Block.createCuboidShape(4,0,4, 12, 10, 12),
            Block.createCuboidShape(4,0,4, 12, 10, 12),
            //age 1
            Block.createCuboidShape(4,0,4, 13, 11, 13),
            Block.createCuboidShape(4,0,4, 13, 11, 13),
            Block.createCuboidShape(4,0,4, 13, 11, 13),
            //age 2
            Block.createCuboidShape(4,0,4, 13, 11, 13),
            Block.createCuboidShape(4,0,4, 13, 11, 13),
            //age 3
            Block.createCuboidShape(2,0,2, 14, 15, 14),
    };

    public BroccoliPlantBlock(Settings settings) {
        super(settings);
    }

    public ItemConvertible getSeedsItem() {
        return Main.BROCCOLI_SEEDS;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
    }
}
