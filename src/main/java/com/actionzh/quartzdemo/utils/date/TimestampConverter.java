package com.actionzh.quartzdemo.utils.date;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.joda.time.DateTime;

public class TimestampConverter extends BidirectionalConverter<DateTime, Long> {

    @Override
    public Long convertTo(DateTime source, Type<Long> destinationType, MappingContext mappingContext) {
        return source.getMillis();
    }

    @Override
    public DateTime convertFrom(Long source, Type<DateTime> destinationType, MappingContext mappingContext) {
        return new DateTime(source);
    }
}
