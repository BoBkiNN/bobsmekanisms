package xyz.bobkinn.bobsmekanisms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class ElectricLampBE extends BlockEntity {

    public ElectricLampBE(BlockPos pos, BlockState state) {
        super(BobsMekanisms.ELECTRIC_LAMP_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        boolean hasPower = false;

        for (Direction dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(dir));
            if (neighbor != null) {
                LazyOptional<IEnergyStorage> cap = neighbor.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite());
                if (cap.isPresent()) {
                    IEnergyStorage energy = cap.orElse(null);
                    if (energy.extractEnergy(5, true) >= 5) {
                        energy.extractEnergy(5, false); // consume
                        hasPower = true;
                        break;
                    }
                }
            }
        }

        if (state.getValue(BlockStateProperties.LIT) != hasPower) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, hasPower), Block.UPDATE_ALL);
        }
    }
}
