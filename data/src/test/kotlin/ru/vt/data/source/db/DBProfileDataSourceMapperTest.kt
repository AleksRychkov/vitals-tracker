package ru.vt.data.source.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.vt.database.entity.ProfileEntityDB
import ru.vt.domain.common.AppGender
import ru.vt.domain.common.SimpleDateEntity
import ru.vt.domain.profile.entity.ProfileEntity

internal class DBProfileDataSourceMapperTest {

    @Test
    fun `should successfully convert entity from domain to DB`() {
        val actual = ProfileEntity(
            id = 123L,
            name = "SuperDuperName",
            birth = SimpleDateEntity(12, 2, 1986),
            gender = AppGender.FEMALE,
            heightCm = 160,
            weightG = 70
        )
        val expected = ProfileEntityDB(
            profileId = 123L,
            name = "SuperDuperName",
            birthDay = 12,
            birthMonth = 2,
            birthYear = 1986,
            gender = AppGender.FEMALE.name,
            heightCm = 160,
            weightG = 70
        )
        assertEquals(expected, DBProfileDataSource.Mapper.mapToDb(actual))
    }

    @Test
    fun `should successfully convert entity from DB to domain`() {
        val actual = ProfileEntityDB(
            profileId = 123L,
            name = "SuperDuperName",
            birthDay = 12,
            birthMonth = 2,
            birthYear = 1986,
            gender = AppGender.FEMALE.name,
            heightCm = 160,
            weightG = 70
        )
        val expected = ProfileEntity(
            id = 123L,
            name = "SuperDuperName",
            birth = SimpleDateEntity(12, 2, 1986),
            gender = AppGender.FEMALE,
            heightCm = 160,
            weightG = 70
        )
        assertEquals(expected, DBProfileDataSource.Mapper.mapFromDb(actual))
    }
}
