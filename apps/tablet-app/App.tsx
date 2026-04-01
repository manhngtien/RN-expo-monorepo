import { StyleSheet, Text, View } from 'react-native';

import { StatusBar } from 'expo-status-bar';

import {
  NATIVE_PI,
  getBatteryLevel,
  checkIsConnected,
  subscribeToNetworkChanges,
  getAllFlags,
  checkFeatureEnabled,
  startReader,
  stopReader
} from '../../packages/data-sync';
import { useEffect, useState } from 'react';



export default function App() {
  const [statusNetwork, setStatusNetwork] = useState<string>('');
  const [typeNetwork, setTypeNetwork] = useState<string>('');
  const [isFeatureEnabled, setIsFeatureEnabled] = useState<boolean>(false);
  const [isScanning, setIsScanning] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string>("");


  useEffect(() => {
    handleStartNfc()
    subscribeToNetworkChanges((event) => {
      setStatusNetwork(event.isConnected ? 'connected' : 'disconnected')
      setTypeNetwork(event.type)
    });

    setIsFeatureEnabled(checkFeatureEnabled('enable_offline_sync'))
  }, []);

  const handleStartNfc = async () => {
    try {
      setIsScanning(true);
      // Gọi hàm từ core, đưa luồng try-catch về đúng UI
      await startReader();
    } catch (error: any) {
      setIsScanning(false);
      switch (error.code) {
        case 'ERR_ACTIVITY_NOT_FOUND':
        case 'ActivityNotFound':
          setErrorMessage("Lỗi hệ thống: Vui lòng mở lại màn hình này.");
          break;
        case 'NfcNotSupported':
          // Có thể thiết kế UI hiển thị nút "Chuyển sang quét QR"
          setErrorMessage("Máy không có NFC. Vui lòng quét mã QR.");
          break;
        case 'NfcDisabled':
          // Hiện Modal hỏi user có muốn đi tới Cài đặt không
          setErrorMessage("Lỗi đọc thẻ: " + error.message);
          break;
        default:
          setErrorMessage("Không có lỗi")
      }
    }
  }

  const handleStopNfc = async () => {
    try {
      await stopReader();
      setIsScanning(false);
    } catch (error: any) {
      console.error("Lỗi khi tắt NFC:", error);
      // Đôi khi lỗi tắt NFC (ngầm) không cần báo cho user biết để tránh phiền
    }
  };

  return (
    <View style={styles.container}>
      <Text>The Native PI value: {NATIVE_PI}</Text>
      <Text>The BatteryLevel: {getBatteryLevel()}</Text>
      <Text>Check is Connect: {checkIsConnected() ? 'True' : 'False'}</Text>
      <Text>The status network: {statusNetwork}</Text>
      <Text>The type netwotk: {typeNetwork}</Text>
      <Text>Is Feature Enabled: {isFeatureEnabled ? 'true' : 'false'}</Text>
      <Text>error Message: {errorMessage}</Text>
      <StatusBar style='auto' />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center'
  }
});
