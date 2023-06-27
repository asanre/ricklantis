package io.asanre.app.extensions

import app.cash.molecule.AndroidUiDispatcher
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

object AndroidUiDispatcherExtension : BeforeAllCallback, AfterAllCallback {
    override fun beforeAll(context: ExtensionContext?) {
        mockkObject(AndroidUiDispatcher).also {
            every { AndroidUiDispatcher.Companion::Main.get() } returns UnconfinedTestDispatcher()
        }
    }

    override fun afterAll(context: ExtensionContext?) {
        unmockkObject(AndroidUiDispatcher)
    }
}