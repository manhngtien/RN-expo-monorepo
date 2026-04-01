
import { NetworkInfo } from './Network.types';
import NetworkModule from './NetworkModule'

export const checkIsConnected = (): boolean => {
    return NetworkModule.isConnected();
};

export const fetchNetworkInfo = (): NetworkInfo => {
    try {
        return NetworkModule.getNetworkInfo();
    } catch (error) {
        console.error("[NetworkModule] Lỗi lấy thông tin mạng:", error);
        return { isConnected: false, isValidated: false, type: 'UNKNOWN' };
    }
};

export const subscribeToNetworkChanges = (
    callback: (info: NetworkInfo) => void
): (() => void) => {
    const subscription = NetworkModule.addListener('networkChanged', callback);
    return () => {
        subscription.remove();
    };
};
