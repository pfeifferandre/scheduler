package org.meetings.scheduler.service.implementation;

import org.meetings.scheduler.domain.dto.DatesDTO;
import org.meetings.scheduler.domain.dto.PersonSchedulesDTO;
import org.meetings.scheduler.domain.model.Dates;
import org.meetings.scheduler.domain.model.PersonSchedule;
import org.meetings.scheduler.domain.repository.DatesRepository;
import org.meetings.scheduler.domain.repository.PersonScheduleRepository;
import org.meetings.scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService<PersonSchedulesDTO, DatesDTO, String> {

    @Autowired
    private DatesRepository datesRepository;
    @Autowired
    private PersonScheduleRepository personScheduleRepository;
    @Override
    public boolean insertSchedule(PersonSchedulesDTO schedule) {
        Optional<PersonSchedule> existPerson = personScheduleRepository.findById(schedule.getName());

        if(existPerson.isPresent()){
            updateSchedule(
                    existPerson.get(),
                    castDTOToDomain(schedule).getDates()
            );
            return true;
        }

        return newPerson(schedule);
     }

    private boolean newPerson(PersonSchedulesDTO schedule){
        PersonSchedule person = personScheduleRepository.save(castDTOToDomain(schedule));
        return person.getName().equalsIgnoreCase(schedule.getName());
    }

    private PersonSchedule castDTOToDomain(PersonSchedulesDTO schedule) {
        return PersonSchedule.PersonScheduleBuilder.buildFromPersonScheduleDTO(schedule);
    }

    private void updateSchedule(PersonSchedule person, Set<Dates> newSchedule){
        Map<String,Dates> currentSchedule = getPersonDatesAsMap(person);
        newSchedule.forEach(day->{
            if(currentSchedule.containsKey(day.getWeekDay())){
                day.setId(currentSchedule.get(day.getWeekDay()).getId());
            }
            datesRepository.save(day);
        });
    }

    private Map<String,Dates> getPersonDatesAsMap(PersonSchedule person){
        return person.getDates()
                .stream()
                .collect(
                        Collectors.toMap(
                                Dates::getWeekDay,
                                date -> date,
                                (weekDay1,weekDay2) -> weekDay1
                        )
                );
    }

    @Override
    public List<DatesDTO> verifyScheduleOptions(String firstPerson, String secondPerson) {
        Optional<PersonSchedule> first = personScheduleRepository.findById(firstPerson);
        Optional<PersonSchedule> second = personScheduleRepository.findById(secondPerson);
        List<DatesDTO> scheduleOptions = new ArrayList<>();

        first.ifPresent(person
                -> second.ifPresent(
                        otherPerson
                        -> {
                                Map<String,Dates> firstPersonDates = this.getPersonDatesAsMap(person);
                                Map<String,Dates> otherPersonDates = this.getPersonDatesAsMap(otherPerson);
                                scheduleOptions.addAll(
                                    getListOfCompatibleDates(firstPersonDates,otherPersonDates)
                            );
                        }
                )
        );
        return scheduleOptions;
    }

    @Override
    public List<DatesDTO> verifyScheduleOptionsWithMultiplePersons(String person, List<String> others) {
        List<DatesDTO> scheduleOptions = new ArrayList<>();
        others.forEach(someone -> scheduleOptions.addAll(verifyScheduleOptions(person,someone)));
        return scheduleOptions;
    }

    private List<DatesDTO> getListOfCompatibleDates(Map<String, Dates> firstPersonDates, Map<String,Dates> secondPersonDates){
        List<DatesDTO> scheduleOptions = new ArrayList<>();
        firstPersonDates.forEach(
                (key,value)
                        -> scheduleOptions.add(
                                DatesDTO.DatesDTOBuilder.buildFromDatesDTO(
                                        Objects.requireNonNull(
                                                secondPersonDates.computeIfPresent(key, (matchingKey, matchingValue) -> value.getIntersectionTimes(matchingValue))
                                        )
                                )
                )
        );

        return scheduleOptions;
    }
}
