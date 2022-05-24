package rubyboat.modbattle.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rubyboat.modbattle.Main;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {
    @Inject(at = @At("HEAD"), method = "isMovable", cancellable = true)
    private static void isMovable(BlockState state, World world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
        if(state.getBlock() == Main.HARDENED_PYKRETE_LOG_BLOCK) {
            cir.setReturnValue(false);
        }
    }
}
