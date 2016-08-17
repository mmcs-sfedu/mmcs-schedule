package com.nolan.mmcs_schedule.repository.api.primitives;

import java.io.Serializable;
import java.util.ArrayList;

public class RawRoom
    implements Serializable {

    public static class List extends ArrayList<RawRoom> { }

    private int    id   = -1;
    private String name = null;

    public RawRoom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
