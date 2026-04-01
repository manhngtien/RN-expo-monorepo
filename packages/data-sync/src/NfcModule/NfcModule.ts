import { NativeModule, requireNativeModule } from "expo-modules-core";


declare class NfcModule extends NativeModule {
    startNfcReader(): Promise<boolean>
    stopNfcReader(): Promise<void>
}

export default requireNativeModule<NfcModule>('NativeNfcModule');