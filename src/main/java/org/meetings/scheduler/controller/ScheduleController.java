package org.meetings.scheduler.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Inserts new schedules for a single person",
    notes = "Receives a Json containing data to insert or update a person.")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseEntity insertSchedules(
            @ApiParam(value = "Data representing the schedule of a new or existing person",
                    required = true)
            @RequestBody PersonSchedulesDTO schedule){
        return scheduleService.insertSchedule(schedule)?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Gets a list of times that 2 persons could meet",
            notes = "Compares 2 person schedules and return the available dates in common for both.")
    @RequestMapping(value = "/verify/meeting/{firstPerson}/{secondPerson}", method = RequestMethod.GET)
    public ResponseEntity<List<DatesDTO>> verifyScheduleOptions(
            @ApiParam(value = "Name of a person to search schedules",
                    required = true)
            @PathVariable(name = "firstPerson") String firstPerson,
            @ApiParam(value = "Name of a person to compare the schedule",
                    required = true)
            @PathVariable(name = "secondPerson") String secondPerson){
        return new ResponseEntity<>(scheduleService.verifyScheduleOptions(firstPerson, secondPerson),HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a list of times that a person could meet any of the others given persons",
            notes = "Compares the given person schedule with all other persons and return each person availability.")
    @RequestMapping(value = "/verify/meeting/{person}", method = RequestMethod.GET)
    public ResponseEntity<List<DatesDTO>> verifyScheduleOptionsWithMultiplePersons(
            @ApiParam(value = "Name of a person to search schedules",
                    required = true)
            @PathVariable(name = "person") String firstPerson,
            @ApiParam(value = "A list containing everyone that should be compared with the provided person",
                    required = true)
            @RequestBody List<String> listOfPersons){
        return new ResponseEntity<>(scheduleService.verifyScheduleOptionsWithMultiplePersons(firstPerson, listOfPersons),HttpStatus.OK);
    }
}
