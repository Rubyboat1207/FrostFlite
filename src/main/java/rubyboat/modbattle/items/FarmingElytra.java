package rubyboat.modbattle.items;

import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import rubyboat.modbattle.Main;
import rubyboat.modbattle.items.seedPackages.SeedBundle;

import java.util.Arrays;

public class FarmingElytra extends ElytraItem implements FabricElytraItem {
    public FarmingElytra(Settings settings) {
        super(settings);
    }

    @Override
    public boolean useCustomElytra(LivingEntity entity, ItemStack chestStack, boolean tickElytra) {
        //seeds
        BlockPos prevLoc = new BlockPos(entity.prevX, entity.prevY, entity.prevZ);
        if(prevLoc.getX() != entity.getX() || prevLoc.getZ() != entity.getZ()) {
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                ItemStack offhandStack = player.getStackInHand(Hand.OFF_HAND);
                Item crop = offhandStack.getItem();
                if(Arrays.asList(Main.crops).contains(crop) || crop instanceof SeedBundle) {
                    for(int i = prevLoc.getY(); i > -64; i--) {
                        if(SeedBundle.getSeeds(offhandStack) <= 0 && crop instanceof SeedBundle) {
                            SeedBundle.setSeeds(offhandStack, 0);
                            break;
                        }
                        BlockPos pos = new BlockPos(entity.getX(), i, entity.getZ());
                        if(entity.world.getBlockState(pos).getBlock() == Blocks.AIR) {continue;}
                        int count = Update3blockAreaWithBlock(crop instanceof BlockItem ? ((BlockItem) crop).getBlock() : ((SeedBundle) crop).seed.getBlock(), new BlockPos(pos.add(0,1,0)), entity.world, crop instanceof BlockItem ? offhandStack.getMaxCount() : SeedBundle.getSeeds(offhandStack));
                        if(!player.getWorld().isClient){
                            //decrement
                            if(crop instanceof SeedBundle) {
                                SeedBundle.decrementSeeds(offhandStack, count);
                            }else
                            {
                                offhandStack.decrement(count);
                            }
                        }
                        if(entity.world.getBlockState(pos).getBlock() != Blocks.AIR ) {
                            break;
                        }
                    }
                }
            }
        }
        //damage
        int nextRoll = entity.getRoll() + 1;

        if (!entity.world.isClient && nextRoll % 10 == 0) {
            if ((nextRoll / 10) % 2 == 0) {
                chestStack.damage(1, entity, p -> p.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
            }

            entity.emitGameEvent(GameEvent.ELYTRA_FREE_FALL);
        }

        return true;
    }
    //I know that this sometimes causes extra seeds to be placed, but its a feature, not a bug
    public int Update3blockAreaWithBlock(Block block, BlockPos pos, World world, int maxCount) {
        BlockPos corner1 = new BlockPos(pos).add(2,0,2);
        BlockPos corner2 = new BlockPos(pos).add(-2,0,-2);
        int count = 0;
        for(BlockPos pos1 : BlockPos.iterate(corner1, corner2)) {
            if(count >= maxCount) {
                break;
            }
            //if the block at pos1 is air and the block below is farmland then replace it with the block
            if(world.getBlockState(pos1).getBlock() == Blocks.AIR) {
                if(world.getBlockState(new BlockPos(pos1).add(0, -1, 0)).getBlock() == Blocks.FARMLAND || (block == Blocks.BAMBOO && (world.getBlockState(new BlockPos(pos1).add(0, -1, 0)).getBlock() == Blocks.GRASS_BLOCK || world.getBlockState(new BlockPos(pos1).add(0, -1, 0)).getBlock() == Blocks.DIRT))) {
                    world.setBlockState(pos1, block.getDefaultState());
                    count++;
                }
            }
        }
        return count;
    }
}
