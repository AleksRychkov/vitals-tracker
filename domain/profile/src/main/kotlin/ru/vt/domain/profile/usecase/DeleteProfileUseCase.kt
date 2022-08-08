package ru.vt.domain.profile.usecase

import ru.vt.core.common.UseCase
import ru.vt.domain.profile.repository.ProfileRepository

class DeleteProfileUseCase(
    private val profileRepository: ProfileRepository
) : UseCase<DeleteProfileUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Result<Boolean> {
        profileRepository.deleteProfile(params.id)
        return Result.success(true)
    }

    data class Params(val id: Long)
}
