package ru.vlsu.ispi.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        if (source == null || source.isEmpty()) {
            return 0; // значение по умолчанию для примитивного int
        }
        return Integer.valueOf(source);
    }
}
