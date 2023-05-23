package io.asanre.app.ui.characterList

import io.asanre.app.fixtures.dummyCharacterList
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue


class CharacterListStateTest {
    @Test
    fun `when initial state then show loading and return count`() {
        val current = CharacterListState.INITIAL

        assertTrue(current.showLoading)
        assertFalse(current.showEmptyScreen)
        assertNotNull(current.count)
    }

    @Test
    fun `when add new characters then return new state`() {
        val current = CharacterListState.INITIAL.addCharacters(dummyCharacterList)

        assertTrue(current.characters.isNotEmpty())
        assertFalse(current.showLoading)
        assertFalse(current.showEmptyScreen)
        assertNotNull(current.count)
    }

    @Test
    fun `when all characters loaded then should hide loading and not return count`() {
        val current = CharacterListState.INITIAL
            .addCharacters(dummyCharacterList.copy(allCharacterLoaded = true))

        assertTrue(current.characters.isNotEmpty())
        assertFalse(current.showLoading)
        assertFalse(current.showEmptyScreen)
        assertNull(current.count)
    }

    @Test
    fun `given initial state when invoke showError then show error and hide loading`() {
        val sut = CharacterListState.INITIAL.showError()

        assertTrue(sut.error)
        assertFalse(sut.showLoading)
    }

    @Test
    fun `given a state with error when dismissError then hide error`() {
        val sut = CharacterListState.INITIAL.showError()

        assertFalse(sut.dismissError().error)
    }
}