package rubyboat.modbattle;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import rubyboat.modbattle.customBlocks.GrowingPlotBlock;

public class GrowingPlotDispenserBehavior extends FallibleItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        ServerWorld world = pointer.getWorld();
        if (!world.isClient()) {
            BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            BlockState blockState = world.getBlockState(blockPos);
            this.setSuccess(blockState.isOf(Main.GROWING_PLOT_BLOCK) && blockState.get(GrowingPlotBlock.AGE) >= blockState.get(GrowingPlotBlock.CROP).maxAge);
            if (this.isSuccess()) {
                if(stack.damage(1, world.getRandom(), null)) {
                    stack.setCount(0);
                }
                GrowingPlotBlock plot = (GrowingPlotBlock) (blockState).getBlock();
                plot.DropSeeds(blockState, world, blockPos);
            }

        }
        return stack;
    }
}
