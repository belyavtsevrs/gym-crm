package com.epam.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Month;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Months{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "month_name")
    private Month month;
    private Long workload;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workload_id")
    private Workload workloadRef;

    public Months(Month month,Long workload) {
        this.month = month;
        this.workload = workload;
    }
}
