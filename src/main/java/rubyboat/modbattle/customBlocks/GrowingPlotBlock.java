package rubyboat.modbattle.customBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        }
        else if(player.getStackInHand(hand).isEmpty())
        {
            player.setStackInHand(hand, fertilizerToItemStack(state.get(FERTILIZER)));
            world.setBlockState(pos, state.with(FERTILIZER, FertilizerTypes.NONE));
            return ActionResult.SUCCESS;
        }else if(plotCanAcceptSeeds(state))
        {
            if(player.getStackInHand(hand).getItem() == Items.WHEAT_SEEDS) {
                player.getStackInHand(hand).decrement(1);
                world.setBlockState(pos, state.with(CROP, CropTypes.WHEAT_SEEDS));
            }
        }

        return ActionResult.PASS;
    }

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
        NONE("none"),
        WHEAT_SEEDS("wheat"),
        BEETROOT_SEEDS("beetroot"),
        CARROT("carrot"),
        POTATO("potato"),
        NETHER_WART("nether_wart"),
        BROCCOLI("broccoli");

        private final String name;

        CropTypes(String name) {
            this.name = name;
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