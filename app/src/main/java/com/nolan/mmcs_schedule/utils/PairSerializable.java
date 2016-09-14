package com.nolan.mmcs_schedule.utils;

public class PairSerializable<F, S> {
    public final F first;
    public final S second;

    public PairSerializable(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
