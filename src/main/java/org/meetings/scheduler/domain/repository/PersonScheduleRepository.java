package org.meetings.scheduler.domain.repository;

import org.meetings.scheduler.domain.model.PersonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonScheduleRepository extends JpaRepository<PersonSchedule,String> {
}
