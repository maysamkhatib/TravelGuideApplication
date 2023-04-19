package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USER(EMAIL TEXT PRIMARY KEY, FIRSTNAME TEXT, LASTNAME TEXT, PASSWORD TEXT NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE TRAVELDESTINATION(ID INTEGER PRIMARY KEY, CITY TEXT, COUNTRY TEXT, CONTINENT TEXT, LONGITUDE DOUBLE, LATITUDE DOUBLE, COST INT, IMG TEXT, DESCRIPTION TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE FAVORITES(FID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT NOT NULL, ID INTEGER NOT NULL, FOREIGN KEY (EMAIL) REFERENCES USER(EMAIL) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (ID) REFERENCES TRAVELDESTINATION(ID))");
        sqLiteDatabase.execSQL("CREATE TABLE FAVCONT(EMAIL TEXT, CONTINENT TEXT, PRIMARY KEY(EMAIL, CONTINENT), FOREIGN KEY (EMAIL) REFERENCES USER(EMAIL) ON UPDATE CASCADE ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertUser(User user) {
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseR.rawQuery("SELECT * FROM USER WHERE EMAIL = \"" + user.getEmail() + "\";", null);
        if (!cursor.moveToFirst()) {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("EMAIL", user.getEmail());
            contentValues.put("FIRSTNAME", user.getFirstName());
            contentValues.put("LASTNAME", user.getLastName());
            contentValues.put("PASSWORD", user.getPassword());
            sqLiteDatabase.insert("USER", null, contentValues);
            return true;
        }
        return false;
    }

    public void insertTravelDestination(TravelDestination td) {
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseR.rawQuery("SELECT * FROM TRAVELDESTINATION WHERE ID = " + td.getId() + ";", null);
        if (!cursor.moveToFirst()) {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", td.getId());
            contentValues.put("CITY", td.getCity());
            contentValues.put("COUNTRY", td.getCountry());
            contentValues.put("CONTINENT", td.getContinent());
            contentValues.put("LONGITUDE", td.getLongitude());
            contentValues.put("LATITUDE", td.getLatitude());
            contentValues.put("COST", td.getCost());
            contentValues.put("IMG", td.getImg());
            contentValues.put("DESCRIPTION", td.getDescription());
            sqLiteDatabase.insert("TRAVELDESTINATION", null, contentValues);
        }
    }

    public Cursor getCities() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM TRAVELDESTINATION;", null);
    }

    public Cursor getFavoriteContinentCities(User user) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT td.* FROM TRAVELDESTINATION td JOIN FAVCONT fc ON LOWER(fc.CONTINENT) = LOWER(td.CONTINENT) " +
                "WHERE fc.EMAIL = \"" + user.getEmail() + "\";", null);
    }

    public int getIDByNameAndCountry(String city, String country) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String findQuery = "SELECT ID FROM TRAVELDESTINATION WHERE CITY = \"" + city + "\" AND COUNTRY = \"" + country + "\";";
        Cursor c = sqLiteDatabase.rawQuery(findQuery, null);
        if (c.moveToNext()){
            return c.getInt(0);
        }
        return -1;
    }

    public Cursor getCitiesSortedByName() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT CITY, COUNTRY FROM TRAVELDESTINATION ORDER BY CITY;", null);
    }

    public void insertFavorites(User user, TravelDestination travelDestination) {
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseR.rawQuery("SELECT * FROM FAVORITE WHERE EMAIL = \"" + user.getEmail() + "\" AND ID = " + travelDestination.getId() + ";", null);
        if (!cursor.moveToFirst()) {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("EMAIL", user.getEmail());
            contentValues.put("ID", travelDestination.getId());
            sqLiteDatabase.insert("FAVORITES", null, contentValues);
        }
    }

    public void insertFavoriteContinents(User user, String continent) {
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseR.rawQuery("SELECT * FROM FAVCONT WHERE EMAIL = \"" + user.getEmail() + "\" AND CONTINENT = \"" + continent + "\";", null);
        if (!cursor.moveToFirst()) {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("EMAIL", user.getEmail());
            contentValues.put("CONTINENT", continent);
            sqLiteDatabase.insert("FAVCONT", null, contentValues);
        }
    }

    public User getUserByEmail(String email){
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        User user = new User();
        Cursor cursor = sqLiteDatabaseR.rawQuery("SELECT * FROM USER WHERE EMAIL = \"" + email + "\";", null);
        if (cursor.moveToFirst()) {
            user.setEmail(cursor.getString(0));
            user.setFirstName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setPassword(cursor.getString(3));
        }
        return user;
    }

    public void insertFavoritesById(User user, int id) {
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseR.rawQuery("SELECT * FROM FAVORITES WHERE EMAIL = \"" + user.getEmail() + "\" AND ID = " + id + ";", null);
        if (!cursor.moveToFirst()) {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("EMAIL", user.getEmail());
            contentValues.put("ID", id);
            sqLiteDatabase.insert("FAVORITES", null, contentValues);
        }
    }

    public Cursor getCitiesByContinent() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT ID, CITY, COUNTRY, IMG, CONTINENT FROM TRAVELDESTINATION ORDER BY CONTINENT;", null);
    }

    public boolean isFavorite(User user, int id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM FAVORITES WHERE EMAIL = \"" + user.getEmail() +"\" AND ID = " + id + ";", null);
        return c.moveToFirst();
    }

    public Cursor getCitiesByCostAscending() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT ID, CITY, COUNTRY, IMG, COST FROM TRAVELDESTINATION ORDER BY COST;", null);
    }

    public Cursor getCitiesByCostDescending() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT CITY, COUNTRY FROM TRAVELDESTINATION ORDER BY COST DESC;", null);
    }

    public Cursor getFavorites(User user){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT C.ID, C.CITY, C.COUNTRY, C.IMG FROM TRAVELDESTINATION C, FAVORITES F WHERE C.ID = F.ID AND F.EMAIL = \"" + user.getEmail() + "\";", null);
    }

    public int getNumberOfCities() {
        int nOfCities = 0;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM TRAVELDESTINATION;", null);
        while (cursor.moveToNext()){
            nOfCities = cursor.getInt(0);
        }
        return nOfCities;
    }

    public int getNumberOfCitiesFromFavoriteContinents(User user) {
        int nOfCities = 0;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM TRAVELDESTINATION td JOIN FAVCONT fc ON LOWER(fc.CONTINENT) = LOWER(td.CONTINENT) " +
                "WHERE fc.EMAIL = \"" + user.getEmail() + "\";", null);
        while (cursor.moveToNext()){
            nOfCities = cursor.getInt(0);
        }
        return nOfCities;
    }

    public boolean isRegistered(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USER WHERE EMAIL = \"" + email + "\";", null);
        return cursor.moveToFirst();
    }

    public boolean correctSignIn(String email, String password){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USER WHERE EMAIL = \"" + email + "\" AND PASSWORD = \"" + password + "\";", null);
        return cursor.moveToFirst();
    }

    public ArrayList<String> getFavoriteContinentByUser(User user) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM FAVCONT WHERE EMAIL = \"" + user.getEmail() + "\";", null);
        ArrayList<String> favs = new ArrayList<>();
        while (cursor.moveToNext()){
            favs.add(cursor.getString(1));
        }
        return favs;
    }

    public void removeFavoritesById(User user, int id) {
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabaseR.rawQuery("SELECT * FROM FAVORITES WHERE EMAIL = \"" + user.getEmail() + "\" AND ID = " + id + ";", null);
        if (cursor.moveToFirst()) {
            sqLiteDatabase.execSQL("DELETE FROM FAVORITES WHERE EMAIL = \"" + user.getEmail() + "\" AND ID = " + id + ";");
         }
    }

    public TravelDestination getTravelDestination(String city, String country) {
        TravelDestination travelDestination = new TravelDestination();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TRAVELDESTINATION WHERE CITY = \"" +
                city + "\" AND COUNTRY = \"" + country + "\";", null);
        if (cursor.moveToNext()){
            travelDestination.setId(cursor.getInt(0));
            travelDestination.setCity(city);
            travelDestination.setCountry(country);
            travelDestination.setContinent(cursor.getString(3));
            travelDestination.setLongitude(cursor.getDouble(4));
            travelDestination.setLatitude(cursor.getDouble(5));
            travelDestination.setCost(cursor.getInt(6));
            travelDestination.setImg(cursor.getString(7));
            travelDestination.setDescription(cursor.getString(8));
        }
        return travelDestination;
    }

    public void editUser(User old, User newUser) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE USER SET EMAIL = ?, FIRSTNAME = ?, LASTNAME = ?, PASSWORD = ? WHERE EMAIL = ?";
        db.execSQL(sql, new String[]{newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(),
                newUser.getPassword(), old.getEmail()});
        db.execSQL("UPDATE FAVORITES SET EMAIL = '" + newUser.getEmail() + "' WHERE EMAIL = '" + old.getEmail() + "';");
    }

    public void removeFavorites(User newUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "EMAIL = ?";
        String[] whereArgs = {newUser.getEmail()};
        int rowsDeleted = db.delete("FAVCONT", whereClause, whereArgs);
    }
}
