package com.example.airbnb.dto.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HolidayItem {
    String dateName;
    String isHoliday;
    int locdate;

    public LocalDate toLocalDate() {
        String year = Integer.toString(this.locdate).substring(0,4);
        String month = Integer.toString(this.locdate).substring(4,6);
        String day = Integer.toString(this.locdate).substring(6,8);
        LocalDate date = LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));

        return date;
    }
}
