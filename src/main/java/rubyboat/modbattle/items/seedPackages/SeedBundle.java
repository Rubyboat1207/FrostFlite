package rubyboat.modbattle.items.seedPackages;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeedBundle extends Item {
    public BlockItem seed;
    public static final String KEY = "seed_count";

    public SeedBundle(Settings settings, BlockItem seed) {
        super(settings);
        this.seed = seed;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        ItemStack prevItemStack = cursorStackReference.get().copy();
        if(cursorStackReference.get().isOf(seed)) {
            stack.getOrCreateNbt().putInt(KEY, stack.getOrCreateNbt().getInt(KEY) + (cursorStackReference.get().getCount() / (clickType == ClickType.RIGHT ? 2 : 1)));
            cursorStackReference.get().decrement(cursorStackReference.get().getCount() / (clickType == ClickType.RIGHT ? 2 : 1));
        }
        return prevItemStack.isOf(seed);
    }

    public static void decrementSeeds(ItemStack stack, int count) {
        stack.getOrCreateNbt().putInt(KEY, stack.getOrCreateNbt().getInt(KEY) - count);
    }
    public static int getSeeds(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(KEY);
    }
    public static void setSeeds(ItemStack stack, int count) {
        stack.getOrCreateNbt().putInt(KEY, count);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("Contains: " + stack.getOrCreateNbt().getInt(KEY) + " seeds"));
    }
}
