package br.com.davi.trackmyfood.core.model;

import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import br.com.davi.trackmyfood.core.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_men")
public class DeliveryMan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusDeliveryMan statusDeliveryMan;
}
