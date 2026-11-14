package com.github.hantsy.ee8sample.domain.support;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Version;
import jakarta.persistence.TableGenerator;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import com.github.hantsy.ee8sample.EclipseLinkLongVersionType;

@MappedSuperclass
@Getter
@Setter
public class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @TableGenerator(name = "SEQ_GEN", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SEQ_GEN", initialValue = 0, allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SEQ_GEN")
    @Column(name = "id")
    private Long id;

    @Version
    @Type(EclipseLinkLongVersionType.class)
    @Column(name = "version")
    private Long version;
}
