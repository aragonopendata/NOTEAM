package noteam.conocesaragon.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseConstants {
    public static final String PROVIDER_NAME = "noteam.conocesaragon.provider";
    public static final Uri CONTENT_URI_MUNICIPIOS = Uri.parse("content://" + PROVIDER_NAME + "/"
            + Municipios.TABLE_NAME);
    public static final Uri CONTENT_URI_COMARCAS = Uri.parse("content://" + PROVIDER_NAME + "/"
            + Comarcas.TABLE_NAME);
    public static final Uri CONTENT_URI_PROVINCIAS = Uri.parse("content://" + PROVIDER_NAME + "/"
            + Provincias.TABLE_NAME);
    public static final Uri CONTENT_URI_COMUNIDADES = Uri.parse("content://" + PROVIDER_NAME + "/"
            + Comunidades.TABLE_NAME);

    public final static String DB_NAME = "conocesaragon.db";
    public final static int DB_VERSION = 1;

    public class Municipios {
        public static final String TABLE_NAME = "municipios";
        public static final String _ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String COMARCA = "comarca";
        public static final String AREA = "area";
        public static final String POB_HOMBRES = "pob_hombres";
        public static final String POB_MUJERES = "pob_mujeres";
        public static final String ALCALDE = "alcalde";
        public final static String SORT_ORDER = NOMBRE + " ASC";
    }

    public class Comarcas {
        public static final String TABLE_NAME = "comarca";
        public static final String _ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String PROVINCIA = "comunidad";
        public final static String SORT_ORDER = NOMBRE + " ASC";
    }

    public class Provincias {
        public static final String TABLE_NAME = "provincias";
        public static final String _ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String COMUNIDAD = "comunidad";
        public final static String SORT_ORDER = NOMBRE + " ASC";
    }

    public class Comunidades {
        public static final String TABLE_NAME = "comunidades";
        public static final String _ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String PAIS = "pais";
        public final static String SORT_ORDER = NOMBRE + " ASC";
    }
}