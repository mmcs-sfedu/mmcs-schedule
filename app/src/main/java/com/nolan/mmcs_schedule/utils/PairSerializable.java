package com.nolan.mmcs_schedule.utils;

import java.io.Serializable;

public class PairSerializable<F extends Serializable, S extends Serializable> {
    public final F first;
    public final S second;

    public PairSerializable(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
