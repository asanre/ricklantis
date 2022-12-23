package io.asanre.app

import io.asanre.app.core.data.coreDataModule
import io.asanre.app.data.dataModule
import io.asanre.app.domain.domainModule
import io.asanre.app.ui.uiModule

val appModule = coreDataModule + dataModule + domainModule + uiModule