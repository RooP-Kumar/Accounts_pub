package com.zen.accounts.repository

import com.zen.accounts.db.model.User
import com.zen.accounts.api.AuthApi
import dagger.hilt.android.AndroidEntryPoint
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import org.hamcrest.MatcherAssert.*


class AuthRepositoryTest {
    private lateinit var authRepository : AuthRepository
    private lateinit var user : User
    private lateinit var user1 : User
    private lateinit var user2 : User

    @Before
    fun setup() {
        authRepository = AuthRepository(AuthApi())
        user = User(
            "",
            "Roop Kumar",
            "9528865314",
            "roopkm12@gmail.com",
            false,
            null
        )
        user1 = User(
            "",
            "Roop",
            "9528865314",
            "roopkm12@gmail.com",
            false,
            null
        )
        user2 = User(
            "",
            "Harish",
            "9528865314",
            "harish123456@gmail.com",
            false,
            null
        )
    }

    @Test
    fun generating_correct_uid() {
        assertThat(authRepository.generateUID(user), `is`("95288Roop@roopkm1265314"))
    }

    @Test
    fun generating_con2_uid() {
        assertThat(authRepository.generateUID(user1), `is`("95288Roop@roopkm1265314"))
    }

    @Test
    fun generating_con3t_uid() {
        assertThat(authRepository.generateUID(user2), `is`("95288Harish@harish12345665314"))
    }
}