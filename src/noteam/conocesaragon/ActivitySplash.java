package noteam.conocesaragon;

import noteam.conocesaragon.db.DatabaseConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.TextView;

public class ActivitySplash extends ActionBarActivity {

    private TextView txtLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtLoading = (TextView) findViewById(R.id.txtLoading);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new SyncAragopedia().execute();
    }

    private class SyncAragopedia extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // COMUNIDADES
            String nextpage = "http://opendata.aragon.es/recurso/territorio/ComunidadAutonoma?_sort=label&_page=0";
            while (!TextUtils.isEmpty(nextpage)) {
                String response = Utils.getResponse(nextpage + "&api_key=" + Utils.API_KEY);
                try {
                    JSONObject result = new JSONObject(response).getJSONObject("result");
                    nextpage = result.optString("next");

                    JSONArray items = result.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String nombre = item.getString("label");
                        publishProgress(nombre);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Comunidades.NOMBRE, nombre);
                        values.put(DatabaseConstants.Comunidades.PAIS, "España");

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_COMUNIDADES,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // PROVINCIAS
            nextpage = "http://opendata.aragon.es/recurso/territorio/Provincia?_sort=label&_page=0";
            while (!TextUtils.isEmpty(nextpage)) {
                String response = Utils.getResponse(nextpage + "&api_key=" + Utils.API_KEY);
                try {
                    JSONObject result = new JSONObject(response).getJSONObject("result");
                    nextpage = result.optString("next");

                    JSONArray items = result.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String nombre = item.getString("label");
                        publishProgress(nombre);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Provincias.NOMBRE, nombre);
                        values.put(DatabaseConstants.Provincias.COMUNIDAD, "Aragón");

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_PROVINCIAS,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // COMARCAS
            nextpage = "http://opendata.aragon.es/recurso/territorio/Comarca?_sort=label&_page=0";
            while (!TextUtils.isEmpty(nextpage)) {
                String response = Utils.getResponse(nextpage + "&api_key=" + Utils.API_KEY);
                try {
                    JSONObject result = new JSONObject(response).getJSONObject("result");
                    nextpage = result.optString("next");

                    JSONArray items = result.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String nombre = item.getString("label");
                        // String provincia = item.getString("provincia");
                        publishProgress(nombre);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Comarcas.NOMBRE, nombre);
                        values.put(DatabaseConstants.Comarcas.PROVINCIA, "");

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_COMARCAS,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // MUNICIPIOS
            nextpage = "http://opendata.aragon.es/recurso/territorio/Municipio?_sort=label&_page=0";
            while (!TextUtils.isEmpty(nextpage)) {
                String response = Utils.getResponse(nextpage + "&api_key=" + Utils.API_KEY);
                try {
                    JSONObject result = new JSONObject(response).getJSONObject("result");
                    nextpage = result.optString("next");

                    JSONArray items = result.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String nombre = item.getString("label");
                        String comarca = item.getString("comarca");
                        comarca = comarca.substring(comarca.lastIndexOf("/"));
                        publishProgress(nombre);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Municipios.NOMBRE, nombre);
                        values.put(DatabaseConstants.Municipios.COMARCA, comarca);

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_MUNICIPIOS,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            txtLoading.setText("Descargando: " + values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
