AIPricing-android
=====

AIPricing helps you to implement dynamic pricing features in android applications.

Quick experience
-----
Search and Run '`aipricing`' in `Google Play`.

<a href="https://aipricing.ai/img/rm_gp_demo1.png"><img src="https://aipricing.ai/img/rm_gp_demo1.png" height=96></a>
<a href="https://aipricing.ai/img/rm_gp_demo2.png"><img src="https://aipricing.ai/img/rm_gp_demo2.png" height=96></a>

Installation and Usage
---------
1. Add this line to your app's `build.gradle` inside the `dependencies` section:
```java
    implementation 'ai.aipricing:sdk:2.2'
```
2. Get a purchase level and purchase
```java
    String SKU_HEAD="demo_sku";
    AIPricing aiPricing =new AIPricing(this);
    aiPricing.getLevel(SKU_HEAD, 3, new LevelCallback() {
        public void onSuccess(int level) {
            String productID=SKU_HEAD+level;
            //Use productID to launch Billing
        }
    });
```
3. If purchase sucessfuly, send payment notification
```java
    AIPricing aiPricing=new AIPricing(this);
    aiPricing.paymentNotification(purchase.getSku());
```
4. Create an In-app entry in the Google Play Console.

Item is characterized by head + number, the number starting from 1, maximum 9, arranged from low to high prices.

<a href="https://aipricing.ai/img/rm_gp_sku1.png"><img src="https://aipricing.ai/img/rm_gp_sku1.png" height=96></a>
<a href="https://aipricing.ai/img/rm_gp_sku2.png"><img src="https://aipricing.ai/img/rm_gp_sku2.png" height=96></a>

Building Demo Project
---------
Choose `File -> New -> Project from Version Control->Github`, Git Repository URL: [https://github.com/aipricing/AIPricing-android.git](https://github.com/aipricing/AIPricing-android.git).

<a href="https://aipricing.ai/img/rm_building1.png"><img src="https://aipricing.ai/img/rm_building1.png" height=96></a>
<a href="https://aipricing.ai/img/rm_building2.png"><img src="https://aipricing.ai/img/rm_building2.png" height=96></a>