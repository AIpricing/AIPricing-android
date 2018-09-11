package ai.aipricing.billing;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class StringToHttp {
    private String apiKey;
    private int mConnectTimeout,mReadTimeout;

    public StringToHttp(Context context,int connectTimeout,int readTimeout){
        apiKey=getMd5WithSlat(context.getPackageName());
        mConnectTimeout=connectTimeout;
        mReadTimeout=readTimeout;
    }

    public String send(String str) throws IOException{
        URL url = new URL("https://pa.aipricing.ai");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestMethod("POST");
        httpCon.setRequestProperty("x-api-key", apiKey);
        httpCon.setRequestProperty("accept", "application/json");
        httpCon.setRequestProperty("content-type", "application/json");
        if(mConnectTimeout>0) {
            httpCon.setConnectTimeout(mConnectTimeout);
        }
        if(mReadTimeout>0) {
            httpCon.setReadTimeout(mReadTimeout);
        }
        httpCon.setUseCaches(false);
        httpCon.setDoInput(true);
        httpCon.setDoOutput(true);
        OutputStreamWriter dos = new OutputStreamWriter(httpCon.getOutputStream());
        dos.write(str);
        dos.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
        StringBuilder strBuilder=new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            strBuilder.append(line);
        }
        in.close();
        httpCon.disconnect();
        return strBuilder.toString();
    }

    private static String getMd5WithSlat(String val){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((val + "cnc").getBytes());
            byte[] digest = md.digest();
            return byteArrayToHex(digest).toUpperCase();
        }catch (Exception ex){
            return "MN203UC";
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
        char[] resultCharArray =new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>>4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }
}