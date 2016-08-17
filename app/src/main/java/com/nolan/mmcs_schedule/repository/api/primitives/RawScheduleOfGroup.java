package com.nolan.mmcs_schedule.repository.api.primitives;

import java.io.Serializable;
import java.util.List;

public class RawScheduleOfGroup
    implements Serializable {

    private List<RawLesson>     lessons   = null;
    private List<RawCurriculum> curricula = null;

    public RawScheduleOfGroup(List<RawLesson> lessons, List<RawCurriculum> curricula) {
        this.lessons = lessons;
        this.curricula = curricula;
    }

    public List<RawLesson> getLessons() {
        return lessons;
    }

    public List<RawCurriculum> getCurricula() {
        return curricula;
    }
}
