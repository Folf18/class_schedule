package com.softserve.service.impl;
import com.softserve.dto.RoomForScheduleInfoDTO;
import com.softserve.entity.Room;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.repository.RoomRepository;
import com.softserve.service.RoomService;
import com.softserve.mapper.RoomForScheduleInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomForScheduleInfoMapper roomForScheduleInfoMapper;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomForScheduleInfoMapper roomForScheduleInfoMapper) {
        this.roomRepository = roomRepository;
        this.roomForScheduleInfoMapper = roomForScheduleInfoMapper;
    }

    /**
     * The method used for getting room by id
     *
     * @param id Identity number of room
     * @return target room
     * @throws EntityNotFoundException if room doesn't exist
     */
    @Override
    public Room getById(Long id) {
        log.info("Enter into getById of RoomServiceImpl with id {}", id);
        return roomRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Room.class, "id", id.toString())
        );
    }

    /**
     * The method used for getting all rooms
     *
     * @return list of rooms
     */
    @Override
    public List<Room> getAll() {
        log.info("Enter into getAll of RoomServiceImpl");
        return roomRepository.getAll();
    }


    /**
     * The method used for getting all rooms
     *
     * @return list of rooms
     */
    @Override
    public List<Room> getDisabled() {
        log.info("Enter into getAll of getDisabled");
        return roomRepository.getDisabled();
    }

    /**
     * The method used for saving room in database
     *
     * @param object room
     * @return save room
     */
    @Override
    public Room save(Room object) {
        log.info("Enter into save of RoomServiceImpl with entity:{}", object );
        if (isRoomExists(object)){
            throw new EntityAlreadyExistsException("Room with this parameters already exists");
        }
        else {
            return roomRepository.save(object);
        }
    }

    /**
     * The method used for updating existed room in database
     *
     * @param object room
     * @return room before update
     */
    @Override
    public Room update(Room object) {
        log.info("Enter into update of RoomServiceImpl with entity:{}", object);
        if (isRoomExists(object)){
            throw new EntityAlreadyExistsException("Room with this parameters already exists");
        }
        else {
            return roomRepository.update(object);
        }
    }

    /**
     * The method used for deleting existed room in database
     *
     * @param object room
     * @return deleted room
     */
    @Override
    public Room delete(Room object) {
        log.info("Enter into delete of RoomServiceImpl with entity:{}", object);
        return roomRepository.delete(object);
    }

    /**
     * The method used for getting list of free room by specific period, day of week and number of week
     *
     * @param idOfPeriod identity number of period
     * @param dayOfWeek day of the week
     * @param evenOdd    number of week
     * @return list of rooms
     */
    @Override
    public List<Room> freeRoomBySpecificPeriod(Long idOfPeriod, DayOfWeek dayOfWeek, EvenOdd evenOdd) {
        log.info("Enter into freeRoomBySpecificPeriod of RoomServiceImpl with id {}, dayOfWeek {} and evenOdd {} ",
                idOfPeriod, dayOfWeek, evenOdd);
        return roomRepository.freeRoomBySpecificPeriod(idOfPeriod, dayOfWeek, evenOdd);
    }

    @Override
    public List<Room> getNotAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId) {
        return roomRepository.getNotAvailableRoomsForSchedule(semesterId, dayOfWeek, evenOdd, classId);
    }

    @Override
    public List<Room> getAvailableRoomsForSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId) {
        return roomRepository.getAvailableRoomsForSchedule(semesterId, dayOfWeek, evenOdd, classId);
    }

    @Override
    public List<RoomForScheduleInfoDTO> getAllRoomsForCreatingSchedule(Long semesterId, DayOfWeek dayOfWeek, EvenOdd evenOdd, Long classId) {
        List<RoomForScheduleInfoDTO> rooms = roomForScheduleInfoMapper.toRoomForScheduleDTOList(getAvailableRoomsForSchedule(semesterId, dayOfWeek, evenOdd, classId));
        rooms.forEach(roomForScheduleDTO -> roomForScheduleDTO.setAvailable(true));
        rooms.addAll(roomForScheduleInfoMapper.toRoomForScheduleDTOList(getNotAvailableRoomsForSchedule(semesterId, dayOfWeek, evenOdd, classId)));
        return rooms;
    }

    @Override
    public boolean isRoomExists(Room room) {
        return roomRepository.countRoomDuplicates(room) != 0;
    }

    @Override
    public List<Room> getRoomsWithSchedule(Long semesterId) {
        return roomRepository.getRoomsWithSchedule(semesterId);
    }
}
