package com.michael.e.liquislots.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class LiquipackTank extends FluidTank {

    private int slot;

    public LiquipackTank(int capacity) {
        super(capacity);
    }

    public LiquipackTank(FluidStack stack, int capacity) {
        super(stack, capacity);
    }

    public LiquipackTank(Fluid fluid, int amount, int capacity) {
        super(fluid, amount, capacity);
    }

    public LiquipackTank(FluidTank tank){
        this(tank.getFluid(), tank.getCapacity());
    }

    public LiquipackTank(int capacity, int slot) {
        super(capacity);
        this.slot = slot;
    }

    public LiquipackTank(FluidStack stack, int capacity, int slot) {
        super(stack, capacity);
        this.slot = slot;
    }

    public LiquipackTank(Fluid fluid, int amount, int capacity, int slot) {
        super(fluid, amount, capacity);
        this.slot = slot;
    }

    public LiquipackTank(FluidTank tank, int slot){
        this(tank.getFluid(), tank.getCapacity());
        this.slot = slot;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Capacity", getCapacity());
        super.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public LiquipackTank readFromNBT(NBTTagCompound nbt) {
        if(!nbt.hasKey("Capacity"))return null;
        capacity = nbt.getInteger("Capacity");
        super.readFromNBT(nbt);
        return this;
    }

    public static LiquipackTank loadFromNBT(NBTTagCompound nbt) {
        if(nbt == null || !nbt.hasKey("Capacity"))return null;
        int capacity = nbt.getInteger("Capacity");
        return new LiquipackTank(capacity).readFromNBT(nbt);
    }
}
