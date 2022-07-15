package org.meetings.scheduler.service;

import java.util.List;

public interface ScheduleService<P,D,I> {
    boolean insertSchedule(P schedule);
    List<D> verifyScheduleOptions(I firstPerson, I secondPerson);

    List<D> verifyScheduleOptionsWithMultiplePersons(I person, List<I> others);
}
