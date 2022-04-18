package rubyboat.modbattle.items;

import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class FarmingElytraEquipmentProvider implements EquipmentSlotProvider {
    @Override
    public EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }
}
