package com.example.bennettmitchell_final;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import org.jetbrains.annotations.TestOnly;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class APICaller {
    private enum methods{
        GET,
        POST,
    }

    // anonymous function :D

    private static Runnable other = () -> {Log.i("API", "test");};

    // crying about these being public in the code i tried to put them in the apps environment
    private static String clientID = "[REMOVED]";
    private static String clientSecret = "[REMOVED]";
    private static String auth = null;

    private static String query = "fields name,summary,involved_companies,platforms,cover,first_release_date; search \"&\"; limit 10; where version_parent = null;";

    private static void authTwitch(){
        ExecutorService exe = Executors.newSingleThreadExecutor();

        String url = "https://id.twitch.tv/oauth2/token?"; // base url
        url += "client_id=" + clientID;
        url += "&client_secret=" + clientSecret;
        url += "&grant_type=client_credentials";
        JSONObject authJson = callAPI(url, methods.POST);
        try{
            if (authJson != null) {
                auth = authJson.getString("access_token");
            }
            else{
                Log.d("API", "Could not auth twitch, retrying");
                authTwitch(); // omg funny recursion
                //  itll work eventually
            }
        }
        catch (JSONException e){
            Log.e("API", e.toString());
        }
    }

    public static void searchAPI(String search){
        // check if the twitch connection is already there
        if (auth == null){
            authTwitch();
        }
        String url = "https://api.igdb.com/v4/games";
        Map<String, String> headers = new HashMap<>();
        headers.put("Client-ID", clientID);
        headers.put("Authorization", "Bearer " + auth);
        //
        String body = query.replace("&", search);
        Log.i("API", "Query " + body);
        JSONArray games = callAPIArray(url, methods.POST, headers, body);
        if (games != null) {
            try {
                Log.i("API", games.get(3).toString());
            }
            catch (JSONException e){}
        }
    }
    private static JSONObject callAPI(String sUrl, methods method){return callAPI(sUrl, method, null, null);}

    private static JSONObject callAPI(String sUrl, methods method, Map<String, String> requestProperties, String body){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<JSONObject> future = executor.submit(new Callable<JSONObject>() {
            @Override
            public JSONObject call() {
                HttpURLConnection connection = null; // define the connection object as null
                JSONObject json = null;
                // try
                try {
                    URL url = new URL(sUrl); // get the link to the url
                    connection = (HttpURLConnection) url.openConnection(); // define the connection to the url
                    connection.setRequestMethod(method.toString()); // set the request method to get
                    if (requestProperties != null){
                        List<String> keys = new ArrayList<String>(requestProperties.keySet());
                        // put all of the headers into the request
                        for (String key : keys ){
                            connection.setRequestProperty(key, requestProperties.get(key));
                        }
                    }
                    // insert the body into the stream
                    if (body != null){
                        byte[] out = body.getBytes(StandardCharsets.UTF_8);
                        OutputStream os = connection.getOutputStream();
                        os.write(out);
                        os.close();
                    }
                    Log.d("API", Integer.toString(connection.getResponseCode()));
                    InputStream in = connection.getInputStream();
                    BufferedReader isp = new BufferedReader(new InputStreamReader(in)); // create an input stream and its reader


                    String line = isp.readLine();
                    if (line != null){
                        json = new JSONObject(line);
                    }
                }
                catch (IOException e){
                    Log.e("API", e.toString());
                }
                catch (JSONException e) {
                    Log.e("API",e.toString());
//                    return null;
                }
                finally {
                    if (connection != null){
                        connection.disconnect();
                    }

                }
                return json;
            }
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e){
            Log.e("API", "im so tired");
            return null;
        }
    }

    private static JSONArray callAPIArray(String sUrl, methods method, Map<String, String> requestProperties, String body){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<JSONArray> future = executor.submit(new Callable<JSONArray>() {
            @Override
            public JSONArray call() {
                HttpURLConnection connection = null; // define the connection object as null
                JSONArray json = null;
                // try
                try {
                    URL url = new URL(sUrl); // get the link to the url
                    connection = (HttpURLConnection) url.openConnection(); // define the connection to the url
                    connection.setRequestMethod(method.toString()); // set the request method to get
                    if (requestProperties != null){
                        List<String> keys = new ArrayList<String>(requestProperties.keySet());
                        // put all of the headers into the request
                        for (String key : keys ){
                            connection.setRequestProperty(key, requestProperties.get(key));
                        }
                    }
                    // insert the body into the stream
                    if (body != null){
                        byte[] out = body.getBytes(StandardCharsets.UTF_8);
                        OutputStream os = connection.getOutputStream();
                        os.write(out);
                        os.close();
                    }
                    Log.d("API", Integer.toString(connection.getResponseCode()));
                    InputStream in = connection.getInputStream();
                    BufferedReader isp = new BufferedReader(new InputStreamReader(in)); // create an input stream and its reader

                    String jsonIN = "";
                    String line = isp.readLine();
                    while (line != null){
//                        Log.i("API", line);
                        jsonIN += line;
                        line = isp.readLine();
                    }
                    json = new JSONArray(jsonIN);
                }
                catch (IOException e){
                    Log.e("API", e.toString());
                }
                catch (JSONException e) {
                    Log.e("API",e.toString());
//                    return null;
                }
                finally {
                    if (connection != null){
                        connection.disconnect();
                    }

                }
                return json;
            }
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e){
            Log.e("API", "im so tired");
            return null;
        }
    }

    // takes a context to pull from resources
    public static Bitmap getImageFromWeb(Context context, String url){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Bitmap> future = executor.submit(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                if (url != null) {
                    try {
                        InputStream inputStream = (InputStream) new URL(url).getContent();
                        Bitmap draw = BitmapFactory.decodeStream(inputStream);
//                      Drawable draw = Drawable.createFromStream(inputStream, "Box Art");
                        return draw;
                    } catch (MalformedURLException e) {
                        Log.e("Image Error", "Malformed URL");
                    } catch (IOException e) {
                        Log.e("Image Error", "IO Exception");
                    } catch (Exception e) {
                        Log.e("Image Error", e.toString());
                    }
                }

                // return this if something goes wrong so its not nothing that displays
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background); //AppCompatResources.getDrawable(context, R.drawable.ic_launcher_background);
            }
        });
        try{
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }


    // could throw jsonexception
    @TestOnly
    public static void test(){
        try {
            String pkmn = "arcanine";
            String url = "https://pokeapi.co/api/v2/pokemon/" + pkmn;

            JSONObject info = callAPI(url, methods.GET);
            if (info != null) {
                String h = info.getJSONArray("moves").getJSONObject(5).getJSONObject("move").getString("name");
                Log.i("API", h); // bite
            }
        }
        catch (JSONException e){
            Log.e("API", e.toString());
        }
    }
}


