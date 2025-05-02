package xyz.bobkinn.bobsmekanisms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricLampBE extends BlockEntity {

    private final LampEnergyStorage energyStorage = new LampEnergyStorage(15, 15, 0);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public ElectricLampBE(BlockPos pos, BlockState state) {
        super(BobsMekanisms.ELECTRIC_LAMP_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) {
            boolean lit = energyStorage.getEnergyStored() > 0;
            if (state.getValue(BlockStateProperties.LIT) != lit) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, lit), Block.UPDATE_ALL);
            }
            if (lit) {
                energyStorage.decrease(1);
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        energyStorage.deserializeNBT(tag.get("energy"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy", energyStorage.serializeNBT());
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energy.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        energy = LazyOptional.of(() -> energyStorage);
    }
}
