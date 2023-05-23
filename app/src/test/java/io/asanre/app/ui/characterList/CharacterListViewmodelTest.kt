package io.asanre.app.ui.characterList

import app.cash.molecule.RecompositionClock
import app.cash.turbine.test
import io.asanre.app.domain.repository.CharacterRepository
import io.asanre.app.fixtures.dummyCharacterList
import io.asanre.app.ui.characterList.CharactersScreen.Event
import io.asanre.app.ui.characterList.CharactersScreen.State
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CharacterListViewmodelTest {

    private val repository: CharacterRepository = mockk(relaxed = true)
    private val viewModel = CharacterListViewmodel(
        repository,
        UnconfinedTestDispatcher(),
        RecompositionClock.Immediate
    )


    @Test
    fun `when collect  first state should be initial state`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(State.INITIAL, state)
        }
    }

    @Test
    fun `when GetCharacters should get characters from api`() = runTest {
        coEvery { repository.getCharacters(any()) } returns Result.success(dummyCharacterList)
        viewModel.state.test {
            viewModel.emit(Event.GetCharacters)
            cancelAndIgnoreRemainingEvents()
            coVerify(exactly = 1) { repository.getCharacters(any()) }
        }
    }

    @Test
    fun `when GetCharacters success should update state`() = runTest {
        val result = dummyCharacterList
        coEvery { repository.getCharacters(any()) } returns Result.success(result)
        viewModel.state.test {
            viewModel.emit(Event.GetCharacters)
            assertEquals(awaitItem().addCharacters(result), awaitItem())
        }
    }

    @Test
    fun `when GetCharacters error should show error`() = runTest {
        coEvery { repository.getCharacters(any()) } returns Result.failure(Error())
        viewModel.state.test {
            val initial = awaitItem()
            viewModel.emit(Event.GetCharacters)
            assertEquals(initial.showError(), awaitItem())
        }
    }

    @Test
    fun `when ErrorShown should dismissError`() = runTest {
        coEvery { repository.getCharacters(any()) } returns Result.failure(Error())
        viewModel.state.test {
            viewModel.emit(Event.GetCharacters)
            viewModel.emit(Event.ErrorShown)
            skipItems(1)
            assertEquals(awaitItem().dismissError(), awaitItem())
        }
    }
}
