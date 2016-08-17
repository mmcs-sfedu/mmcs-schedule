package com.nolan.mmcs_schedule.repository.api.primitives;

import java.io.Serializable;
import java.util.ArrayList;

public class RawSubject
    implements Serializable {

    public static class List extends ArrayList<RawSubject> { }

    private int    id   = -1;
    private String name = null;
    private String abbr = null;

    public RawSubject(int id, String name, String abbr) {
        this.id = id;
        this.name = name;
        this.abbr = abbr;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }
}
