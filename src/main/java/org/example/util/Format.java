package org.example.util;

import org.example.dto.CounterReadingDTO;
import org.example.mapper.MapperCR;
import org.example.model.CounterReading;

import java.util.ArrayList;
import java.util.List;

public class Format {
    public static List<CounterReadingDTO> formatter(List<CounterReading> list) {
        var result = new ArrayList<CounterReadingDTO>();
        if (!list.isEmpty()) {
            var etalonYear = list.get(0).getYear();
            var etalonMonth = list.get(0).getMonth();
            var buf = new ArrayList<CounterReading>();
            for (var element : list) {
                if (element.getYear() == etalonYear && element.getMonth() == etalonMonth) {
                    buf.add(element);
                } else {
                    result.add(MapperCR.toDTO(buf));
                    buf.clear();
                    etalonYear = element.getYear();
                    etalonMonth = element.getMonth();
                    buf.add(element);
                }
            }
            result.add(MapperCR.toDTO(buf));
        }
        return result;
    }
}
