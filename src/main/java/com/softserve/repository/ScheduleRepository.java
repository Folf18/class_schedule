package com.softserve.repository;

import com.softserve.entity.*;
import com.softserve.entity.enums.EvenOdd;


import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends BasicRepository<Schedule, Long> {
    Long conflictForGroupInSchedule(Long semesterId, java.time.DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long groupId);
  Long conflictForTeacherInSchedule(Long semesterId, java.time.DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId, Long teacherId);
  List<Group> uniqueGroupsInScheduleBySemester(Long semesterID);
  List<Period> periodsForGroupByDayBySemester(Long semesterId, Long groupId, DayOfWeek day);
  Optional<Lesson> lessonForGroupByDayBySemesterByPeriodByWeek(Long semesterId, Long groupId, Long periodId, DayOfWeek day, EvenOdd evenOdd);
  Room getRoomForLesson(Long semesterId, Long lessonId, Long periodId, DayOfWeek day, EvenOdd evenOdd);
  List<String> getDaysWhenGroupHasClassesBySemester(Long semesterId, Long groupId);
  Long countSchedulesForGroupInSemester(Long semesterId, Long groupId);

}
