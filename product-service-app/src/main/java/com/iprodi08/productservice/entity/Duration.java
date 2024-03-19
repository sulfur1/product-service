package com.iprodi08.productservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
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
 * Duration entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "durations")
public class Duration {
    @Id
    @SequenceGenerator(name = "durations_seq", sequenceName = "durations_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "durations_seq")
    private Long id;

    @Column(name = "in_days")
    private Integer inDays;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "duration")
    private List<Product> products;

    public Duration(Integer inDays) {
        this(null, inDays, null, null, new ArrayList<>());
    }
}
