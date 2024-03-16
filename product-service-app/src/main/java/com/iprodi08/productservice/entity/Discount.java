package com.iprodi08.productservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Discount entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @SequenceGenerator(name = "discounts_seq", sequenceName = "discounts_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "discounts_seq")
    private Long id;

    @Column(name = "discount_value")
    private Integer value;

    @Column(name = "datetime_from")
    private OffsetDateTime dateTimeFrom;

    @Column(name = "datetime_until")
    private OffsetDateTime dateTimeUntil;

    @Column
    private Boolean active;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(mappedBy = "discounts")
    private List<Product> products;

    public Discount(Integer value, OffsetDateTime dateTimeFrom, OffsetDateTime dateTimeUntil, Boolean active) {
        this(null, value, dateTimeFrom, dateTimeUntil, active, null, null, new ArrayList<>());
    }
}
