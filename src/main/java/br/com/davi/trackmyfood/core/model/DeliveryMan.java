package br.com.davi.trackmyfood.core.model;

import br.com.davi.trackmyfood.core.enums.StatusDeliveryMan;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "delivery_men")
public class DeliveryMan extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDeliveryMan status;
}
