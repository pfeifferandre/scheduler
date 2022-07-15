package org.meetings.scheduler.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meetings.scheduler.domain.dto.DatesDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "dates")
public class Dates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "dateId", updatable=false, nullable=false)
    private long id;
    private String weekDay;
    @ElementCollection(targetClass=Integer.class)
    private Set<Integer> times;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name", nullable = false)
    private PersonSchedule person;

    public Dates getIntersectionTimes(Dates secondDate){
        return DatesBuilder.buildIntersection(this, secondDate);
    }
    public static class DatesBuilder{

        private static Dates buildIntersection(Dates firstDate, Dates secondDate){
            Dates intersection = new Dates();
            intersection.setWeekDay(firstDate.getWeekDay());
            Set<Integer> possibleTimes = new HashSet<>(firstDate.getTimes());
            possibleTimes.retainAll(secondDate.getTimes());
            intersection.setTimes(possibleTimes);
            intersection.setPerson(secondDate.getPerson());
            return intersection;
        }
        public static Dates buildFromDatesDTO(DatesDTO dto, PersonSchedule person){
            return Dates.builder()
                    .person(person)
                    .weekDay(dto.getWeekDay())
                    .times(dto.getTimes())
                    .build();
        }
    }
}
