package com.softserve.service;

import com.softserve.entity.Teacher;
import com.softserve.entity.TeacherWishes;
import com.softserve.entity.Wish;
import com.softserve.entity.Wishes;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.entity.enums.WishStatuses;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.IncorrectWishException;
import com.softserve.repository.TeacherWishesRepository;
import com.softserve.service.impl.TeacherWishesServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class TeacherWishesServiceTest {

    @Mock
    private TeacherWishesRepository teacherWishesRepository;

    @InjectMocks
    private TeacherWishesServiceImpl teacherWishesService;

    @Test
    public void getTeacherWishesById() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.EVEN);
        wishes.setWishes(wishList);
        Wishes[] wishesArray = {wishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);

        when(teacherWishesRepository.findById(1L)).thenReturn(Optional.of(teacherWishes));

        TeacherWishes result = teacherWishesService.getById(1L);
        assertNotNull(result);
        assertEquals(teacherWishes.getId(), result.getId());
        verify(teacherWishesRepository, times(1)).findById(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfTeacherWishesNotFounded() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.EVEN);
        wishes.setWishes(wishList);
        Wishes[] wishesArray = {wishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);

        teacherWishesService.getById(2L);
        verify(teacherWishesRepository, times(1)).findById(2L);
    }

    @Test
    public void saveTeacherWishesIfTeacherDoesNotExistsAndClassesAreUniqueAndUniqueDayWithEvenOddAndEvenOddWithoutWeeklyInSameTime() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.EVEN);
        wishes.setWishes(wishList);
        Wishes[] wishesArray = {wishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);

        when(teacherWishesRepository.countWishesByTeacherId(teacher.getId())).thenReturn(0L);
        when(teacherWishesRepository.save(teacherWishes)).thenReturn(teacherWishes);

        TeacherWishes result = teacherWishesService.save(teacherWishes);
        assertNotNull(result);
        assertEquals(teacherWishes.getId(), result.getId());
        assertEquals(teacherWishes.getTeacher(), result.getTeacher());
        verify(teacherWishesRepository, times(1)).save(teacherWishes);
        verify(teacherWishesRepository, times(1)).countWishesByTeacherId(1L);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void throwEntityAlreadyExistsExceptionIfTeacherAlreadyExists() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.EVEN);
        wishes.setWishes(wishList);
        Wishes[] wishesArray = {wishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);

        when(teacherWishesRepository.countWishesByTeacherId(1L)).thenReturn(1L);

        teacherWishesService.save(teacherWishes);
        verify(teacherWishesRepository, times(1)).save(teacherWishes);
        verify(teacherWishesRepository, times(1)).countWishesByTeacherId(1L);
    }

    @Test(expected = IncorrectWishException.class)
    public void throwIncorrectWishExceptionIfClassesAreNotUnique() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        Wish anotherWish = new Wish();
        anotherWish.setClassName("1 para");
        anotherWish.setStatus(WishStatuses.BAD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        wishList.add(anotherWish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.EVEN);
        wishes.setWishes(wishList);
        Wishes anotherWishes = new Wishes();
        anotherWishes.setWishes(wishList);
        anotherWishes.setDayOfWeek(DayOfWeek.MONDAY);
        anotherWishes.setEvenOdd(EvenOdd.ODD);
        Wishes[] wishesArray = {wishes, anotherWishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);

        when(teacherWishesRepository.countWishesByTeacherId(1L)).thenReturn(0L);

        teacherWishesService.save(teacherWishes);
        verify(teacherWishesRepository, times(1)).save(teacherWishes);
        verify(teacherWishesRepository, times(1)).countWishesByTeacherId(1L);
    }

    @Test(expected = IncorrectWishException.class)
    public void throwIncorrectWishExceptionIfDayAndEvenOddAreNotUnique() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        Wish anotherWish = new Wish();
        anotherWish.setClassName("2 para");
        anotherWish.setStatus(WishStatuses.BAD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        wishList.add(anotherWish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.WEEKLY);
        wishes.setWishes(wishList);
        Wishes anotherWishes = new Wishes();
        anotherWishes.setWishes(wishList);
        anotherWishes.setDayOfWeek(DayOfWeek.MONDAY);
        anotherWishes.setEvenOdd(EvenOdd.WEEKLY);
        Wishes[] wishesArray = {wishes, anotherWishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);

        when(teacherWishesRepository.countWishesByTeacherId(1L)).thenReturn(0L);

        teacherWishesService.save(teacherWishes);
        verify(teacherWishesRepository, times(1)).save(teacherWishes);
        verify(teacherWishesRepository, times(1)).countWishesByTeacherId(1L);
    }

    @Test(expected = IncorrectWishException.class)
    public void throwIncorrectWishExceptionIfEvenOddWithWeeklyInSameTime() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.EVEN);
        Wishes anotherWishes = new Wishes();
        anotherWishes.setWishes(wishList);
        anotherWishes.setDayOfWeek(DayOfWeek.MONDAY);
        anotherWishes.setEvenOdd(EvenOdd.WEEKLY);
        wishes.setWishes(wishList);
        Wishes[] wishesArray = {wishes, anotherWishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);

        when(teacherWishesRepository.countWishesByTeacherId(1L)).thenReturn(0L);

        teacherWishesService.save(teacherWishes);
        verify(teacherWishesRepository, times(1)).save(teacherWishes);
        verify(teacherWishesRepository, times(1)).countWishesByTeacherId(1L);
    }

    @Test
    public void updateTeacherWishesIfTeacherDoesNotExistsAndClassesAreUniqueAndUniqueDayWithEvenOddAndEvenOddWithoutWeeklyInSameTime() {
        Wish wish = new Wish();
        wish.setClassName("1 para");
        wish.setStatus(WishStatuses.GOOD);
        List<Wish> wishList = new ArrayList<>();
        wishList.add(wish);
        Wishes wishes = new Wishes();
        wishes.setDayOfWeek(DayOfWeek.MONDAY);
        wishes.setEvenOdd(EvenOdd.EVEN);
        wishes.setWishes(wishList);
        Wishes[] wishesArray = {wishes};
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherWishes teacherWishes = new TeacherWishes();
        teacherWishes.setId(1L);
        teacherWishes.setTeacher(teacher);
        teacherWishes.setTeacherWishesList(wishesArray);


        when(teacherWishesRepository.update(teacherWishes)).thenReturn(teacherWishes);

        TeacherWishes result = teacherWishesService.update(teacherWishes);
        assertNotNull(result);
        assertEquals(result, teacherWishes);
        verify(teacherWishesRepository, times(1)).update(teacherWishes);
    }
}
