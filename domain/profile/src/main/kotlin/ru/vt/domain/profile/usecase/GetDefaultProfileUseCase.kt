package ru.vt.domain.profile.usecase

import ru.vt.core.common.UseCase
import ru.vt.domain.profile.entity.ProfileEntity
import ru.vt.domain.profile.repository.ProfileRepository

class GetDefaultProfileUseCase(
    private val repository: ProfileRepository
) : UseCase<Unit, ProfileEntity?>() {

    override suspend fun execute(params: Unit): Result<ProfileEntity?> =
        Result.success(repository.getDefaultProfile())
}
