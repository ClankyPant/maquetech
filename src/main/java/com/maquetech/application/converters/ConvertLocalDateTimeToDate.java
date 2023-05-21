package com.maquetech.application.converters;

import com.maquetech.application.helpers.DateHelper;
import com.maquetech.application.helpers.LocalDateTimeHelper;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalDateTime;
import java.util.Date;

public class ConvertLocalDateTimeToDate implements Converter<LocalDateTime, Date> {

    @Override
    public Result<Date> convertToModel(LocalDateTime localDateTime, ValueContext valueContext) {
        Result<Date> result = null;

        try {
            if (localDateTime == null) {
                throw new IllegalArgumentException("Informe data e hora antes de prosseguir");
            }

            result = Result.ok(DateHelper.parse(localDateTime));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Result.error(ex.getMessage());
        }

        return result;
    }

    @Override
    public LocalDateTime convertToPresentation(Date date, ValueContext valueContext) {
        return LocalDateTimeHelper.parse(date);
    }
}
