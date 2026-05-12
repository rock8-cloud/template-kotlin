package com.vibeham.template.model

import jakarta.persistence.*

@Entity
@Table(name = "space_target")
class SpaceTarget(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String = "",

    var type: String = "",

    @Column(name = "distance_light_years")
    var distanceLightYears: Double = 0.0
)
