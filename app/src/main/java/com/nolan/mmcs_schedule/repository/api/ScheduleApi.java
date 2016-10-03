package com.nolan.mmcs_schedule.repository.api;

import com.nolan.mmcs_schedule.repository.api.primitives.RawCurriculaOfLesson;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGrade;
import com.nolan.mmcs_schedule.repository.api.primitives.RawGroup;
import com.nolan.mmcs_schedule.repository.api.primitives.RawRoom;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfGroup;
import com.nolan.mmcs_schedule.repository.api.primitives.RawScheduleOfTeacher;
import com.nolan.mmcs_schedule.repository.api.primitives.RawSubject;
import com.nolan.mmcs_schedule.repository.api.primitives.RawTeacher;
import com.nolan.mmcs_schedule.repository.api.primitives.RawTimeSlot;
import com.nolan.mmcs_schedule.repository.api.primitives.RawWeek;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * This interface represents this API: <a>http://users.mmcs.sfedu.ru/~schedule/restapi</a>.
 * It's used by Retrofit. See <a>http://square.github.io/retrofit</a> for more information.
 */
public interface ScheduleApi {

    @GET("/APIv0/grade/list")
    RawGrade.List getGrades();

    @GET("/APIv0/group/list/{gradeID}")
    RawGroup.List getGroups(@Path("gradeID") int gradeId);

    @GET("/APIv0/group/forUber/{uberID}")
    RawGroup.List getGroupsOfUberGroup(@Path("uberID") int uberId);

    @GET("/APIv0/room/list")
    RawRoom.List getRooms();

    @GET("/APIv0/schedule/lesson/{ID}")
    RawCurriculaOfLesson getCurriculaForLesson(@Path("ID") int id);

    @GET("/APIv0/schedule/group/{ID}")
    RawScheduleOfGroup getScheduleOfGroup(@Path("ID") int id);

    @GET("/APIv0/schedule/teacher/{ID}")
    RawScheduleOfTeacher getScheduleOfTeacher(@Path("ID") int id);

    @GET("/APIv0/subject/list")
    RawSubject.List getSubjects();

    @GET("/APIv0/teacher/list")
    RawTeacher.List getTeachers();

    @GET("/APIv0/time/week")
    RawWeek getCurrentWeek();

    @GET("/APIv0/time/list")
    RawTimeSlot.List getTimeSlots();
}