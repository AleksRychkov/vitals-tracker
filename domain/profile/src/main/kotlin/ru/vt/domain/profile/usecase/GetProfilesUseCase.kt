package ru.vt.domain.profile.usecase

import ru.vt.core.common.UseCase
import ru.vt.domain.profile.entity.ProfileEntity
import ru.vt.domain.profile.repository.ProfileRepository

class GetProfilesUseCase(
    private val repository: ProfileRepository
) : UseCase<Unit, List<ProfileEntity>>() {

    override suspend fun execute(params: Unit): Result<List<ProfileEntity>> =
        Result.success(repository.getProfiles())
}
