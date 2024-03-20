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
    @SequenceGenerator(
            name = "discounts_seq",
            sequenceName = "discounts_id_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "discounts_seq")
    private Long id;

    @Column(name = "discount_value")
    private Integer value;

    @Column(name = "datetime_from")
    private Instant dateTimeFrom;

    @Column(name = "datetime_until")
    private Instant dateTimeUntil;

    @Column
    private Boolean active;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(mappedBy = "discounts")
    private List<Product> products = new ArrayList<>();

    public static Discount createNewDiscount(
            Long id,
            Integer value,
            Instant dateTimeFrom,
            Instant dateTimeUntil,
            Boolean active
    ) {
        Discount discount = new Discount();
        discount.setId(id);
        discount.setValue(value);
        discount.setDateTimeFrom(dateTimeFrom);
        discount.setDateTimeUntil(dateTimeUntil);
        discount.setActive(active);

        return discount;
    }
}
