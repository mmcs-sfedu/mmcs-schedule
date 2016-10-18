package com.nolan.mmcs_schedule.utils;

import java.io.Serializable;

/**
 * I'm not sure this class is required. It added one during debugging
 * serialization issue. So if you have time
 * TODO: try to substitute this with mere pair.
 */
public class PairSerializable<F extends Serializable, S extends Serializable> {
    public final F first;
    public final S second;

    public PairSerializable(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
