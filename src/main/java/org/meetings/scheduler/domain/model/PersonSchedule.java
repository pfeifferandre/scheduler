package org.meetings.scheduler.domain.model;

import lombok.*;
import org.meetings.scheduler.domain.dto.PersonSchedulesDTO;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "PersonSchedules", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}
)
public class PersonSchedule {
    @Id
    private String name;
    private String role;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "person",cascade = CascadeType.ALL,targetEntity = Dates.class)
    private Set<Dates> dates;

    public static class PersonScheduleBuilder{
        public static PersonSchedule buildFromPersonScheduleDTO(PersonSchedulesDTO dto){
            PersonSchedule person = PersonSchedule.builder()
                    .name(dto.getName())
                    .role(dto.getRole())
                    .build();
            Set<Dates> dates = dto.getDates().stream().map(date -> {return Dates.DatesBuilder.buildFromDatesDTO(date,person);}).collect(Collectors.toSet());
            person.setDates(dates);
            return person;
        }
    }
}
