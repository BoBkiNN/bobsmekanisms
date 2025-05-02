package xyz.bobkinn.bobsmekanisms;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(BobsMekanisms.MOD_ID)
public class BobsMekanisms {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "bobsmekanisms";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

    public static final RegistryObject<Block> ELECTRIC_LAMP = BLOCKS.register("electric_lamp", () ->
            new ElectricLampBlock(BlockBehaviour.Properties.of(Material.GLASS)
                    .strength(0.3f)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0))
    );

    public static final RegistryObject<BlockEntityType<ElectricLampBE>> ELECTRIC_LAMP_BE =
            BLOCK_ENTITIES.register("electric_lamp", () ->
                    BlockEntityType.Builder.of(ElectricLampBE::new, ELECTRIC_LAMP.get()).build(null));

    public static final RegistryObject<Item> ELECTRIC_LAMP_ITEM = ITEMS.register("electric_lamp",
            () -> new BlockItem(ELECTRIC_LAMP.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

    public BobsMekanisms(FMLJavaModLoadingContext ctx) {
        IEventBus modEventBus = ctx.getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        modEventBus.register(this);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Force block to be rendered as solid (not cutout/transparent)
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ELECTRIC_LAMP.get(), RenderType.solid());
        });
    }
}
