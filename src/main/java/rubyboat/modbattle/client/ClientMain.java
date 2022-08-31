package rubyboat.modbattle.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import rubyboat.modbattle.Main;
import rubyboat.modbattle.items.seedPackages.SeedBundle;

@Environment(EnvType.CLIENT)
public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), Main.BROCCOLI_PLANT);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), Main.WINTERBERRY_BUSH);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), Main.GROWING_PLOT_BLOCK);

        ModelPredicateProviderRegistry.register(Main.POTATO_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(Main.CARROT_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(Main.WHEAT_SEEDS_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(Main.BEETROOT_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(Main.BAMBOO_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(Main.NETHER_WART_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(Main.BROCCOLI_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);
        ModelPredicateProviderRegistry.register(Main.WINTERBERRY_BUNDLE, new Identifier(Main.MOD_ID, "has_items"), (stack, world, entity, seed) -> stack.getOrCreateNbt().getInt(SeedBundle.KEY) > 0 ? 1 : 0);

    }
}
