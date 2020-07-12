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

@Database(entities = {Bug.class, Fish.class}, version = 13, exportSchema = false)
public abstract class BebeteDatabase extends RoomDatabase {

    public abstract BugDao bugDao();
    public abstract  FishDao fishDao();

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
                                if(name.equals("char")) {
                                    // TODO : remove from bug and add only in fish
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

//                Bug bug = new Bug(1,"Piéride de la rave",160,"Dans l'air",4,19,9,6);
//                bug_dao.insert(bug);
//                bug = new Bug(2,"Citrin",160,"Dans l'air",4,19,3,6);
//                bug_dao.insert(bug);
//                bug = new Bug(3,"Citrin",160,"Dans l'air",4,19,9,10);
//                bug_dao.insert(bug);
//                bug = new Bug(4,"Machaon",240,"Dans l'air",4,19,3,9);
//                bug_dao.insert(bug);
//                bug = new Bug(5,"Papilio bianor",2500,"Dans l'air by Hybrid Flowers",4,19,3,6);
//                bug_dao.insert(bug);
//                bug = new Bug(6,"Graphium sarpedon",300,"Dans l'air",4,19,4,8);
//                bug_dao.insert(bug);
//                bug = new Bug(7,"Grand planeur",1000,"Dans l'air",8,19,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(8,"Grand papillon pourpre",3000,"Dans l'air",4,19,5,8);
//                bug_dao.insert(bug);
//                bug = new Bug(9,"Monarque",140,"Dans l'air",4,17,9,11);
//                bug_dao.insert(bug);
//                bug = new Bug(10,"Morpho bleu",4000,"Dans l'air",17,8,12,3);
//                bug_dao.insert(bug);
//                bug = new Bug(11,"Morpho bleu",4000,"Dans l'air",17,8,6,9);
//                bug_dao.insert(bug);
//                bug = new Bug(12,"Agrias",3000,"Dans l'air",8,17,4,9);
//                bug_dao.insert(bug);
//                bug = new Bug(13,"Troides brookiana",2500,"Dans l'air",8,17,12,2);
//                bug_dao.insert(bug);
//                bug = new Bug(14,"Troides brookiana",2500,"Dans l'air",8,17,4,9);
//                bug_dao.insert(bug);
//                bug = new Bug(15,"Troides alexandrae",4000,"Dans l'air",8,16,5,9);
//                bug_dao.insert(bug);
//                bug = new Bug(16,"Papillon de nuit",130,"Dans l'air by Light",19,4,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(17,"Attacus Atlas",3000,"Sur les arbres",19,4,4,9);
//                bug_dao.insert(bug);
//                bug = new Bug(18,"Chrysiridia rhipheus",2500,"Dans l'air",8,16,4,9);
//                bug_dao.insert(bug);
//                bug = new Bug(19,"Acrida cinerea",200,"Sur le sol",8,19,4,11);
//                bug_dao.insert(bug);
//                bug = new Bug(20,"Criquet pélerin",600,"Sur le sol",8,19,8,11);
//                bug_dao.insert(bug);
//                bug = new Bug(21,"Rice grasshopper",160,"Sur le sol",8,19,8,11);
//                bug_dao.insert(bug);
//                bug = new Bug(22,"Sauterelle",160,"Sur le sol",8,17,7,9);
//                bug_dao.insert(bug);
//                bug = new Bug(23,"Criquet",130,"Sur le sol",17,8,9,11);
//                bug_dao.insert(bug);
//                bug = new Bug(24,"Grillon du midi",430,"Sur le sol",17,8,9,10);
//                bug_dao.insert(bug);
//                bug = new Bug(25,"Mante religieuse",430,"Sur les fleurs",8,17,3,11);
//                bug_dao.insert(bug);
//                bug = new Bug(26,"Mante orchidée",2400,"Sur les fleurs (White)",8,17,3,11);
//                bug_dao.insert(bug);
//                bug = new Bug(27,"Abeille naine",200,"Dans l'air",8,17,3,7);
//                bug_dao.insert(bug);
//                bug = new Bug(28,"Guêpe",2500,"Dans les arbres",0,0,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(29,"Cigale cercope",250,"Sur les arbres",8,17,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(30,"Cigale hyalessa",300,"Sur les arbres",8,17,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(31,"Cigale géante",500,"Sur les arbres",8,17,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(32,"Cigale cicadelle",400,"Sur les arbres",8,17,8,9);
//                bug_dao.insert(bug);
//                bug = new Bug(33,"Cigale higurashi",550,"Sur les arbres",4,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(34,"Cigale higurashi",550,"Sur les arbres",16,19,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(35,"Mue de cigale",10,"Sur les arbres",0,0,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(36,"Demoiselle",180,"Dans l'air",8,19,9,10);
//                bug_dao.insert(bug);
//                bug = new Bug(37,"Anax napolitain",230,"Dans l'air",8,17,4,10);
//                bug_dao.insert(bug);
//                bug = new Bug(38,"Cordulégastre",4500,"Dans l'air",8,17,5,10);
//                bug_dao.insert(bug);
//                bug = new Bug(39,"Sympetrum",500,"Dans l'air",0,0,11,2);
//                bug_dao.insert(bug);
//                bug = new Bug(40,"Luciole",300,"Dans l'air",19,4,6,6);
//                bug_dao.insert(bug);
//                bug = new Bug(41,"Taupe-grillon",500,"Sous terre",0,0,11,5);
//                bug_dao.insert(bug);
//                bug = new Bug(42,"Patineur",130,"Dans l'eau",8,19,5,9);
//                bug_dao.insert(bug);
//                bug = new Bug(43,"Dytique",800,"Dans l'eau",8,19,5,9);
//                bug_dao.insert(bug);
//                bug = new Bug(44,"Punaise d'eau géante",2000,"Dans l'eau",19,8,4,9);
//                bug_dao.insert(bug);
//                bug = new Bug(45,"Punaise",120,"Sur les fleurs",0,0,3,10);
//                bug_dao.insert(bug);
//                bug = new Bug(46,"Catacanthus",1000,"Sur les fleurs",19,8,3,10);
//                bug_dao.insert(bug);
//                bug = new Bug(47,"Coccinelle",200,"Sur les fleurs",8,17,3,6);
//                bug_dao.insert(bug);
//                bug = new Bug(48,"Coccinelle",200,"Sur les fleurs",8,17,10,10);
//                bug_dao.insert(bug);
//                bug = new Bug(49,"Cicindèle",1500,"Sur le sol",0,0,2,10);
//                bug_dao.insert(bug);
//                bug = new Bug(50,"Bupreste",2400,"Sur les souches",0,0,4,8);
//                bug_dao.insert(bug);
//                bug = new Bug(51,"Mormolyce",450,"Sur les souches",0,0,5,6);
//                bug_dao.insert(bug);
//                bug = new Bug(52,"Mormolyce",450,"Sur les souches",0,0,9,11);
//                bug_dao.insert(bug);
//                bug = new Bug(53,"Capricorne des agrumes",350,"Sur les souches",0,0,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(54,"Rosalia batesi",3000,"Sur les souches",0,0,5,9);
//                bug_dao.insert(bug);
//                bug = new Bug(55,"Scarabée bleu",800,"Sur les arbres (cocotier?)",0,0,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(56,"Bousier",3000,"Sur le sol (rolling snowballs)",0,0,12,2);
//                bug_dao.insert(bug);
//                bug = new Bug(57,"Geotrupidae",300,"Sur le sol",0,0,7,9);
//                bug_dao.insert(bug);
//                bug = new Bug(58,"Scarabée",10000,"Sur les arbres",23,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(59,"Cétoine dorée",200,"Sur les arbres",0,0,6,8);
//                bug_dao.insert(bug);
//                bug = new Bug(60,"Scarabée Goliath",8000,"Sur les arbres (cocotier)",17,8,6,9);
//                bug_dao.insert(bug);
//                bug = new Bug(61,"Lucane inclinatus",2000,"Sur les arbres",0,0,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(62,"Lucane miyama",1000,"Sur les arbres",0,0,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(63,"Lucane cerf-volant",10000,"Sur les arbres",23,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(64,"Lucane copris irisé",6000,"Sur les arbres",19,8,6,9);
//                bug_dao.insert(bug);
//                bug = new Bug(65,"Lucane cyclommatus",8000,"Sur les arbres (cocotier)",17,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(66,"Lucane lamprima",12000,"Sur les arbres (cocotier)",17,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(67,"Lucane girafe",12000,"Sur les arbres (cocotier?)",17,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(68,"Scarabée kabuto",1350,"Sur les arbres",17,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(69,"Scarabée Atlas",8000,"Sur les arbres (cocotier)",17,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(70,"Scarabée éléphant",8000,"Sur les arbres (cocotier)",17,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(71,"Scarabée Hercule",12000,"Sur les arbres (cocotier)",17,8,7,8);
//                bug_dao.insert(bug);
//                bug = new Bug(72,"Phasme",600,"Sur les arbres",4,8,7,11);
//                bug_dao.insert(bug);
//                bug = new Bug(73,"Phasme",600,"Sur les arbres",17,19,7,11);
//                bug_dao.insert(bug);
//                bug = new Bug(74,"Phyllie",600,"Sur le sol",0,0,7,9);
//                bug_dao.insert(bug);
//                bug = new Bug(75,"Psyché",600,"Dans les arbres",0,0,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(76,"Fourmi",80,"Navets pourris",0,0,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(77,"Bernard-l'ermite",1000,"Sur la plage",19,8,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(78,"Ligie",200,"Sur la plage",0,0,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(79,"Mouche",60,"Sur les déchets",0,0,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(80,"Moustique",130,"Dans l'air",17,4,6,9);
//                bug_dao.insert(bug);
//                bug = new Bug(81,"Puce",70,"Sur les animaux",0,0,4,11);
//                bug_dao.insert(bug);
//                bug = new Bug(82,"Escargot",250,"Sur les rochers (Pluie)",0,0,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(83,"Cloporte",250,"Sous les rochers",23,16,9,6);
//                bug_dao.insert(bug);
//                bug = new Bug(84,"Mille-pattes",300,"Sous les rochers",16,23,9,6);
//                bug_dao.insert(bug);
//                bug = new Bug(85,"Araignée",480,"Dans les arbres",19,8,0,0);
//                bug_dao.insert(bug);
//                bug = new Bug(86,"Tarentule",8000,"Sur le sol",19,4,11,4);
//                bug_dao.insert(bug);
//                bug = new Bug(87,"Scorpion",8000,"Sur le sol",19,4,5,10);
//                bug_dao.insert(bug);


                FishDao fish_dao = INSTANCE.fishDao();
                fish_dao.deleteAll();

                Fish fish = new Fish(1,"Bouvière",900,"Rivière",1,0,0,11,3);
                fish_dao.insert(fish);
                fish = new Fish(2,"Chevaine",200,"Rivière",1,9,16,0,0);
                fish_dao.insert(fish);
                fish = new Fish(3,"Carassin",160,"Rivière",2,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(4,"Vandoise",192,"Rivière",3,16,9,0,0);
                fish_dao.insert(fish);
                fish = new Fish(5,"Carpe",300,"Étang",4,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(6,"Carpe koï",4000,"Étang",4,16,9,0,0);
                fish_dao.insert(fish);
                fish = new Fish(7,"Poisson rouge",1300,"Étang",1,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(8,"Cyprin doré",1300,"Étang",1,9,16,0,0);
                fish_dao.insert(fish);
                fish = new Fish(9,"Ranchu",4500,"Étang",2,9,16,0,0);
                fish_dao.insert(fish);
                fish = new Fish(10,"Fondule barré",300,"Étang",1,0,0,4,8);
                fish_dao.insert(fish);
                fish = new Fish(11,"Écrevisse",200,"Étang",2,0,0,4,9);
                fish_dao.insert(fish);
                fish = new Fish(12,"Tortue trionyx",3750,"Rivière",4,16,9,8,9);
                fish_dao.insert(fish);
                fish = new Fish(13,"Tortue serpentine",5000,"Rivière",5,21,4,4,10);
                fish_dao.insert(fish);
                fish = new Fish(14,"Têtard",100,"Étang",1,0,0,3,7);
                fish_dao.insert(fish);
                fish = new Fish(15,"Grenouille",120,"Étang",2,0,0,5,8);
                fish_dao.insert(fish);
                fish = new Fish(16,"Gobie d'eau douce",400,"Rivière",2,16,9,0,0);
                fish_dao.insert(fish);
                fish = new Fish(17,"Loche d'étang",400,"Rivière",2,0,0,3,5);
                fish_dao.insert(fish);
                fish = new Fish(18,"Silure",800,"Étang",4,16,9,5,10);
                fish_dao.insert(fish);
                fish = new Fish(19,"Tête de serpent",5500,"Étang",5,9,16,6,8);
                fish_dao.insert(fish);
                fish = new Fish(20,"Crapet",180,"Rivière",2,9,16,0,0);
                fish_dao.insert(fish);
                fish = new Fish(21,"Perche jaune",300,"Rivière",3,0,0,10,3);
                fish_dao.insert(fish);
                fish = new Fish(22,"Bar",320,"Rivière",4,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(23,"Tilapia",800,"Rivière",3,0,0,6,10);
                fish_dao.insert(fish);
                fish = new Fish(24,"Brochet",1800,"Rivière",5,0,0,9,12);
                fish_dao.insert(fish);
                fish = new Fish(25,"Éperlan",500,"Rivière",2,0,0,12,2);
                fish_dao.insert(fish);
                fish = new Fish(26,"Ayu",900,"Rivière",3,0,0,7,9);
                fish_dao.insert(fish);
                fish = new Fish(27,"Saumon masou",800,"Rivière (falaise)",3,16,9,3,6);
                fish_dao.insert(fish);
                fish = new Fish(28,"Saumon masou",800,"Rivière (falaise)",3,16,9,9,11);
                fish_dao.insert(fish);
                fish = new Fish(29,"Omble",3800,"Rivière (falaise)  Étang",3,16,9,3,6);
                fish_dao.insert(fish);
                fish = new Fish(30,"Omble",3800,"Rivière (falaise)  Étang",3,16,9,9,11);
                fish_dao.insert(fish);
                fish = new Fish(31,"Truite dorée",15000,"Rivière (falaise)",3,16,9,3,5);
                fish_dao.insert(fish);
                fish = new Fish(32,"Truite dorée",15000,"Rivière (falaise)",3,16,9,9,11);
                fish_dao.insert(fish);
                fish = new Fish(33,"Dai yu",15000,"Rivière (falaise)",5,16,9,12,3);
                fish_dao.insert(fish);
                fish = new Fish(34,"Saumon",700,"Rivière (embouchure)",4,0,0,9,9);
                fish_dao.insert(fish);
                fish = new Fish(35,"Saumon roi",1800,"Rivière (embouchure)",6,0,0,9,9);
                fish_dao.insert(fish);
                fish = new Fish(36,"Crabe chinois",2000,"Rivière",2,16,9,9,11);
                fish_dao.insert(fish);
                fish = new Fish(37,"Guppy",1300,"Rivière",1,9,16,4,11);
                fish_dao.insert(fish);
                fish = new Fish(38,"Poisson-docteur",1500,"Rivière",1,9,16,5,9);
                fish_dao.insert(fish);
                fish = new Fish(39,"Poisson-ange",3000,"Rivière",2,16,9,5,10);
                fish_dao.insert(fish);
                fish = new Fish(40,"Combattant",2500,"Rivière",2,9,16,5,10);
                fish_dao.insert(fish);
                fish = new Fish(41,"Néon bleu",500,"Rivière",1,9,16,4,11);
                fish_dao.insert(fish);
                fish = new Fish(42,"Poisson arc-en-ciel",800,"Rivière",1,9,16,5,10);
                fish_dao.insert(fish);
                fish = new Fish(43,"Piranha",2500,"Rivière",2,9,16,6,9);
                fish_dao.insert(fish);
                fish = new Fish(44,"Piranha",2500,"Rivière",2,21,4,6,9);
                fish_dao.insert(fish);
                fish = new Fish(45,"Arowana",10000,"Rivière",4,16,9,6,9);
                fish_dao.insert(fish);
                fish = new Fish(46,"Dorade",15000,"Rivière",5,4,21,6,9);
                fish_dao.insert(fish);
                fish = new Fish(47,"Gar",6000,"Étang",6,16,9,6,9);
                fish_dao.insert(fish);
                fish = new Fish(48,"Arapaïma",10000,"Rivière",6,16,9,6,9);
                fish_dao.insert(fish);
                fish = new Fish(49,"Bichir",4000,"Rivière",4,21,4,6,9);
                fish_dao.insert(fish);
                fish = new Fish(50,"Esturgeon",10000,"Rivière (embouchure)",6,0,0,9,3);
                fish_dao.insert(fish);
                fish = new Fish(51,"Clione",1000,"Océan",1,0,0,12,3);
                fish_dao.insert(fish);
                fish = new Fish(52,"Hippocampe",1100,"Océan",1,0,0,4,11);
                fish_dao.insert(fish);
                fish = new Fish(53,"Poisson-clown",650,"Océan",1,0,0,4,9);
                fish_dao.insert(fish);
                fish = new Fish(54,"Poisson chirurgien",1000,"Océan",2,0,0,4,9);
                fish_dao.insert(fish);
                fish = new Fish(55,"Poisson-papillon",1000,"Océan",2,0,0,4,9);
                fish_dao.insert(fish);
                fish = new Fish(56,"Napoléon",10000,"Océan",6,4,21,7,8);
                fish_dao.insert(fish);
                fish = new Fish(57,"Poisson-scorpion",500,"Océan",3,0,0,4,11);
                fish_dao.insert(fish);
                fish = new Fish(58,"Poisson ballon",5000,"Océan",3,21,4,11,2);
                fish_dao.insert(fish);
                fish = new Fish(59,"Poisson porc-épic",250,"Océan",3,0,0,7,9);
                fish_dao.insert(fish);
                fish = new Fish(60,"Anchois",200,"Océan",2,4,21,0,0);
                fish_dao.insert(fish);
                fish = new Fish(61,"Chinchard",150,"Océan",2,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(62,"Scarus",5000,"Océan",3,0,0,3,11);
                fish_dao.insert(fish);
                fish = new Fish(63,"Bar commun",400,"Océan",5,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(64,"Vivaneau",3000,"Océan",4,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(65,"Limande",300,"Océan",3,0,0,10,4);
                fish_dao.insert(fish);
                fish = new Fish(66,"Cardeau",800,"Océan",5,0,0,0,0);
                fish_dao.insert(fish);
                fish = new Fish(67,"Calmar",500,"Océan",3,0,0,12,8);
                fish_dao.insert(fish);
                fish = new Fish(68,"Murène",2000,"Océan",7,0,0,8,10);
                fish_dao.insert(fish);
                fish = new Fish(69,"Murène ruban bleue",600,"Océan",7,0,0,6,10);
                fish_dao.insert(fish);
                fish = new Fish(70,"Thon",7000,"Ponton",6,0,0,11,4);
                fish_dao.insert(fish);
                fish = new Fish(71,"Marlin bleu",10000,"Ponton",6,0,0,11,4);
                fish_dao.insert(fish);
                fish = new Fish(72,"Marlin bleu",10000,"Ponton",6,0,0,7,9);
                fish_dao.insert(fish);
                fish = new Fish(73,"Carangue grosse tête",4500,"Ponton",5,0,0,5,10);
                fish_dao.insert(fish);
                fish = new Fish(74,"Coryphène",6000,"Ponton",5,0,0,5,10);
                fish_dao.insert(fish);
                fish = new Fish(75,"Lune de mer",4000,"Océan",6,4,21,7,9);
                fish_dao.insert(fish);
                fish = new Fish(76,"Raie",3000,"Océan",5,4,21,8,11);
                fish_dao.insert(fish);
                fish = new Fish(77,"Requin scie",12000,"Océan",6,16,9,6,9);
                fish_dao.insert(fish);
                fish = new Fish(78,"Requin marteau",8000,"Océan",6,16,9,6,9);
                fish_dao.insert(fish);
                fish = new Fish(79,"Grand requin blanc",15000,"Océan",6,16,9,6,9);
                fish_dao.insert(fish);
                fish = new Fish(80,"Requin baleine",13000,"Océan",6,0,0,6,9);
                fish_dao.insert(fish);
                fish = new Fish(81,"Rémora Rayé",1500,"Océan",4,0,0,6,9);
                fish_dao.insert(fish);
                fish = new Fish(82,"Poisson lanterne",2500,"Océan",4,16,9,11,3);
                fish_dao.insert(fish);
                fish = new Fish(83,"Poisson-ruban",9000,"Océan",6,0,0,12,5);
                fish_dao.insert(fish);
                fish = new Fish(84,"Macropinna",15000,"Océan",2,21,4,0,0);
                fish_dao.insert(fish);
                fish = new Fish(85,"Coelacanthe",15000,"Océan (pluie)",6,0,0,0,0);
                fish_dao.insert(fish);


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