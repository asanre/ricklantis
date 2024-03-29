package io.asanre.app.ui.characterDetail

import androidx.compose.runtime.Composable
import io.asanre.app.domain.usecase.GetCharacterDetailsUseCase
import io.asanre.app.extensions.test
import io.asanre.app.fixtures.dummyCharacterDetails
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CharacterDetailPresenterTest {
    private val usecase: GetCharacterDetailsUseCase = mockk(relaxed = true)

    private val presenter: @Composable () -> CharacterDetailScreen.State
        get() = { CharacterDetailPresenter(usecase) }

    @Test
    fun `when subscribe should return initial state`() = runTest {
        presenter.test {
            val initial = awaitItem()
            assertNull(initial.character)
            assertFalse(initial.error)
        }
    }

    @Test
    fun `when GetDetail should request detail form api`() = runTest {
        presenter.test {
            val initial = awaitItem()
            initial.onEvent(CharacterDetailScreen.Event.GetDetail(1))
            cancelAndIgnoreRemainingEvents()
            coVerify(exactly = 1) { usecase(1) }
        }
    }

    @Test
    fun `when GetDetail success should update detail`() = runTest {
        coEvery { usecase(any()) } returns Result.success(dummyCharacterDetails)
        presenter.test {
            awaitItem().onEvent(CharacterDetailScreen.Event.GetDetail(1))
            val actual = awaitItem()
            assertEquals(dummyCharacterDetails, actual.character)
            assertFalse(actual.error)
        }
    }

    @Test
    fun `when GetDetail error should show error`() = runTest {
        coEvery { usecase(any()) } returns Result.failure(Error("Error"))
        presenter.test {
            awaitItem().onEvent(CharacterDetailScreen.Event.GetDetail(1))
            val actual = awaitItem()
            assertNull(actual.character)
            assertTrue(actual.error)
        }
    }
}
