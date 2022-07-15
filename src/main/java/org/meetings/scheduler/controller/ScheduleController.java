package org.meetings.scheduler.controller;

import io.swagger.annotations.ApiOperation;
import org.meetings.scheduler.domain.dto.DatesDTO;
import org.meetings.scheduler.domain.dto.PersonSchedulesDTO;
import org.meetings.scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheduler")
@ApiOperation("Simple Scheduling API")
public class ScheduleController {

    @Autowired
    private ScheduleService<PersonSchedulesDTO, DatesDTO, String> scheduleService;

    @ApiOperation(value = "Inserts new schedules for a single person")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseEntity insertSchedules(@RequestBody PersonSchedulesDTO schedule){
        return scheduleService.insertSchedule(schedule)?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Gets a list of times that 2 persons could meet")
    @RequestMapping(value = "/verify/meeting/{firstPerson}/{secondPerson}", method = RequestMethod.GET)
    public ResponseEntity<List<DatesDTO>> verifyScheduleOptions(@PathVariable(name = "firstPerson") String firstPerson,
                                                                @PathVariable(name = "secondPerson") String secondPerson){
        return new ResponseEntity<>(scheduleService.verifyScheduleOptions(firstPerson, secondPerson),HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a list of times that a person could meet any of given other persons")
    @RequestMapping(value = "/verify/meeting/{person}", method = RequestMethod.GET)
    public ResponseEntity<List<DatesDTO>> verifyScheduleOptionsWithMultiplePersons(@PathVariable(name = "person") String firstPerson,
                                                                                   @RequestBody List<String> listOfPersons){
        return new ResponseEntity<>(scheduleService.verifyScheduleOptionsWithMultiplePersons(firstPerson, listOfPersons),HttpStatus.OK);
    }
}
