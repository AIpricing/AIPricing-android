package ai.aipricing.billing;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;

interface IInAppBillingService extends IInterface {
    int isBillingSupported(int var1, String var2, String var3) throws RemoteException;

    Bundle getSkuDetails(int var1, String var2, String var3, Bundle var4) throws RemoteException;

    Bundle getBuyIntent(int var1, String var2, String var3, String var4, String var5) throws RemoteException;

    Bundle getPurchases(int var1, String var2, String var3, String var4) throws RemoteException;

    int consumePurchase(int var1, String var2, String var3) throws RemoteException;

    int stub(int var1, String var2, String var3) throws RemoteException;

    Bundle getBuyIntentToReplaceSkus(int var1, String var2, List<String> var3, String var4, String var5, String var6) throws RemoteException;

    Bundle getBuyIntentExtraParams(int var1, String var2, String var3, String var4, String var5, Bundle var6) throws RemoteException;

    Bundle getPurchaseHistory(int var1, String var2, String var3, String var4, Bundle var5) throws RemoteException;

    int isBillingSupportedExtraParams(int var1, String var2, String var3, Bundle var4) throws RemoteException;

    public abstract static class Stub extends Binder implements IInAppBillingService {
        private static final String DESCRIPTOR = "com.android.vending.billing.IInAppBillingService";
        static final int TRANSACTION_isBillingSupported = 1;
        static final int TRANSACTION_getSkuDetails = 2;
        static final int TRANSACTION_getBuyIntent = 3;
        static final int TRANSACTION_getPurchases = 4;
        static final int TRANSACTION_consumePurchase = 5;
        static final int TRANSACTION_stub = 6;
        static final int TRANSACTION_getBuyIntentToReplaceSkus = 7;
        static final int TRANSACTION_getBuyIntentExtraParams = 8;
        static final int TRANSACTION_getPurchaseHistory = 9;
        static final int TRANSACTION_isBillingSupportedExtraParams = 10;

        public Stub() {
            this.attachInterface(this, "com.android.vending.billing.IInAppBillingService");
        }

        public static IInAppBillingService asInterface(IBinder obj) {
            if(obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.android.vending.billing.IInAppBillingService");
                return (IInAppBillingService)(iin != null && iin instanceof IInAppBillingService?(IInAppBillingService)iin:new IInAppBillingService.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch(code) {
                case 1: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _result = this.isBillingSupported(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 2: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    Bundle _arg3;
                    if (0 != data.readInt()) {
                        _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    Bundle _result = this.getSkuDetails(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                }
                case 3: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    String _arg4 = data.readString();
                    Bundle _result = this.getBuyIntent(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                }
                case 4: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    Bundle _result = this.getPurchases(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 5: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _result = this.consumePurchase(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 6: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    int _result = this.stub(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 7: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    List<String> _arg2 = data.createStringArrayList();
                    String _arg3 = data.readString();
                    String _arg4 = data.readString();
                    String _arg5 = data.readString();
                    Bundle _result = this.getBuyIntentToReplaceSkus(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 8: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    String _arg4 = data.readString();
                    Bundle _result;
                    if (0 != data.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _result = null;
                    }

                    _result = this.getBuyIntentExtraParams(_arg0, _arg1, _arg2, _arg3, _arg4, _result);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 9: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    String _arg3 = data.readString();
                    Bundle _result;
                    if (0 != data.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _result = null;
                    }

                    _result = this.getPurchaseHistory(_arg0, _arg1, _arg2, _arg3, _result);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 10: {
                    data.enforceInterface("com.android.vending.billing.IInAppBillingService");
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String  _arg2 = data.readString();
                    Bundle _arg3;
                    if (0 != data.readInt()) {
                        _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }

                    int _result = this.isBillingSupportedExtraParams(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 1598968902:
                    reply.writeString("com.android.vending.billing.IInAppBillingService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IInAppBillingService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.android.vending.billing.IInAppBillingService";
            }

            public int isBillingSupported(int apiVersion, String packageName, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Bundle _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    if(skusBundle != null) {
                        _data.writeInt(1);
                        skusBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (Bundle)Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type, String developerPayload) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Bundle _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(sku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (Bundle)Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Bundle getPurchases(int apiVersion, String packageName, String type, String continuationToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Bundle _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    _data.writeString(continuationToken);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (Bundle)Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public int consumePurchase(int apiVersion, String packageName, String purchaseToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(purchaseToken);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public int stub(int apiVersion, String packageName, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Bundle getBuyIntentToReplaceSkus(int apiVersion, String packageName, List<String> oldSkus, String newSku, String type, String developerPayload) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Bundle _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeStringList(oldSkus);
                    _data.writeString(newSku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (Bundle)Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Bundle getBuyIntentExtraParams(int apiVersion, String packageName, String sku, String type, String developerPayload, Bundle extraParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Bundle _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(sku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    if(extraParams != null) {
                        _data.writeInt(1);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (Bundle)Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Bundle getPurchaseHistory(int apiVersion, String packageName, String type, String continuationToken, Bundle extraParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Bundle _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    _data.writeString(continuationToken);
                    if(extraParams != null) {
                        _data.writeInt(1);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (Bundle)Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public int isBillingSupportedExtraParams(int apiVersion, String packageName, String type, Bundle extraParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("com.android.vending.billing.IInAppBillingService");
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    if(extraParams != null) {
                        _data.writeInt(1);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }
        }
    }
}
