package rubyboat.modbattle.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class AntiFreezeEffect extends StatusEffect {
    public AntiFreezeEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xFFFFFF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setFrozenTicks(0);
    }
}
