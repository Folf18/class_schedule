package com.softserve.service;

import com.softserve.entity.Group;
import com.softserve.entity.Lesson;
import com.softserve.entity.Subject;
import com.softserve.entity.Teacher;
import com.softserve.entity.enums.LessonType;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.repository.LessonRepository;
import com.softserve.service.impl.LessonServiceImpl;
import com.softserve.service.impl.SubjectServiceImpl;
import com.softserve.service.impl.TeacherServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class LessonServiceTest {
    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TeacherServiceImpl teacherService;

    @Mock
    private SubjectServiceImpl subjectService;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Test
    public void getLessonById() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setTeacherForSite("Ivanov I.I.");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        Lesson result = lessonService.getById(1L);
        assertNotNull(result);
        assertEquals(lesson, result);
        verify(lessonRepository, times(1)).findById(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfLessonNotFoundedById() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setTeacherForSite("Ivanov I.I.");

        lessonService.getById(2L);
        verify(lessonRepository, times(1)).findById(2L);
    }

    @Test
    public void saveLessonIfDuplicatesDoesNotExists() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("");
        lesson.setTeacherForSite("");

        when(lessonRepository.countLessonDuplicates(lesson)).thenReturn(0L);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(teacherService.getById(teacher.getId())).thenReturn(teacher);
        when(subjectService.getById(subject.getId())).thenReturn(subject);

        Lesson result = lessonService.save(lesson);
        assertNotNull(result);
        assertEquals(lesson, result);
        verify(lessonRepository, times(1)).countLessonDuplicates(lesson);
        verify(lessonRepository, times(1)).save(lesson);
        verify(teacherService, times(1)).getById(teacher.getId());
        verify(subjectService, times(1)).getById(subject.getId());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void throwEntityAlreadyExistsExceptionIfSaveLessonWithSameTeacherSubjectGroupLessonType() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setTeacherForSite("Ivanov I.I.");

        when(lessonRepository.countLessonDuplicates(lesson)).thenReturn(1L);

        lessonService.save(lesson);
        verify(lessonRepository, times(1)).countLessonDuplicates(lesson);
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    public void updateLessonIfItDoesNotEqualsWithExistsLessons() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setTeacherForSite("Ivanov I.I.");

        when(lessonRepository.countLessonDuplicates(lesson)).thenReturn(0L);
        when(lessonRepository.update(lesson)).thenReturn(lesson);

        Lesson result = lessonService.update(lesson);
        assertNotNull(result);
        assertEquals(lesson, result);
        verify(lessonRepository, times(1)).countLessonDuplicates(lesson);
        verify(lessonRepository, times(1)).update(lesson);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void throwEntityAlreadyExistsExceptionIfUpdatedLessonEqualsWithExistsLessons() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setTeacherForSite("Ivanov I.I.");

        when(lessonRepository.countLessonDuplicates(lesson)).thenReturn(1L);

        lessonService.update(lesson);
        verify(lessonRepository, times(1)).countLessonDuplicates(lesson);
        verify(lessonRepository, times(1)).update(lesson);
    }
}
