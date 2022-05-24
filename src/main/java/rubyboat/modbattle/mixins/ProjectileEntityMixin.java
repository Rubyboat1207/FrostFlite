package rubyboat.modbattle.mixins;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rubyboat.modbattle.Main;
import rubyboat.modbattle.customBlocks.PykreteBlock;
import rubyboat.modbattle.items.FarmingElytraFeatureRenderer;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {
    @Shadow public abstract void setVelocity(double x, double y, double z, float speed, float divergence);

    @Shadow private @Nullable Entity owner;

    @Inject(at = @At("HEAD"), method = "onBlockHit", cancellable = true)
    private void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        if(((ProjectileEntity)(Object)this).getWorld().getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof PykreteBlock || ((ProjectileEntity)(Object)this).getWorld().getBlockState(blockHitResult.getBlockPos()).getBlock() == Main.HARDENED_PYKRETE_LOG_BLOCK) {
            /*
            Quaternion q = blockHitResult.getSide().getRotationQuaternion();
            var yaw = Math.atan2(2.0*(q.getY()*q.getZ() + q.getW()*q.getX()), q.getW()*q.getW() - q.getX()*q.getX() - q.getY()*q.getY() + q.getZ()*q.getZ());
            var pitch = Math.atan(-2.0*(q.getX()*q.getZ() - q.getW()*q.getY()));
            var roll = Math.atan2(2.0*(q.getX()*q.getY() + q.getW()*q.getZ()), q.getW()*q.getW() + q.getX()*q.getX() - q.getY()*q.getY() - q.getZ()*q.getZ());
            setVelocity(yaw, pitch, roll, ((ProjectileEntity)(Object)this).speed, 0);
            */

            if(owner != null) {
                owner.damage(Main.DEFLECTED, 10);
                if(owner.isPlayer()) {
                    PlayerEntity player = ((PlayerEntity) owner);
                    player.setStuckArrowCount(player.getStuckArrowCount() + 1);
                }
            }
            ((ProjectileEntity)(Object)this).discard();
        }
    }
}
