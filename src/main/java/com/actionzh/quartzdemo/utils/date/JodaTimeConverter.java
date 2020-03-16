package com.actionzh.quartzdemo.utils.date;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Date;

/*
 * eclipselink has another interface called 'Converter', here we
 * use the 'standard' one
 *
 * maybe type arguments of AttributeConverter can be omitted.
 * but currently, if omit, hibernate raises an error
 */
@Converter
public class JodaTimeConverter implements AttributeConverter<DateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(DateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toDate();
    }

    @Override
    public DateTime convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }
        return new DateTime(dbData);
    }

}
