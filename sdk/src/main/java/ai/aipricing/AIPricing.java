package ai.aipricing;

import android.Manifest;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ai.aipricing.billing.BillingHelper;
import ai.aipricing.billing.StringToHttp;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Handler for calls to the AIPricing SDK.
 */
public class AIPricing {
    /** Maximum level constant. */
    public static final int MIN_LEVEL=1;
    /** Minimum level constant. */
    public static final int MAX_LEVEL=9;
    private Context mContext;
    private int connectTimeout=-1;
    private int readTimeout=-1;
    private double[] priceArray;
    private HashMap<String,String> stringMap;
    private HashMap<String,Integer> valueMap;
    private static ExecutorService singleExecutor;
    private static final String PREFERENCES_NAME="aip10spv";

    /**
     * Constructor.
     * @param context {@link Context} for resolving resources
     */
    public AIPricing(@NonNull Context context) {
        if(context==null){
            throw new IllegalArgumentException("'context' does not allow null values");
        }else{
            mContext=context.getApplicationContext();
        }
    }

    /**
     * Get purchase level, this is a asynchronous method.
     * @param productId Product Id Prefix
     * @param defaultLevel Default level, If the network does not work, this function returns the default value
     * @param callback a {@link LevelCallback} to receive the result of this operation
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void getLevel(final @NonNull String productId,final @IntRange(from=MIN_LEVEL,to=MAX_LEVEL) int defaultLevel, final @Nullable LevelCallback callback){
        if(singleExecutor==null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                if (preferences.contains(productId)) {
                    if (callback != null) {
                        callback.onSuccess(preferences.getInt(productId, defaultLevel));
                    }
                } else {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("aPi", "a" + productId);
                        //local
                        Locale locale;
                        if (Build.VERSION.SDK_INT >= 24) {
                            locale = mContext.getResources().getConfiguration().getLocales().get(0);
                        } else {
                            //noinspection deprecation
                            locale = mContext.getResources().getConfiguration().locale;
                        }
                        json.put("aCt", locale.getCountry());
                        json.put("aLg", locale.getLanguage());
                        //level
                        json.put("aDt", 2);
                        json.put("aDv", defaultLevel);
                        //android id
                        try {
                            String androidId = Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
                            if (androidId != null) {
                                json.put("aId", androidId);
                            }
                        } catch (Exception ex) {
                        }
                        //brand
                        internalAddItem(":aPb", Build.BRAND);
                        //model
                        internalAddItem(":aPm", Build.MODEL);
                        //SDK_INT
                        internalAddItem(":aVi", Build.VERSION.SDK_INT);
                        //Sim Operator name
                        try {
                            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
                            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                                internalAddItem(":aSn", telephonyManager.getSimOperatorName());
                            }
                        } catch (Exception ex) {
                        }
                        //Storage Space
                        try {
                            File path = Environment.getDataDirectory();
                            if (path.exists()) {
                                StatFs stat = new StatFs(path.getPath());
                                long totalSpace;
                                if (Build.VERSION.SDK_INT >= 18) {
                                    totalSpace = stat.getTotalBytes();
                                } else {
                                    //noinspection deprecation
                                    totalSpace = (long) stat.getBlockSize() * (long) stat.getBlockCount();
                                }
                                int aSS = (int) (totalSpace / 1024L / 1024L);
                                //If the conversion overflows, the value is negative
                                if (aSS > 0) {
                                    internalAddItem(":aSs", aSS);
                                }
                            }
                        } catch (Exception ex) {
                        }
                        //Memory
                        if (Build.VERSION.SDK_INT >= 16) {
                            try {
                                ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
                                if (activityManager != null) {
                                    ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                                    activityManager.getMemoryInfo(memoryInfo);
                                    int aMs = (int) (memoryInfo.totalMem / 1024L / 1024L);
                                    //If the conversion overflows, the value is negative
                                    if (aMs > 0) {
                                        internalAddItem(":aMs", aMs);
                                    }
                                }
                            } catch (Exception ex) {
                            }
                        }
                        //Screen
                        try {
                            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                            if (windowManager != null) {
                                DisplayMetrics metrics = new DisplayMetrics();
                                if (Build.VERSION.SDK_INT >= 17) {
                                    windowManager.getDefaultDisplay().getRealMetrics(metrics);
                                } else {
                                    //noinspection deprecation
                                    windowManager.getDefaultDisplay().getMetrics(metrics);
                                }
                                internalAddItem(":aSi", Math.min(metrics.widthPixels, metrics.heightPixels));
                                internalAddItem(":aSa", Math.max(metrics.widthPixels, metrics.heightPixels));
                                internalAddItem(":aSd", metrics.densityDpi);
                            }
                        } catch (Exception ex) {
                        }
                        //app version code
                        try {
                            PackageManager packageManager = mContext.getPackageManager();
                            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                            long verCode;
                            if (Build.VERSION.SDK_INT >= 28) {
                                verCode = packageInfo.getLongVersionCode();
                            } else {
                                verCode = packageInfo.versionCode;
                            }
                            internalAddItem(":aVc", Long.toString(verCode));
                        } catch (Exception ex) {
                        }
                        //item map,String
                        if (stringMap != null && stringMap.size() > 0) {
                            JSONObject jsonObj = new JSONObject();
                            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                                jsonObj.put(entry.getKey(), entry.getValue());
                            }
                            json.put("hAs", jsonObj);
                        }
                        //item map,int
                        if (valueMap != null && valueMap.size() > 0) {
                            JSONObject jsonObj = new JSONObject();
                            for (Map.Entry<String, Integer> entry : valueMap.entrySet()) {
                                jsonObj.put(entry.getKey(), entry.getValue().intValue());
                            }
                            json.put("hAv", jsonObj);
                        }
                        //price array
                        if (priceArray != null) {
                            ArrayList<Double> af = new ArrayList<>();
                            for (double tf : priceArray) {
                                af.add(tf);
                            }
                            json.put("hPf", new JSONArray(af).toString());
                        }
                        String sendStr = json.toString();
                        StringToHttp sth = new StringToHttp(mContext, connectTimeout, readTimeout);
                        String resultJson = sth.send(sendStr);
                        JSONObject jo = null;
                        if (resultJson != null) {
                            try {
                                jo = new JSONObject(resultJson);
                            } catch (JSONException ex) {
                            }
                        }
                        if (jo != null) {
                            if (jo.has("callKey")) {
                                try {
                                    new BillingHelper(mContext, productId, jo.getInt("callKey"));
                                } catch (Exception ex) {
                                }
                            }
                            int level = jo.getInt("val");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(productId, level);
                            editor.putString(productId + "j32sOn", sendStr);
                            editor.apply();
                            if (callback != null) {
                                callback.onSuccess(level);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }//else
            }//run()
        });//execute
    }

    /**
     * Send payment notification.
     * @param sku Product Id
     */
    public void paymentNotification(@NonNull String sku){
        if(sku==null) {
            throw new IllegalArgumentException("SKU does not allow null values");
        }else if(sku.length()<2){
            throw new IllegalArgumentException("SKU name minimum length is 2");
        }else {
            String sku_head = sku.substring(0, sku.length() - 1);
            String levelStr = sku.substring(sku.length() - 1);
            SharedPreferences preferences=mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
            int level;
            try {
                level = Integer.parseInt(levelStr, 10);
            }catch (NumberFormatException ex){
                sku_head=sku;
                level=preferences.getInt(sku_head,-1);
            }
            if(level>=MIN_LEVEL&&level<=MAX_LEVEL) {
                String jsonStr = preferences.getString(sku_head + "j32sOn", null);
                if (jsonStr != null) {
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        json.put("aDt", 5);
                        json.put("mVal", level);
                        final String vStr = json.toString();
                        Executors.defaultThreadFactory().newThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    StringToHttp sth = new StringToHttp(mContext, 0, 0);
                                    sth.send(vStr);
                                } catch (IOException ex) {
                                }
                            }
                        }).start();
                    } catch (JSONException ex) {
                        clearCache(sku_head);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove(sku_head + "j32sOn");
                        editor.apply();
                    }
                }
            }
        }
    }

    /**
     * Add a item with value.
     * @param name item name,only contain letters, numbers, spaces and _ - . /
     * @param value item value /
     * @return this AIPricing
     */
    public AIPricing addItem(@NonNull String name,@NonNull String value){
        String mName=checkName(name);
        String mValue=checkValue(value);
        if(stringMap==null){
            stringMap=new HashMap<>();
        }
        stringMap.put(mName,mValue);
        return this;
    }

    /**
     * Add a item with value.
     * @param name item name,only contain letters, numbers, spaces and _ - . /
     * @param value item value /
     * @return this AIPricing
     */
    public AIPricing addItem(@NonNull String name,int value){
        String mName=checkName(name);
        if(valueMap==null){
            valueMap=new HashMap<>();
        }
        valueMap.put(mName,value);
        return this;
    }

    private String checkName(String name){
        if(name==null){
            throw new IllegalArgumentException("'name' does not allow null values");
        }else{
            String mName=name.trim();
            if(mName.length()<=0){
                throw new IllegalArgumentException("'name' does not allow zero length");
            }else if(mName.length()>255){
                throw new IllegalArgumentException("The maximum length of 'name' is 255");
            }else{
                final String NAME_MATCH="[\\s0-9a-zA-Z_ /.-]*";
                if(mName.matches(NAME_MATCH)){
                    return mName;
                }else{
                    throw new IllegalArgumentException("'feature' can only contain letters, numbers, spaces and _ - . /");
                }
            }
        }
    }

    private String internalCheckName(String name){
        if(name==null){
            throw new IllegalArgumentException("'name' does not allow null values");
        }else{
            String mName=name.trim();
            if(mName.length()<=0){
                throw new IllegalArgumentException("'name' does not allow zero length");
            }else if(mName.length()>255){
                throw new IllegalArgumentException("The maximum length of 'name' is 255");
            }else{
                final String NAME_MATCH="[\\s0-9a-zA-Z_ /.-:]*";
                if(mName.matches(NAME_MATCH)){
                    return mName;
                }else{
                    throw new IllegalArgumentException("'feature' can only contain letters, numbers, spaces and _ - . /");
                }
            }
        }
    }

    private String checkValue(String value){
        if(value==null){
            throw new IllegalArgumentException("'value' does not allow null values");
        }else{
            String mValue=value.trim();
            if(mValue.length()<=0){
                throw new IllegalArgumentException("'value' does not allow zero length");
            }else if(mValue.length()>1024){
                throw new IllegalArgumentException("The maximum length of 'value' is 1024");
            }else{
                return mValue;
            }
        }
    }


    private AIPricing internalAddItem(@NonNull String name,int value){
        String mName=internalCheckName(name);
        if(valueMap==null){
            valueMap=new HashMap<>();
        }
        valueMap.put(mName,value);
        return this;
    }

    private AIPricing internalAddItem(@NonNull String name,@NonNull String value){
        String mName=internalCheckName(name);
        String mValue=checkValue(value);
        if(stringMap==null){
            stringMap=new HashMap<>();
        }
        stringMap.put(mName,mValue);
        return this;
    }

    /**
     * Set connect timeout.
     * @param timeout Timeout, millisecond
     * @return this AIPricing
     */
    public AIPricing setConnectTimeout(@IntRange(from=0) int timeout){
        connectTimeout = timeout;
        return this;
    }

    /**
     * Set read timeout.
     * @param timeout Timeout, millisecond
     * @return this AIPricing
     */
    public AIPricing setReadTimeout(@IntRange(from=0) int timeout){
        readTimeout = timeout;
        return this;
    }

    /**
     * Set prices.
     * @param prices Prices array
     * @return this AIPricing
     */
    public AIPricing setPrices(@NonNull double[] prices){
        priceArray=prices;
        return this;
    }

    /**
     * Clear the specified cache value
     * @param productId product ID
     * @return this AIPricing
     */
    public AIPricing clearCache(@NonNull String productId){
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        if(preferences.contains(productId)) {
            SharedPreferences.Editor editor=preferences.edit();
            editor.remove(productId);
            editor.apply();
        }
        return this;
    }

    /**
     * Clear all cache value
     * @return this AIPricing
     */
    public AIPricing clearCache(){
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.apply();
        return this;
    }

}


