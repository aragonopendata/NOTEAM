package noteam.conocesaragon.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DatabaseProvider extends ContentProvider {

    private static final int MUNICIPIOS = 1;
    private static final int MUNICIPIOS_ID = 2;
    private static final int COMARCAS = 3;
    private static final int COMARCAS_ID = 4;
    private static final int PROVINCIAS = 5;
    private static final int PROVINCIAS_ID = 6;
    private static final int COMUNIDADES = 7;
    private static final int COMUNIDADES_ID = 8;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME, DatabaseConstants.Municipios.TABLE_NAME,
                MUNICIPIOS);
        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME, DatabaseConstants.Municipios.TABLE_NAME
                + "/#", MUNICIPIOS_ID);

        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME, DatabaseConstants.Comarcas.TABLE_NAME,
                COMARCAS);
        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME, DatabaseConstants.Comarcas.TABLE_NAME
                + "/#", COMARCAS_ID);

        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME, DatabaseConstants.Provincias.TABLE_NAME,
                PROVINCIAS);
        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME, DatabaseConstants.Provincias.TABLE_NAME
                + "/#", PROVINCIAS_ID);

        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME,
                DatabaseConstants.Comunidades.TABLE_NAME, COMUNIDADES);
        uriMatcher.addURI(DatabaseConstants.PROVIDER_NAME, DatabaseConstants.Comunidades.TABLE_NAME
                + "/#", COMUNIDADES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return (database == null) ? false : true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MUNICIPIOS:
                return "vnd.android.cursor.dir/vnd.noteam."
                        + DatabaseConstants.Municipios.TABLE_NAME;
            case MUNICIPIOS_ID:
                return "vnd.android.cursor.item/vnd.noteam."
                        + DatabaseConstants.Municipios.TABLE_NAME;

            case COMARCAS:
                return "vnd.android.cursor.dir/vnd.noteam."
                        + DatabaseConstants.Comarcas.TABLE_NAME;
            case COMARCAS_ID:
                return "vnd.android.cursor.item/vnd.noteam."
                        + DatabaseConstants.Comarcas.TABLE_NAME;

            case PROVINCIAS:
                return "vnd.android.cursor.dir/vnd.noteam."
                        + DatabaseConstants.Provincias.TABLE_NAME;
            case PROVINCIAS_ID:
                return "vnd.android.cursor.item/vnd.noteam."
                        + DatabaseConstants.Provincias.TABLE_NAME;

            case COMUNIDADES:
                return "vnd.android.cursor.dir/vnd.noteam."
                        + DatabaseConstants.Comunidades.TABLE_NAME;
            case COMUNIDADES_ID:
                return "vnd.android.cursor.item/vnd.noteam."
                        + DatabaseConstants.Comunidades.TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = -1;
        switch (uriMatcher.match(uri)) {
            case MUNICIPIOS:
                rowID = database.insert(DatabaseConstants.Municipios.TABLE_NAME, null, values);

                if (rowID > 0) {
                    uri = ContentUris.withAppendedId(DatabaseConstants.CONTENT_URI_MUNICIPIOS,
                            rowID);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;

            case COMARCAS:
                rowID = database.insert(DatabaseConstants.Comarcas.TABLE_NAME, null, values);

                if (rowID > 0) {
                    uri = ContentUris.withAppendedId(DatabaseConstants.CONTENT_URI_COMARCAS, rowID);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;

            case PROVINCIAS:
                rowID = database.insert(DatabaseConstants.Provincias.TABLE_NAME, null, values);

                if (rowID > 0) {
                    uri = ContentUris.withAppendedId(DatabaseConstants.CONTENT_URI_PROVINCIAS,
                            rowID);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;

            case COMUNIDADES:
                rowID = database.insert(DatabaseConstants.Comunidades.TABLE_NAME, null, values);

                if (rowID > 0) {
                    uri = ContentUris.withAppendedId(DatabaseConstants.CONTENT_URI_COMUNIDADES,
                            rowID);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;

            default:
                throw new SQLException("Failed to insert row into " + uri);
        }
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case MUNICIPIOS_ID:
                queryBuilder.appendWhere(DatabaseConstants.Municipios._ID + "="
                        + uri.getLastPathSegment());
            case MUNICIPIOS:
                queryBuilder.setTables(DatabaseConstants.Municipios.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DatabaseConstants.Municipios.SORT_ORDER;
                }
                break;

            case COMARCAS_ID:
                queryBuilder.appendWhere(DatabaseConstants.Comarcas._ID + "="
                        + uri.getLastPathSegment());
            case COMARCAS:
                queryBuilder.setTables(DatabaseConstants.Comarcas.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DatabaseConstants.Comarcas.SORT_ORDER;
                }
                break;

            case PROVINCIAS_ID:
                queryBuilder.appendWhere(DatabaseConstants.Provincias._ID + "="
                        + uri.getLastPathSegment());
            case PROVINCIAS:
                queryBuilder.setTables(DatabaseConstants.Provincias.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DatabaseConstants.Provincias.SORT_ORDER;
                }
                break;

            case COMUNIDADES_ID:
                queryBuilder.appendWhere(DatabaseConstants.Comunidades._ID + "="
                        + uri.getLastPathSegment());
            case COMUNIDADES:
                queryBuilder.setTables(DatabaseConstants.Comunidades.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DatabaseConstants.Comunidades.SORT_ORDER;
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor c = queryBuilder.query(database, projection, selection, selectionArgs, null, null,
                sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;
        String rowId;

        switch (uriMatcher.match(uri)) {
            case MUNICIPIOS:
                rowsUpdated = database.update(DatabaseConstants.Municipios.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case MUNICIPIOS_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(DatabaseConstants.Municipios.TABLE_NAME, values,
                            DatabaseConstants.Municipios._ID + "=" + rowId, null);
                } else {
                    rowsUpdated = database.update(DatabaseConstants.Municipios.TABLE_NAME, values,
                            DatabaseConstants.Municipios._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            case COMARCAS:
                rowsUpdated = database.update(DatabaseConstants.Comarcas.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case COMARCAS_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(DatabaseConstants.Comarcas.TABLE_NAME, values,
                            DatabaseConstants.Comarcas._ID + "=" + rowId, null);
                } else {
                    rowsUpdated = database.update(DatabaseConstants.Comarcas.TABLE_NAME, values,
                            DatabaseConstants.Comarcas._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            case PROVINCIAS:
                rowsUpdated = database.update(DatabaseConstants.Provincias.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case PROVINCIAS_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(DatabaseConstants.Provincias.TABLE_NAME, values,
                            DatabaseConstants.Provincias._ID + "=" + rowId, null);
                } else {
                    rowsUpdated = database.update(DatabaseConstants.Provincias.TABLE_NAME, values,
                            DatabaseConstants.Provincias._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            case COMUNIDADES:
                rowsUpdated = database.update(DatabaseConstants.Comunidades.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case COMUNIDADES_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(DatabaseConstants.Comunidades.TABLE_NAME, values,
                            DatabaseConstants.Comunidades._ID + "=" + rowId, null);
                } else {
                    rowsUpdated = database.update(DatabaseConstants.Comunidades.TABLE_NAME, values,
                            DatabaseConstants.Comunidades._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        String rowId;

        switch (uriMatcher.match(uri)) {
            case MUNICIPIOS:
                rowsDeleted = database.delete(DatabaseConstants.Municipios.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case MUNICIPIOS_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(DatabaseConstants.Municipios.TABLE_NAME,
                            DatabaseConstants.Municipios._ID + "=" + rowId, null);
                } else {
                    rowsDeleted = database.delete(DatabaseConstants.Municipios.TABLE_NAME,
                            DatabaseConstants.Municipios._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            case COMARCAS:
                rowsDeleted = database.delete(DatabaseConstants.Comarcas.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case COMARCAS_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(DatabaseConstants.Comarcas.TABLE_NAME,
                            DatabaseConstants.Comarcas._ID + "=" + rowId, null);
                } else {
                    rowsDeleted = database.delete(DatabaseConstants.Comarcas.TABLE_NAME,
                            DatabaseConstants.Comarcas._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            case PROVINCIAS:
                rowsDeleted = database.delete(DatabaseConstants.Provincias.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case PROVINCIAS_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(DatabaseConstants.Provincias.TABLE_NAME,
                            DatabaseConstants.Provincias._ID + "=" + rowId, null);
                } else {
                    rowsDeleted = database.delete(DatabaseConstants.Provincias.TABLE_NAME,
                            DatabaseConstants.Provincias._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            case COMUNIDADES:
                rowsDeleted = database.delete(DatabaseConstants.Comunidades.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case COMUNIDADES_ID:
                rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(DatabaseConstants.Comunidades.TABLE_NAME,
                            DatabaseConstants.Comunidades._ID + "=" + rowId, null);
                } else {
                    rowsDeleted = database.delete(DatabaseConstants.Comunidades.TABLE_NAME,
                            DatabaseConstants.Comunidades._ID + "=" + rowId + " and " + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

}
