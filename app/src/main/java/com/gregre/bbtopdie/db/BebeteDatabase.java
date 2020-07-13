package com.gregre.bbtopdie.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.  In a real
 * app, consider exporting the schema to help you with migrations.
 */

@Database(entities = {Bug.class, Fish.class,SeaCreature.class}, version = 15, exportSchema = false)
public abstract class BebeteDatabase extends RoomDatabase {

    public abstract BugDao bugDao();
    public abstract  FishDao fishDao();
    public abstract  SeaCreatureDao seaCreatureDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile BebeteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BebeteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BebeteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BebeteDatabase.class, "acnh_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);


            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more bugs, just add them.

                BugDao bug_dao = INSTANCE.bugDao();
                bug_dao.deleteAll();

                // Create URL
                URL endpoint = null;
                try {
                    endpoint = new URL("https://acnhapi.com/v1/bugs/");

                    // Create connection
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) endpoint.openConnection();

                    if (myConnection.getResponseCode() == 200) {
                        // Success
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);

                        jsonReader.beginObject(); // Start processing the JSON object

                        int id = -1;
                        String name = null;
                        String name_fr = null;
                        String month_northern = null;
                        List<Integer> month_array_northern = null;
                        String month_southern = null;
                        List<Integer> month_array_southern = null;
                        String time = null;
                        List<Integer> time_array = null;
                        boolean isAllDay = false;
                        boolean isAllYear = false;
                        String location = null;
                        String rarity = null;
                        int price = -1;
                        String icon_uri = null;
                        String key;
                        boolean fullEntry = false;

                        while (jsonReader.hasNext()) { // Loop through all keys

                            key = jsonReader.nextName(); // Fetch the next key

                            if (jsonReader.peek().equals(JsonToken.BEGIN_OBJECT)) {
                                jsonReader.beginObject();
                                key = jsonReader.nextName(); // Fetch the next key

                            }

                            if (key.equals("id")) {
                                id = jsonReader.nextInt();
                            } else if (key.equals("file-name")) {
                                name = jsonReader.nextString();
                                Pattern p = Pattern.compile("-");
                                Matcher m = p.matcher(name);
                                name = m.replaceAll("_");
                            } else if (key.equals("name-EUfr")) {
                                name_fr = jsonReader.nextString();
                            } else if (key.equals("month-northern")) {
                                month_northern = jsonReader.nextString();
                            } else if (key.equals("month-array-northern")) {
                                month_array_northern = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    month_array_northern.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("month-southern")) {
                                month_southern = jsonReader.nextString();
                            } else if (key.equals("month-array-southern")) {
                                month_array_southern = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    month_array_southern.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("time")) {
                                time = jsonReader.nextString();
                            } else if(key.equals("time-array")) {
                                time_array = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    time_array.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("isAllDay")) {
                                isAllDay = jsonReader.nextBoolean();
                            } else if (key.equals("isAllYear")) {
                                isAllYear = jsonReader.nextBoolean();
                            } else if (key.equals("location")) {
                                location = jsonReader.nextString();
                            } else if (key.equals("rarity")) {
                                rarity = jsonReader.nextString();
                            } else if (key.equals("price")) {
                                price = jsonReader.nextInt();
                            } else if(key.equals("icon_uri")) {
                                icon_uri = jsonReader.nextString();
                                fullEntry = true;
                            } else {
                                jsonReader.skipValue();
                            }

                            if(fullEntry) {
                                name_fr = capitalize(name_fr);

                                Bug bug = new Bug(id, name, name_fr, month_northern, month_array_northern, month_southern, month_array_southern, time, time_array, isAllDay, isAllYear, location, rarity, price, icon_uri);
                                bug_dao.insert(bug);

                                fullEntry = false;
                            }

                            if (jsonReader.peek().equals(JsonToken.END_OBJECT)) {
                                jsonReader.endObject();
                            }

                        }

                        jsonReader.close();
                    } else {
                        // Error handling code goes here
                    }
                    myConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FishDao fish_dao = INSTANCE.fishDao();
                fish_dao.deleteAll();

                // Create URL
                endpoint = null;
                try {
                    endpoint = new URL("https://acnhapi.com/v1/fish/");

                    // Create connection
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) endpoint.openConnection();

                    if (myConnection.getResponseCode() == 200) {
                        // Success
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);

                        jsonReader.beginObject(); // Start processing the JSON object

                        int id = -1;
                        String name = null;
                        String name_fr = null;
                        String month_northern = null;
                        List<Integer> month_array_northern = null;
                        String month_southern = null;
                        List<Integer> month_array_southern = null;
                        String time = null;
                        List<Integer> time_array = null;
                        boolean isAllDay = false;
                        boolean isAllYear = false;
                        String location = null;
                        String rarity = null;
                        int price = -1;
                        String shadow = null;
                        String icon_uri = null;
                        String key;
                        boolean fullEntry = false;

                        while (jsonReader.hasNext()) { // Loop through all keys

                            key = jsonReader.nextName(); // Fetch the next key

                            if (jsonReader.peek().equals(JsonToken.BEGIN_OBJECT)) {
                                jsonReader.beginObject();
                                key = jsonReader.nextName(); // Fetch the next key

                            }

                            if (key.equals("id")) {
                                id = jsonReader.nextInt();
                            } else if (key.equals("file-name")) {
                                name = jsonReader.nextString();
                                if(name.equals("char")) {
                                    name = "char_fish"; // because APPARENTLY there is a FISH CALLED CHAR
                                } else {
                                    Pattern p = Pattern.compile("-");
                                    Matcher m = p.matcher(name);
                                    name = m.replaceAll("_");
                                }
                            } else if (key.equals("name-EUfr")) {
                                name_fr = jsonReader.nextString();
                            } else if (key.equals("month-northern")) {
                                month_northern = jsonReader.nextString();
                            } else if (key.equals("month-array-northern")) {
                                month_array_northern = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    month_array_northern.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("month-southern")) {
                                month_southern = jsonReader.nextString();
                            } else if (key.equals("month-array-southern")) {
                                month_array_southern = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    month_array_southern.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("time")) {
                                time = jsonReader.nextString();
                            } else if(key.equals("time-array")) {
                                time_array = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    time_array.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("isAllDay")) {
                                isAllDay = jsonReader.nextBoolean();
                            } else if (key.equals("isAllYear")) {
                                isAllYear = jsonReader.nextBoolean();
                            } else if (key.equals("location")) {
                                location = jsonReader.nextString();
                            } else if (key.equals("rarity")) {
                                rarity = jsonReader.nextString();
                            } else if (key.equals("price")) {
                                price = jsonReader.nextInt();
                            } else if (key.equals("shadow")) {
                                shadow = jsonReader.nextString();
                            } else if(key.equals("icon_uri")) {
                                icon_uri = jsonReader.nextString();
                                fullEntry = true;
                            } else {
                                jsonReader.skipValue();
                            }

                            if(fullEntry) {
                                name_fr = capitalize(name_fr);

                                Fish fish = new Fish(id, name, name_fr, month_northern, month_array_northern, month_southern, month_array_southern, time, time_array, isAllDay, isAllYear, location, rarity, price, shadow, icon_uri);
                                fish_dao.insert(fish);

                                fullEntry = false;
                            }

                            if (jsonReader.peek().equals(JsonToken.END_OBJECT)) {
                                jsonReader.endObject();
                            }

                        }

                        jsonReader.close();
                    } else {
                        // Error handling code goes here
                    }
                    myConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SeaCreatureDao seaCreatureDao = INSTANCE.seaCreatureDao();
                seaCreatureDao.deleteAll();

                // Create URL
                endpoint = null;
                try {
                    endpoint = new URL("https://acnhapi.com/v1/sea/");

                    // Create connection
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) endpoint.openConnection();

                    if (myConnection.getResponseCode() == 200) {
                        // Success
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);

                        jsonReader.beginObject(); // Start processing the JSON object

                        int id = -1;
                        String name = null;
                        String name_fr = null;
                        String month_northern = null;
                        List<Integer> month_array_northern = null;
                        String month_southern = null;
                        List<Integer> month_array_southern = null;
                        String time = null;
                        List<Integer> time_array = null;
                        boolean isAllDay = false;
                        boolean isAllYear = false;
                        String speed = null;
                        String shadow = null;
                        int price = -1;
                        String icon_uri = null;
                        String key;
                        boolean fullEntry = false;

                        while (jsonReader.hasNext()) { // Loop through all keys

                            key = jsonReader.nextName(); // Fetch the next key

                            if (jsonReader.peek().equals(JsonToken.BEGIN_OBJECT)) {
                                jsonReader.beginObject();
                                key = jsonReader.nextName(); // Fetch the next key

                            }

                            if (key.equals("id")) {
                                id = jsonReader.nextInt();
                            } else if (key.equals("file-name")) {
                                name = jsonReader.nextString();
                                if(name.equals("char")) {
                                    name = "char_fish"; // because APPARENTLY there is a FISH CALLED CHAR
                                } else {
                                    Pattern p = Pattern.compile("-");
                                    Matcher m = p.matcher(name);
                                    name = m.replaceAll("_");
                                }
                            } else if (key.equals("name-EUfr")) {
                                name_fr = jsonReader.nextString();
                            } else if (key.equals("month-northern")) {
                                month_northern = jsonReader.nextString();
                            } else if (key.equals("month-array-northern")) {
                                month_array_northern = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    month_array_northern.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("month-southern")) {
                                month_southern = jsonReader.nextString();
                            } else if (key.equals("month-array-southern")) {
                                month_array_southern = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    month_array_southern.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("time")) {
                                time = jsonReader.nextString();
                            } else if(key.equals("time-array")) {
                                time_array = new ArrayList<>();
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    time_array.add(jsonReader.nextInt());
                                }
                                jsonReader.endArray();
                            } else if (key.equals("isAllDay")) {
                                isAllDay = jsonReader.nextBoolean();
                            } else if (key.equals("isAllYear")) {
                                isAllYear = jsonReader.nextBoolean();
                            } else if (key.equals("speed")) {
                                speed = jsonReader.nextString();
                            } else if (key.equals("price")) {
                                price = jsonReader.nextInt();
                            } else if (key.equals("shadow")) {
                                shadow = jsonReader.nextString();
                            } else if(key.equals("icon_uri")) {
                                icon_uri = jsonReader.nextString();
                                fullEntry = true;
                            } else {
                                jsonReader.skipValue();
                            }

                            if(fullEntry) {
                                name_fr = capitalize(name_fr);

                                SeaCreature seaCreature = new SeaCreature(id, name, name_fr, month_northern, month_array_northern, month_southern, month_array_southern, time, time_array, isAllDay, isAllYear, speed, shadow, price, icon_uri);
                                seaCreatureDao.insert(seaCreature);
                                fullEntry = false;
                            }

                            if (jsonReader.peek().equals(JsonToken.END_OBJECT)) {
                                jsonReader.endObject();
                            }

                        }

                        jsonReader.close();
                    } else {
                        // Error handling code goes here
                    }
                    myConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
    };

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


}