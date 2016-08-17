package com.nolan.mmcs_schedule.repository.api.primitives;

import java.io.Serializable;
import java.util.List;

public class RawCurriculaOfLesson
    implements Serializable {

    private RawLesson           lesson    = null;
    private List<RawCurriculum> curricula = null;

    public RawCurriculaOfLesson(RawLesson lesson, List<RawCurriculum> curricula) {
        this.lesson = lesson;
        this.curricula = curricula;
    }

    public RawLesson getLesson() {
        return lesson;
    }

    public List<RawCurriculum> getCurricula() {
        return curricula;
    }
}
