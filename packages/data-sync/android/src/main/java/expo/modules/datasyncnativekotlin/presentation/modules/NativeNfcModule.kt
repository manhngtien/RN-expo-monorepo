package expo.modules.datasyncnativekotlin.presentation.modules

import expo.modules.datasyncnativekotlin.di.KoinInitializer
import expo.modules.datasyncnativekotlin.domain.exception.ActivityNotFoundException
import expo.modules.datasyncnativekotlin.domain.manager.AndroidNfcManager
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeNfcModule : Module(), KoinComponent {

    private val nfcManager: AndroidNfcManager by inject()

    override fun definition() = ModuleDefinition {
        Name("NativeNfcModule")

        Events("onNfcTagScanned")

        OnCreate {
            KoinInitializer.start(appContext.reactContext!!)
        }

        AsyncFunction("startNfcReader") {
            val activity = appContext.currentActivity ?: throw ActivityNotFoundException()
            val result = nfcManager.startListening(activity) { tagData ->
                sendEvent(
                    "onNfcTagScanned", mapOf(
                        "tagId" to tagData,
                        "timestamp" to System.currentTimeMillis()
                    )
                )
            }

            return@AsyncFunction result
        }

        Function("stopNfcReader") {
            val activity = appContext.currentActivity ?: throw ActivityNotFoundException()

            nfcManager.stopListening(activity)
        }
    }
}