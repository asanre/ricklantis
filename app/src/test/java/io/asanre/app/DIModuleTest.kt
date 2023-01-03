package io.asanre.app

import org.junit.jupiter.api.Test
import org.koin.test.check.checkKoinModules


class DIModuleTest {
    @Test
    fun verifyModules() {
        checkKoinModules(appModule)
    }
}