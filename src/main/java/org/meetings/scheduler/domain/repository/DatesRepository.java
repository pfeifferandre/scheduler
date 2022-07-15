package org.meetings.scheduler.domain.repository;

import org.meetings.scheduler.domain.model.Dates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatesRepository extends JpaRepository<Dates,Long> {
}
