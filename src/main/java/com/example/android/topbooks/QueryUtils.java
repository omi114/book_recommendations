package com.example.android.topbooks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Book> getBooks(String fetchURLStr) throws IOException {

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of Book objects with the corresponding data.

            URL fetchURL = createURL(fetchURLStr);

            String jsonStr = getStrFromURL(fetchURL) ;

            if (jsonStr == null) { return  null ;}

            JSONObject jsonObject = new JSONObject(jsonStr);

            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i =0;i<itemsArray.length();i++) {
                JSONObject bookObj = itemsArray.getJSONObject(i);
                JSONObject infoObj = bookObj.getJSONObject("volumeInfo");

                Log.i("Number of books", ""+itemsArray.length());

                String bookTitle = infoObj.getString("title");
                Log.i("Book Title :", "" + bookTitle);

                //TODO : Update to handle Authors correctly
                JSONArray bookAuthors = infoObj.getJSONArray("authors");
                String [] authStrArr = new String[bookAuthors.length()];
                for (i=0;i<authStrArr.length;i++) {
                    authStrArr[i] = bookAuthors.getString(i);
                    Log.i("Authors :", "" + authStrArr[i]);
                }

                //TODO: Get Thumbnail
                // JSONArray imageLinks = ;

                String infoURL = infoObj.getString("infoLink");
                books.add(new Book(bookTitle, authStrArr[0], infoURL));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }

    private static String getStrFromURL(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        if (url == null) {
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e("makeHttpRequest : ", "Response code is not 200");
            }

        } catch (IOException e) {
            Log.e("makeHttpRequest : ", "IO exception occured");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createURL(String string) throws MalformedURLException {
        if (string == null) {
            return null;
        }

        try {
            return new URL(string);

        } catch (MalformedURLException e ) {

            Log.i("createURL","Malformed URL");
            return null;
        }
    }
}