package ai.aipricing.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import ai.aipricing.AIPricing;
import ai.aipricing.LevelCallback;

public class DemoMainActivity extends Activity implements PurchasesUpdatedListener {

    final String DEMO_API_KEY="efg9P3DyLN3JVQqmWNdti4dz3N22RDhg8NeWgPPz";
    final String SKU_HEAD="demo_sku";
    AIPricing aiPricing;
    BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);

        //Initialization Google play bill
        billingClient = BillingClient.newBuilder(this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                }else {
                    log("Billing setup error, Code="+billingResponseCode);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    @Override
    protected void onDestroy(){
        if(billingClient !=null){
            billingClient.endConnection();
            billingClient =null;
        }
        super.onDestroy();
    }

    public void purchasesClick(View view){
        clearLog();
        log("-------- In-App Purchases Log --------");
        log("1. Initialize the AIPricing SDK");
        aiPricing =new AIPricing(this,DEMO_API_KEY);
        log("2. Get in-app purchase level");
        aiPricing.getLevel(SKU_HEAD, 3, new LevelCallback() {
            @Override
            public void onSuccess(final int level) {
                log("3. Response purchase level is "+level);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String productID=SKU_HEAD+level;
                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                .setSku(productID)
                                .setType(BillingClient.SkuType.INAPP)
                                .build();
                        log("4. Launch Billing,  Product ID is '"+productID+"'");
                        billingClient.launchBillingFlow(DemoMainActivity.this,flowParams);
                    }
                });

            }
        });
    }

    public void advancedPurchasesClick(View view){
        clearLog();
        log("-------- Advanced Purchases Log --------");
        log("1. Initialize the AIPricing SDK");
        aiPricing =new AIPricing(this,DEMO_API_KEY);
        aiPricing.prepareGetLevel(SKU_HEAD,2,null);
        log("2. Get in-app purchase level");
        aiPricing.clearCache(SKU_HEAD).
                setConnectTimeout(5000).
                setReadTimeout(4000).
                setPrices(new double[]{0.99,1.99,2.99,3.99}).
                addItem("Membership","Member Platinum").
                addItem("Gender","Female").
                addItem("Course Progress",2).
                addItem("Study-Count",26).
                getLevel(SKU_HEAD, 2, new LevelCallback() {
                    @Override
                    public void onSuccess(final int level) {
                        log("3. Response purchase level is "+level);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String productID=SKU_HEAD+level;
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSku(productID)
                                        .setType(BillingClient.SkuType.INAPP)
                                        .build();
                                log("4. Launch Billing,  Product ID is '"+productID+"'");
                                billingClient.launchBillingFlow(DemoMainActivity.this,flowParams);
                            }
                        });
                    }
                });
    }

    @Override
    public void onPurchasesUpdated(@BillingClient.BillingResponse int responseCode, @Nullable List<Purchase> purchases){
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            aiPricing.paymentNotification();
            log("5. Purchase successful, send notification.");
        }
    }

    public void viewConsoleClick(View view) {
        Intent intent=new Intent(this,GpActivity.class);
        startActivity(intent);
    }

    private void clearLog(){
        TextView logView=findViewById(R.id.logView);
        logView.setText("");
    }
    private void log(final Object obj){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView logView=findViewById(R.id.logView);
                logView.setText(logView.getText()+"\n"+obj.toString());
                ScrollView textScroll=findViewById(R.id.textScroll);
                textScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }
}
