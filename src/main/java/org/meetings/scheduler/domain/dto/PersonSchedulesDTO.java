package org.meetings.scheduler.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.meetings.scheduler.domain.model.PersonSchedule;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonSchedulesDTO {

    @JsonProperty("name")
    private String name;
    @JsonProperty("role")
    private String role;
    @JsonProperty("dates")
    private Set<DatesDTO> dates;

    public static class PersonSchedulesDTOBuilder{
        public static PersonSchedulesDTO buildFromPersonScheduler(PersonSchedule personToCast){

            return PersonSchedulesDTO.builder()
                    .name(personToCast.getName())
                    .role(personToCast.getRole())
                    .build();
        }
    }
}
