package com.github.zzehring.intellijjsonnet

import com.intellij.openapi.application.PreloadingActivity
import com.intellij.openapi.progress.ProgressIndicator

class JsonnetPreloadingActivity : PreloadingActivity() {

    override fun preload(indicator: ProgressIndicator) {
        val handler = JsonnetLSStartupHandler()
        handler.start()
    }

}
