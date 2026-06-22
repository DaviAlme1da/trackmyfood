package br.com.davi.trackmyfood.core.model;

import br.com.davi.trackmyfood.core.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusOrder status;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitudeAddress;

    @Column(nullable = false)
    private Double longitudeAddress;

    @ManyToOne
    private DeliveryMan deliveryMan;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

}
