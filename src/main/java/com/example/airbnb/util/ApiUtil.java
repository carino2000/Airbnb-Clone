package com.example.airbnb.util;

import com.example.airbnb.dto.api.HolidayItem;
import com.example.airbnb.dto.api.Holidays;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ApiUtil {

    final ObjectMapper objectMapper;

    public boolean holidayCheck(LocalDate date) {
        LocalDate targetDate = date.plusDays(1);

        if (targetDate.getDayOfWeek().getValue() > 5) {
            return true;
        }

        try {
            String apiUrl = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?" +
                    "solYear=" + targetDate.getYear() + "&solMonth=" + targetDate.getMonthValue() + "&_type=json&numOfRows=30&ServiceKey=8665ebe7da50e80017fbae8933544152314ae4a66c83f5bc3b4dbb61164f1fe2";

            RestClient restClient = RestClient.create();
            String json = restClient.get().uri(apiUrl).retrieve().body(String.class);
            if (json == null || json.isBlank()) {
                throw new IllegalStateException("holiday check failed");
            }

            JsonNode root = objectMapper.readTree(json);
            JsonNode itemNode = root
                    .path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            if (itemNode.isMissingNode() || itemNode.isNull() || itemNode.isEmpty()) {
                return false;
            }

            Holidays holidays = new Holidays();
            if (!itemNode.isArray()) {
                HolidayItem item =
                        objectMapper.convertValue(itemNode, HolidayItem.class);
                holidays.setItem(new HolidayItem[]{item});
            } else {
                HolidayItem[] items =
                        objectMapper.convertValue(
                                itemNode,
                                HolidayItem[].class
                        );
                holidays.setItem(items);
            }

            for(HolidayItem item : holidays.getItem()){
                if(item.toLocalDate().equals(targetDate) && item.getIsHoliday().equals("Y")){
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}
