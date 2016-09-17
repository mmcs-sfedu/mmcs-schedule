package com.nolan.mmcs_schedule.repository.primitives;

public class RoomForLesson {
    private int teacherId;
    private String teacherName;
    private String room;

    public RoomForLesson(int teacherId, String teacherName, String room) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.room = room;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getRoom() {
        return room;
    }
}
