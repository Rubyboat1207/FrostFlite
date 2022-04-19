package rubyboat.modbattle.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import rubyboat.modbattle.Main;

@Environment(EnvType.CLIENT)
public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), Main.BROCCOLI_PLANT);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), Main.GROWING_PLOT_BLOCK);
    }
}
