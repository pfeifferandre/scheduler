package org.meetings.scheduler.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.meetings.scheduler.domain.model.Dates;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class DatesDTO {

    @JsonProperty("person")
    private String person;
    @JsonProperty("weekday")
    private String weekDay;
    @JsonProperty("times")
    private Set<Integer> times;

    public static class DatesDTOBuilder{
        public static DatesDTO buildFromDatesDTO(Dates dates){
            return DatesDTO.builder()
                    .person(dates.getPerson().getName())
                    .weekDay(dates.getWeekDay())
                    .times(dates.getTimes())
                    .build();
        }
    }
}
