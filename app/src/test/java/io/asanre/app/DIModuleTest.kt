package io.asanre.app

import io.asanre.app.extensions.AndroidUiDispatcherExtension
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules
import org.koin.test.mock.declare
import kotlin.coroutines.CoroutineContext

@ExtendWith(AndroidUiDispatcherExtension::class)
class DIModuleTest : KoinTest {
    @Test
    fun verifyModules() {
        checkKoinModules(appModule) {
            declare<CoroutineContext> { UnconfinedTestDispatcher() }
        }
    }
}