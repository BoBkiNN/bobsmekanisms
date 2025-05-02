package xyz.bobkinn.bobsmekanisms;

import net.minecraftforge.energy.EnergyStorage;

public class LampEnergyStorage extends EnergyStorage {
    public LampEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void decrease(int by) {
        energy = Math.max(0, energy-by);
    }
}
