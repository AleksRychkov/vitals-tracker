package ru.vt.domain.profile.usecase

import ru.vt.core.common.UseCase
import ru.vt.domain.profile.entity.ProfileEntity
import ru.vt.domain.profile.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) : UseCase<GetProfileUseCase.Params, ProfileEntity>() {

    data class Params(val profileId: Long)

    override suspend fun execute(params: Params): Result<ProfileEntity> =
        Result.success(repository.getProfileById(params.profileId))
}
