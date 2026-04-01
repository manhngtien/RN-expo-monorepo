// Reexport the native module. On web, it will be resolved to DataSyncModule.web.ts
// and on native platforms to DataSyncModule.ts

import DataSyncModule from './DataSyncModule/DataSyncModule';

export const NATIVE_PI = DataSyncModule.PI;

export function getBatteryLevel(): number {
  return DataSyncModule.getBatteryLevel();
}

export {
  fetchNetworkInfo,
  subscribeToNetworkChanges,
  checkIsConnected
} from './NetworkModule'


export {
  checkFeatureEnabled,
  getAllFlags
} from './FeatureFlagModule'


export {
  startReader,
  stopReader
} from './NfcModule'