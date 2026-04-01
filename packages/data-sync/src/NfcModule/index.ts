import NfcModule from './NfcModule'


export const startReader = async (): Promise<boolean> => {
    return await NfcModule.startNfcReader();
};

export const stopReader = async (): Promise<void> => {
    await NfcModule.stopNfcReader();
};
