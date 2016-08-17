package com.nolan.mmcs_schedule.repository.api.primitives;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("SpellCheckingInspection")
public class RawCurriculum
    implements Serializable {

    private                                  int    id            = -1;
    @SerializedName("lessonid") private      int    lessonId      = -1;
    @SerializedName("subnum") private        int    subNum        = -1;
    @SerializedName("subjectid") private     int    subjectId     = -1;
    @SerializedName("subjectname") private   String subjectName   = null;
    @SerializedName("subjectabbr") private   String subjectAbbr   = null;
    @SerializedName("teacherid") private     int    teacherId     = -1;
    @SerializedName("teachername") private   String teacherName   = null;
    @SerializedName("teacherdegree") private String teacherDegree = null;
    @SerializedName("roomid") private        int    roomId        = -1;
    @SerializedName("roomname") private      String roomName      = null;

    public RawCurriculum(int id, int lessonId, int subNum, int subjectId, String subjectName,
                         String subjectAbbr, int teacherId, String teacherName,
                         String teacherDegree, int roomId, String roomName) {
        this.id = id;
        this.lessonId = lessonId;
        this.subNum = subNum;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectAbbr = subjectAbbr;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherDegree = teacherDegree;
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public int getId() {
        return id;
    }

    public int getLessonId() {
        return lessonId;
    }

    public int getSubNum() {
        return subNum;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectAbbr() {
        return subjectAbbr;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherDegree() {
        return teacherDegree;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }
}
