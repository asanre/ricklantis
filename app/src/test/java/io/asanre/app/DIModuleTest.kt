package io.asanre.app

import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules
import org.koin.test.mock.declare
import kotlin.coroutines.CoroutineContext


class DIModuleTest : KoinTest {
    @Test
    fun verifyModules() {
        checkKoinModules(appModule) {
            declare<CoroutineContext> { UnconfinedTestDispatcher() }
        }
    }
}