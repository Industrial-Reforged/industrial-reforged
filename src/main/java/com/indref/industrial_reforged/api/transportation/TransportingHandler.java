package com.indref.industrial_reforged.api.transportation;

import it.unimi.dsi.fastutil.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TransportingHandler<T> {
    /**
     * @return The default value that will be used if a node isn't transporting a value
     */
    @Contract(pure = true)
    T defaultValue();

    /**
     * Checks if {@code value} is valid for transportation
     * @param value the value that should be checked
     * @return whether the value is valid for transportation in the network, otherwise {@code value} will be returned by
     */
    @Contract(pure = true)
    boolean validTransportValue(T value);

    /**
     * Splits {@code value} into the specified {@code amount} of values
     * @param value The value that should be split
     * @param amount The amount that the value needs to split
     * @return the split value as a list of values
     */
    @Contract(pure = true)
    List<T> split(T value, int amount);

    /**
     * Join two values together if possible, otherwise return {@code null}
     * @param value0 the first value to be joined
     * @param value1 the second value to be joined
     * @return the joined value or {@code null} if joining is not possible
     */
    @Contract(pure = true)
    @Nullable T join(T value0, T value1);

    /**
     * Removes {@code toRemove} from {@code value} (used when calculating transport loss)
     * @param value the base value, must not be modified
     * @param toRemove the value to remove from the base value, must not be modified
     * @return {@code value} with {@code toRemove} removed, if {@code value} is completely removed (empty), the returned {@code value} should be {@link TransportingHandler#defaultValue()}
     */
    @Contract(pure = true)
    T remove(T value, T toRemove);
}
