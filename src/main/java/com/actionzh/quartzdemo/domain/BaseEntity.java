package com.actionzh.quartzdemo.domain;

import com.actionzh.quartzdemo.utils.date.JodaTimeConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "version")
    @Version
    private Long version;

    @Convert(converter = JodaTimeConverter.class)
    @CreatedDate
    @Column(name = "date_created", updatable = false)
    private DateTime dateCreated;

    @Convert(converter = JodaTimeConverter.class)
    @LastModifiedDate
    @Column(name = "last_updated")
    private DateTime lastUpdated;
}
