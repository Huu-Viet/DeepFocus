package com.bcn.deepfocus.model.custom.data.mapper_impl

import com.bcn.deepfocus.core.common.ILocalMapper
import com.bcn.deepfocus.model.custom.data.local.entities.FocusSetEntity
import com.bcn.deepfocus.model.custom.domain.entities.FocusSet

class LocalFocusSetMapper: ILocalMapper<FocusSet, FocusSetEntity> {
    override fun mapToDomain(e: FocusSetEntity): FocusSet {
        return FocusSet(
            id = e.id,
            focusMinute = e.focusMinute,
            breakMinute = e.breakMinute,
        )
    }

    override fun mapToEntity(d: FocusSet): FocusSetEntity {
        return FocusSetEntity(
            id = d.id,
            focusMinute = d.focusMinute,
            breakMinute = d.breakMinute
        )
    }
}