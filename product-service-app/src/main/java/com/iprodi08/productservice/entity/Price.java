package com.iprodi08.productservice.entity;

import com.iprodi08.productservice.entity.enumType.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Price entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prices")
public class Price {
    @Id
    @SequenceGenerator(name = "prices_seq", sequenceName = "prices_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "prices_seq")
    private Long id;

    @Column(name = "price_value")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column
    private Currency currency;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "price")
    private List<Product> products;

    public Price(BigDecimal value, Currency currency) {
        this(null, value, currency, null, null, new ArrayList<>());
    }
}
