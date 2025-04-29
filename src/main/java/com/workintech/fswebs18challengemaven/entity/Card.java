package com.workintech.fswebs18challengemaven.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="cards", schema = "fsweb")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "value")
    private Integer value;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;
}
