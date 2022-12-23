package io.asanre.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoader.*
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return Builder(this)
            .memoryCache { MemoryCache.Builder(this).maxSizePercent(MEMORY_CACHE_PERCENT).build() }
            .diskCache {
                DiskCache.Builder().directory(cacheDir.resolve(IMAGE_CACHE_DIR))
                    .maxSizePercent(DISK_CACHE_PERCENT)
                    .build()
            }.build()
    }

    companion object {
        private const val IMAGE_CACHE_DIR = "image_cache"
        private const val MEMORY_CACHE_PERCENT = 0.25
        private const val DISK_CACHE_PERCENT = 0.02
    }
}