package com.indref.industrial_reforged.content.transportation;

import com.indref.industrial_reforged.api.transportation.TransportingHandler;
import com.indref.industrial_reforged.util.Utils;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.List;

public class EnergyTransportingHandler implements TransportingHandler<Integer> {
    public static final EnergyTransportingHandler INSTANCE = new EnergyTransportingHandler();

    private EnergyTransportingHandler() {
    }

    @Override
    public Integer defaultValue() {
        return 0;
    }

    @Override
    public boolean validTransportValue(Integer value) {
        return value > 0;
    }

    @Override
    public List<Integer> split(Integer value, int amount) {
        int[] split = Utils.splitNumberEvenly(value, amount);
        return IntList.of(split);
    }

    @Override
    public Integer join(Integer value0, Integer value1) {
        if (value1 > 0 && value0 > Integer.MAX_VALUE - value1) {
            return null;
        } else if (value1 < 0 && value0 < Integer.MIN_VALUE - value1) {
            return null;
        }
        return value0 + (int) value1;
    }

    @Override
    public Integer remove(Integer value, Integer toRemove) {
        return Math.max(value - toRemove, defaultValue());
    }

}
