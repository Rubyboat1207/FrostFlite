package rubyboat.modbattle.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rubyboat.modbattle.Main;

public class WinterSkateItem extends Item {
    public WinterSkateItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.addVelocity(0, 2, 0);
        user.damage(Main.ELECTROCUTED, 6);
        return super.finishUsing(stack, world, user);
    }
}
