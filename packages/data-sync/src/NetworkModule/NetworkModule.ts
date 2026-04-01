import { NativeModule, requireNativeModule } from 'expo-modules-core';
import { NetworkInfo, NetworkModuleEvents } from './Network.types';

declare class NetworkModule extends NativeModule<NetworkModuleEvents> {
    getNetworkInfo(): NetworkInfo
    isConnected(): boolean
}

// This call loads the native module object from the JSI.
export default requireNativeModule<NetworkModule>('NativeNetworkModule');
