package ru.vt.domain.profile.usecase

import ru.vt.core.common.UseCase
import ru.vt.domain.common.AppGender
import ru.vt.domain.common.SimpleDateEntity
import ru.vt.domain.profile.entity.ProfileEntity
import ru.vt.domain.profile.exceptions.EmptyNameError
import ru.vt.domain.profile.exceptions.UniqueNameError
import ru.vt.domain.profile.repository.ProfileRepository

class SaveProfileUseCase(
    private val repository: ProfileRepository
) : UseCase<SaveProfileUseCase.Params, Long>() {

    override suspend fun execute(params: Params): Result<Long> {
        return when {
            params.name.isNullOrEmpty() -> Result.failure(EmptyNameError)
            repository.isNameAvailable(params.name).not() -> Result.failure(UniqueNameError)
            else -> {
                Result.success(
                    repository.saveProfile(
                        ProfileEntity(
                            id = 0,
                            name = params.name,
                            birth = params.birthdate,
                            gender = params.gender,
                            heightCm = params.heightCm,
                            weightG = params.weightG
                        )
                    )
                )
            }
        }
    }

    data class Params(
        val name: String?,
        val birthdate: SimpleDateEntity? = null,
        val gender: AppGender? = null,
        val heightCm: Int? = null,
        val weightG: Int? = null
    )
}
