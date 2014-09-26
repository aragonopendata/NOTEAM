package noteam.conocesaragon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseConstants.Municipios.TABLE_NAME + " ("
                + DatabaseConstants.Municipios._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseConstants.Municipios.NOMBRE + " TEXT, "
                + DatabaseConstants.Municipios.COMARCA + " TEXT, "
                + DatabaseConstants.Municipios.AREA + " REAL, "
                + DatabaseConstants.Municipios.POB_HOMBRES + " INTEGER, "
                + DatabaseConstants.Municipios.POB_MUJERES + " INTEGER, "
                + DatabaseConstants.Municipios.ALCALDE + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseConstants.Comarcas.TABLE_NAME + " ("
                + DatabaseConstants.Comarcas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseConstants.Comarcas.NOMBRE + " TEXT, "
                + DatabaseConstants.Comarcas.PROVINCIA + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseConstants.Provincias.TABLE_NAME + " ("
                + DatabaseConstants.Provincias._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseConstants.Provincias.NOMBRE + " TEXT, "
                + DatabaseConstants.Provincias.COMUNIDAD + " TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseConstants.Comunidades.TABLE_NAME + " ("
                + DatabaseConstants.Comunidades._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseConstants.Comunidades.NOMBRE + " TEXT, "
                + DatabaseConstants.Comunidades.PAIS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }

        db.execSQL("DROP TABLE " + DatabaseConstants.Municipios.TABLE_NAME);
        db.execSQL("DROP TABLE " + DatabaseConstants.Comarcas.TABLE_NAME);
        db.execSQL("DROP TABLE " + DatabaseConstants.Provincias.TABLE_NAME);
        db.execSQL("DROP TABLE " + DatabaseConstants.Comunidades.TABLE_NAME);

        onCreate(db);
    }

}
