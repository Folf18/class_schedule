package com.softserve.controller;

import com.softserve.dto.TeacherDTO;
import com.softserve.dto.TeacherWishDTO;
import com.softserve.entity.Teacher;
import com.softserve.service.TeacherService;
import com.softserve.mapper.TeacherMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@Api(tags = "Teacher API")
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping(path = {"/teachers", "/public/teachers"})
    @ApiOperation(value = "Get the list of all teachers")
    public ResponseEntity<List<TeacherDTO>> getAll() {
        log.info("Enter into list method");
        return ResponseEntity.ok(teacherMapper.teachersToTeacherDTOs(teacherService.getAll()));
    }

    @GetMapping("/teachers/{id}")
    @ApiOperation(value = "Get teacher by id")
    public ResponseEntity<TeacherDTO> get(@PathVariable("id") Long id) {
        log.info("Enter into get method with id {} ", id);
        Teacher teacher = teacherService.getById(id);
        return ResponseEntity.ok().body(teacherMapper.teacherToTeacherDTO(teacher));
    }

    @GetMapping("/teachers/with-wishes")
    @ApiOperation(value = "Get the list of all teachers with wishes")
    public ResponseEntity<List<TeacherWishDTO>> getAllWithWishes() {
        log.info("Enter into getAllWithWishes method");
        return ResponseEntity.ok(teacherMapper.toTeacherWithWishesDTOs(teacherService.getAllTeachersWithWishes()));
    }

    @GetMapping("/teachers/{id}/with-wishes")
    @ApiOperation(value = "Get teacher with wish by id")
    public ResponseEntity<TeacherWishDTO> getTeacherWithWishes(@PathVariable("id") Long id) {
        log.info("Enter into getTeacherWithWishes method with id {} ", id);
        Teacher teacher = teacherService.getTeacherWithWishes(id);
        return ResponseEntity.ok().body(teacherMapper.toTeacherWithWishesDTOs(teacher));
    }

    @PostMapping("/teachers")
    @ApiOperation(value = "Create new teacher")
    public ResponseEntity<TeacherDTO> save(@RequestBody TeacherDTO teacherDTO) {
        log.info("Enter into save method with teacherDTO: {}", teacherDTO);
        Teacher teacher = teacherService.save(teacherMapper.teacherDTOToTeacher(teacherDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherMapper.teacherToTeacherDTO(teacher));
    }

    @PutMapping("/teachers")
    @ApiOperation(value = "Update existing teacher by id")
    public ResponseEntity<TeacherDTO> update(@RequestBody TeacherDTO updateTeacherDTO) {
        log.info("Enter into update method with updateTeacherDTO: {}",updateTeacherDTO);
        Teacher teacher = teacherService.update(teacherMapper.teacherDTOToTeacher(updateTeacherDTO));
        return ResponseEntity.status(HttpStatus.OK).body(teacherMapper.teacherToTeacherDTO(teacher));
    }

    @DeleteMapping("/teachers/{id}")
    @ApiOperation(value = "Delete teacher by id")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        log.info("Enter into delete method with  teacher id: {}", id);
        Teacher teacher = teacherService.getById(id);
        teacherService.delete(teacher);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/teachers/disabled")
    @ApiOperation(value = "Get the list of disabled teachers")
    public ResponseEntity<List<TeacherDTO>> getDisabled() {
        log.info("Enter into getDisabled");
        return ResponseEntity.ok(teacherMapper.teachersToTeacherDTOs(teacherService.getDisabled()));
    }

    @GetMapping("/not-registered-teachers")
    @ApiOperation(value = "Get the list of all teachers, that don't registered in system")
    public ResponseEntity<List<TeacherDTO>> getAllNotRegisteredTeachers() {
        log.info("Enter into getAllNotRegisteredTeachers method");
        return ResponseEntity.ok(teacherMapper.teachersToTeacherDTOs(teacherService.getAllTeacherWithoutUser()));
    }
}