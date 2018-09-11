package ai.aipricing.billing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class BillingHelper {
    private Context mContext;
    /** Field's key to hold library version key constant. */
    private static final String LIBRARY_VERSION_KEY = "libraryVersion";
    /** Field's key value to hold current library version. */
    private static final String LIBRARY_VERSION = "1.0";
    String RESPONSE_GET_SKU_DETAILS_LIST="DETAILS_LIST";
    private IInAppBillingService mService;
    private ai.aipricing.billing.BillingHelper.BillingServiceConnection mServiceConnection;
    private String mSku;
    private int mBackKey;
    public @interface SkuType {
        /** A type of SKU for in-app products. */
        String INAPP = "inapp";
        /** A type of SKU for subscriptions. */
        String SUBS = "subs";
    }

    public BillingHelper(@NonNull Context context,String sku,int backKey){
        mContext=context;
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        List<ResolveInfo> intentServices = mContext.getPackageManager().queryIntentServices(serviceIntent, 0);
        if (intentServices != null && !intentServices.isEmpty()) {
            ResolveInfo resolveInfo = intentServices.get(0);
            if (resolveInfo.serviceInfo != null) {
                String packageName = resolveInfo.serviceInfo.packageName;
                String className = resolveInfo.serviceInfo.name;
                if ("com.android.vending".equals(packageName) && className != null) {
                    ComponentName component = new ComponentName(packageName, className);
                    Intent explicitServiceIntent = new Intent(serviceIntent);
                    explicitServiceIntent.setComponent(component);
                    explicitServiceIntent.putExtra(LIBRARY_VERSION_KEY, LIBRARY_VERSION);
                    mServiceConnection=new BillingServiceConnection();
                    mSku=sku;
                    mBackKey=backKey;
                    mContext.bindService(explicitServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                }
            }
        }
    }

    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    private final class BillingServiceConnection implements ServiceConnection {
        final int MIN_LEVEL=1;
        final int MAX_LEVEL=9;
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            mService = IInAppBillingService.Stub.asInterface(service);
            try {
                ArrayList<String> skuList = new ArrayList<String> ();
                for(int i = MIN_LEVEL; i<= MAX_LEVEL; i++){
                    skuList.add(mSku+i);
                }
                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
                querySkus.putString(LIBRARY_VERSION_KEY, LIBRARY_VERSION);
                JSONObject jsonObj=null;
                int emptyCount=0;
                for(int st=0;st<2;st++) {
                    try {
                        final Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(), st<=0? BillingHelper.SkuType.INAPP: BillingHelper.SkuType.SUBS, querySkus);
                        if (skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
                            int response = getResponseCodeFromBundle(skuDetails);
                            if (response == BILLING_RESPONSE_RESULT_OK) {
                                ArrayList<String> skuDetailsJsonList = skuDetails.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST);
                                if (skuDetailsJsonList==null||skuDetailsJsonList.isEmpty()) {
                                    emptyCount++;
                                }else{
                                    jsonObj=new JSONObject();
                                    jsonObj.put("aPi","a"+mSku);
                                    jsonObj.put("callKey",mBackKey);
                                    jsonObj.put("priceList",new JSONArray(skuDetailsJsonList));
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) { }
                }
                if(jsonObj==null&&emptyCount>=2){
                    jsonObj=new JSONObject();
                    jsonObj.put("aPi","a"+mSku);
                    jsonObj.put("callKey",mBackKey);
                    jsonObj.put("priceList",new JSONArray());
                }
                if(jsonObj!=null){
                    final String jsonString=jsonObj.toString();
                    Executors.defaultThreadFactory().newThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                StringToHttp sth=new StringToHttp(mContext,0,0);
                                sth.send(jsonString);
                            }catch (Exception ex){ }
                        }
                    }).start();

                }
            }catch (Exception ex){ }
            if(mContext!=null&&mService!=null) {
                mContext.unbindService(mServiceConnection);
                mServiceConnection=null;
            }
        }

        public int getResponseCodeFromBundle(Bundle bundle) {
            if (bundle == null) {
                return -100;
            }else {
                Object responseCode = bundle.get("RESPONSE_CODE");
                if (responseCode == null) {
                    return 0;
                } else if (responseCode instanceof Integer) {
                    return (Integer)responseCode;
                } else {
                    return -101;
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name){ }
    }
}


