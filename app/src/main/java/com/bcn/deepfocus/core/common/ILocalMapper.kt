package com.bcn.deepfocus.core.common

interface ILocalMapper<Domain, Entity> {
    fun mapToDomain(e: Entity): Domain
    fun mapToEntity(d: Domain): Entity
}