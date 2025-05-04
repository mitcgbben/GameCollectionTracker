package com.example.bennettmitchell_final.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.bennettmitchell_final.Game;
import com.example.bennettmitchell_final.Status;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    // i made a new exception plink
    public static class ConnectionException extends Exception{
        private String error;
        public ConnectionException(String error) {
            this.error = error;
        }
        @Override
        public String toString(){
            return error;
        }
    }
    // anonymous function :D


    // crying about these being public in the code i tried to put them in the apps environment
    private static String clientID = "dk7y9m5rzgwaombcvdt6zs2cnkkrza";
    private static String clientSecret = "jdlpi7y6rgicfaf9jzahktfakh7wnc";
    private static String auth = null;
    private static String imageURL = "https://images.igdb.com/igdb/image/upload/t_cover_small/";


    private static String query = "fields name,summary,involved_companies.developer, involved_companies.publisher, involved_companies.company.name,platforms,cover.image_id,first_release_date; search \"&\"; limit 20; where parent_game = null;";

    private static void authTwitch() throws ConnectionException {authTwitch(10);}
    private static void authTwitch(int maxAttempts) throws ConnectionException{
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
                if (maxAttempts > 0) {
                    Log.d("API", "Could not auth twitch, retrying");
                    authTwitch(maxAttempts - 1); // omg funny recursion
                }
                else{
                    throw new ConnectionException("Could not connect to twitch");
                }
            }
        }
        catch (JSONException e){
            Log.e("API", e.toString());
        }
    }

    public static List<Game> searchAPI(String search) throws ConnectionException{
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
                return getGames(games);
            }
            catch (JSONException e){
                Log.e("API", e.toString());
                return null;
            }
        }
        return null;
    }

    private static List<Game> getGames(JSONArray gameList) throws JSONException{
        ArrayList<Game> games = new ArrayList<>();
        // iterate through all the found games
        for (int i = 0; i < gameList.length(); i++){
//            Log.i("API", Integer.toString(i));
            JSONObject gameJson = gameList.optJSONObject(i); // opt so it doesnt throw an error if a field isnt there
            if (gameJson != null) {
                String name = gameJson.optString("name");
                String description = gameJson.optString("summary");
                int releaseDate = gameJson.optInt("first_release_date");
                JSONArray companies = gameJson.optJSONArray("involved_companies");
//                JSONArray platforms = gameJson.optJSONArray("platforms");

                Date plink = new Date((long) (releaseDate * 1000L));
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
//                Log.d("API", Integer.toString(releaseDate));
                String releaseDateS = dateFormat.format(plink);
                String publisher = "";
                String developer = "";
                if (companies != null){
                    int length = companies.length();
                    for (int j = 0; j < length; j++){
                        JSONObject companyJson = companies.optJSONObject(j);
                        if (companyJson != null) {
//                            Log.d("API", "Outer company not null");
                            JSONObject companyInfo = companyJson.optJSONObject("company");
                            if (companyInfo != null) {
                                String companyName = companyInfo.optString("name");
//                                Log.d("API", "Inner Company #" + j + " for game " + name + " not null " + companyName);
                                if (companyJson.getBoolean("developer")) {
                                    developer = companyName;
                                }
                                if (companyJson.getBoolean("publisher")) {
                                    publisher = companyName;
                                }
                            }
                        }
                    }
                }
                Game newGame = new Game(name, releaseDateS, null, developer, publisher, description, "", new Status(), new Status(), 0);
//                int cover = gameJson.optInt("cover");
                JSONObject cover = gameJson.optJSONObject("cover");
                if (cover != null){
                    String id = cover.optString("image_id");
                    if (!id.isEmpty()){
                        String url = imageURL + id + ".jpg";
//                        Log.d("API", url);
                        newGame.setBoxArt(ImageHandler.getImageFromWeb(url));
                    }
                    else{
                        Log.d("API","id was empty");
                    }
                }



                games.add(newGame);
            }
        }
        return games;
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


