package com.softserve.controller;

import com.softserve.dto.*;
import com.softserve.entity.Schedule;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.mapper.ScheduleWithoutSemesterMapper;
import com.softserve.mapper.SemesterMapper;
import com.softserve.service.ScheduleService;
import com.softserve.mapper.ScheduleMapper;
import com.softserve.mapper.ScheduleSaveMapper;
import com.softserve.service.SemesterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@Api(tags = "Schedule API")
@Slf4j
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SemesterService semesterService;
    private final SemesterMapper semesterMapper;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleSaveMapper scheduleSaveMapper;
    private final ScheduleWithoutSemesterMapper scheduleWithoutSemesterMapper;

    public ScheduleController(ScheduleService scheduleService, SemesterService semesterService, SemesterMapper semesterMapper, ScheduleMapper scheduleMapper, ScheduleSaveMapper scheduleSaveMapper, ScheduleWithoutSemesterMapper scheduleWithoutSemesterMapper) {
        this.scheduleService = scheduleService;
        this.semesterService = semesterService;
        this.semesterMapper = semesterMapper;
        this.scheduleMapper = scheduleMapper;
        this.scheduleSaveMapper = scheduleSaveMapper;
        this.scheduleWithoutSemesterMapper = scheduleWithoutSemesterMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get the list of all schedules")
    public ResponseEntity<List<ScheduleDTO>> list() {
        log.info("In list()");
        List<Schedule> schedules = scheduleService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(scheduleMapper.scheduleToScheduleDTOs(schedules));
    }

    @GetMapping("/semester")
    @ApiOperation(value = "Get the list of all schedules")
    public ResponseEntity<List<ScheduleWithoutSemesterDTO>> listForSemester(@RequestParam Long semesterId) {
        log.info("In listForSemester()");
        List<Schedule> schedules = scheduleService.getSchedulesBySemester(semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleWithoutSemesterMapper.scheduleToScheduleWithoutSemesterDTOs(schedules));
    }

    @GetMapping("/data-before")
    @ApiOperation(value = "Get the info for finishing creating the schedule")
    public ResponseEntity<CreateScheduleInfoDTO> getInfoForCreatingSchedule(@RequestParam Long semesterId,
                                                                            @RequestParam DayOfWeek dayOfWeek,
                                                                            @RequestParam EvenOdd evenOdd,
                                                                            @RequestParam Long classId,
                                                                            @RequestParam Long lessonId){
        log.info("In getInfoForCreatingSchedule(semesterId = [{}], dayOfWeek = [{}], evenOdd = [{}], classId = [{}], lessonId = [{}])", semesterId, dayOfWeek, evenOdd, classId, lessonId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getInfoForCreatingSchedule(semesterId, dayOfWeek,evenOdd, classId, lessonId));
    }

    @GetMapping("/full/groups")
    @ApiOperation(value = "Get full schedule for groupId in some semester")
    public ResponseEntity<ScheduleFullDTO> getFullScheduleForGroup(@RequestParam Long semesterId,
                                                                     @RequestParam Long groupId) {
        log.info("In, getFullScheduleForGroup (semesterId = [{}], groupId = [{}]) ", semesterId, groupId);
        ScheduleFullDTO scheduleFullDTO = new ScheduleFullDTO();
        scheduleFullDTO.setSemester(semesterMapper.semesterToSemesterDTO(semesterService.getById(semesterId)));
        scheduleFullDTO.setSchedule(scheduleService.getFullScheduleForGroup(semesterId, groupId));
        return ResponseEntity.status(HttpStatus.OK).body(scheduleFullDTO);
    }

    @GetMapping("/full/semester")
    @ApiOperation(value = "Get full schedule for semester")
    public ResponseEntity<ScheduleFullDTO> getFullScheduleForSemester(@RequestParam Long semesterId) {
        log.info("In, getFullScheduleForGroup (semesterId = [{}]) ", semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getFullScheduleForSemester(semesterId));
    }

    @GetMapping("/full/teachers")
    @ApiOperation(value = "Get full schedule for teacher by semester")
    public ResponseEntity<ScheduleForTeacherDTO> getFullScheduleForTeacher(@RequestParam Long semesterId,
                                                                             @RequestParam Long teacherId) {
        log.info("In, getFullScheduleForTeacher (semesterId = [{}], teacherId = [{}]) ", semesterId, teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getScheduleForTeacher(semesterId, teacherId));
    }

    @GetMapping("/full/rooms")
    @ApiOperation(value = "Get full schedule for semester. Returns schedule for  rooms")
    public ResponseEntity<List<ScheduleForRoomDTO>> getFullScheduleForRoom(@RequestParam Long semesterId) {
        log.info("In, getFullScheduleForRoom (semesterId = [{}]) ", semesterId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getScheduleForRooms(semesterId));
    }


    @PostMapping
    @ApiOperation(value = "Create new schedule")
    public ResponseEntity<ScheduleSaveDTO> save(@RequestBody ScheduleSaveDTO scheduleSaveDTO) {
        log.info("In save(scheduleSaveDTO = [{}])", scheduleSaveDTO);
        Schedule schedule = scheduleService.save(scheduleSaveMapper.scheduleSaveDTOToSchedule(scheduleSaveDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleSaveMapper.scheduleToScheduleSaveDTO(schedule));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete schedule by id")
    public ResponseEntity delete(@PathVariable("id") long id){
        log.info("In delete(id =[{}]", id);
        Schedule schedule = scheduleService.getById(id);
        scheduleService.delete(schedule);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
