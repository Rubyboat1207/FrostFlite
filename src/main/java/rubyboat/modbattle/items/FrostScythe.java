package rubyboat.modbattle.items;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FrostScythe extends SwordItem {
    public FrostScythe(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of(itemUsageContext -> true, HoeItem.createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        for(BlockPos pos : BlockPos.iterate(new BlockPos(blockPos).add(1,0,1), new BlockPos(blockPos).add(-1,0,-1))) {
            Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = TILLING_ACTIONS.get(world.getBlockState(pos).getBlock());
            if (pair == null) {
                continue;
            }
            Predicate<ItemUsageContext> predicate = pair.getFirst();
            Consumer<ItemUsageContext> consumer = pair.getSecond();
            ItemUsageContext posContext = new ItemUsageContext(context.getPlayer(), context.getHand(), new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), context.getSide(), pos, context.hitsInsideBlock()));
            if (predicate.test(posContext)) {
                PlayerEntity playerEntity = context.getPlayer();
                world.playSound(playerEntity, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                if (!world.isClient) {
                    consumer.accept(posContext);

                    if (playerEntity != null) {
                        context.getStack().damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
                    }
                }
            }
        }
        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = TILLING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
        if (pair == null) { return ActionResult.PASS; }
        Predicate<ItemUsageContext> predicate = pair.getFirst();
        Consumer<ItemUsageContext> consumer = pair.getSecond();
        if (predicate.test(context)) {
            PlayerEntity playerEntity = context.getPlayer();
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (!world.isClient) {
                consumer.accept(context);
                if (playerEntity != null) {
                    context.getStack().damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
                }
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

    /**
     * {@return a tilling action that sets a block state}
     *
     * @param result the tilled block state
     */
    public static Consumer<ItemUsageContext> createTillAction(BlockState result) {
        return context -> context.getWorld().setBlockState(context.getBlockPos(), result, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
    }

    /**
     * {@return a tilling action that sets a block state and drops an item}
     *
     * @param droppedItem the item to drop
     * @param result the tilled block state
     */
    public static Consumer<ItemUsageContext> createTillAndDropAction(BlockState result, ItemConvertible droppedItem) {
        return context -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            Block.dropStack(context.getWorld(), context.getBlockPos(), context.getSide(), new ItemStack(droppedItem));
        };
    }

    /**
     * {@return whether the used block can be tilled into farmland}
     * This method is used as the tilling predicate for most vanilla blocks except rooted dirt.
     */
    public static boolean canTillFarmland(ItemUsageContext context) {
        return context.getSide() != Direction.DOWN && context.getWorld().getBlockState(context.getBlockPos().up()).isAir();
    }
}
