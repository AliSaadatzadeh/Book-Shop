package ir.skynic.bookshop.api;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.skynic.bookshop.RunnableParam;
import ir.skynic.bookshop.Utils;
import ir.skynic.bookshop.model.Book;

public class ApiClient {

    private static String hostName = "http://bookshop.skynic.ir";

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String getJson(String request) {
        try {
            URL url = new URL(hostName + "/" + request);
            Log.d("BK_REQ", hostName + "/" + request);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url= new URL(uri.toASCIIString());

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String result = convertStreamToString(in);

            Log.d("BK_RSP", result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getJson(String[] requestArray) {
        String request = "";
        for (int i = 0; i < requestArray.length; i++) {
            request += requestArray[i] + "/";
        }

        return getJson(request);
    }

    public static class ListOfJson<T> implements ParameterizedType
    {
        private Class<?> wrapped;

        public ListOfJson(Class<T> wrapper)
        {
            this.wrapped = wrapper;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
            return new Type[] { wrapped };
        }

        @Override
        public Type getRawType()
        {
            return List.class;
        }

        @Override
        public Type getOwnerType()
        {
            return null;
        }
    }

    public static void executeCommand(String[] requestArray, RunnableParam onFinished) {
        new Thread(new Runnable() {
            String jsonContent;

            @Override
            public void run() {
                jsonContent =  ApiClient.getJson(requestArray);

                Utils.runOnMainThread(() -> {
                    if(jsonContent == null) {
                        onFinished.run(null);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(jsonContent);
                        String error = jsonObject.getString("error");
                        onFinished.run(Integer.valueOf(error));
                    } catch (Exception e) {
                        onFinished.run(null);
                    }
                });
            }
        }).start();
    }



    public static void executeCommandByImageUpload(Bitmap bitmap, String[] requestArray, RunnableParam onFinished) {
        new Thread(new Runnable() {
            String jsonContent;

            @Override
            public void run() {

                String request = "";
                for (int i = 0; i < requestArray.length; i++) {
                    request += requestArray[i] + "/";
                }

                String url = hostName + "/" + request;

                jsonContent = uploadImage(url, bitmap);

                Utils.runOnMainThread(() -> {
                    if(jsonContent == null) {
                        onFinished.run(null);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(jsonContent);
                        String error = jsonObject.getString("error");
                        onFinished.run(Integer.valueOf(error));
                    } catch (Exception e) {
                        onFinished.run(null);
                    }
                });
            }
        }).start();
    }

    public static <T> void getModel(String[] requestArray, String key, Class<T> tClass, RunnableParam onListReady) {

        new Thread(new Runnable() {
            String jsonContent;

            @Override
            public void run() {
                jsonContent =  ApiClient.getJson(requestArray);

                Utils.runOnMainThread(() -> {
                    if(jsonContent == null) {
                        onListReady.run(null);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(jsonContent);
                        String error = jsonObject.getString("error");

                        if(error.equals("0")) {
                            jsonContent = jsonObject.getJSONArray(key).toString();
                            onListReady.run(0, new Gson().fromJson(jsonContent, new ListOfJson<T>(tClass)));
                        } else {
                            onListReady.run(Integer.valueOf(error));
                        }

                    } catch (Exception e) {
                        onListReady.run(null);
                    }
                });
            }
        }).start();
    }

    public static void getValue(String[] requestArray, String key, RunnableParam onListReady) {

        new Thread(new Runnable() {
            String jsonContent;

            @Override
            public void run() {
                jsonContent =  ApiClient.getJson(requestArray);

                Utils.runOnMainThread(() -> {
                    if(jsonContent == null) {
                        onListReady.run(null);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(jsonContent);
                        String error = jsonObject.getString("error");

                        if(error.equals("0")) {
                            String value = jsonObject.getString(key);
                            onListReady.run(0, value);
                        } else {
                            onListReady.run(Integer.valueOf(error));
                        }

                    } catch (Exception e) {
                        onListReady.run(null);
                    }
                });
            }
        }).start();
    }

    public static String uploadImage(String urlServer, Bitmap image) {
        try {
            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bufferSize;
            byte[] buffer;


            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename=\""
                    + "myupload.jpg" + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 70, out);
            buffer = out.toByteArray();


            bufferSize = buffer.length;

            outputStream.write(buffer, 0, bufferSize);

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String strCurrentLine;

            outputStream.flush();
            outputStream.close();

            String responseBody = "";
            while ((strCurrentLine = bufferedReader.readLine()) != null) {
                responseBody += strCurrentLine;
            }

            return responseBody;
        } catch (Exception e) {
            return null;
        }
    }
}
