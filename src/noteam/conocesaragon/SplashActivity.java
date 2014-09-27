package noteam.conocesaragon;

import noteam.conocesaragon.db.DatabaseConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.TextView;

public class SplashActivity extends ActionBarActivity {

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

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean("first-run", true)) {
            Editor edit = sharedPref.edit();
            edit.putBoolean("first-run", false);
            edit.apply();

            new SyncAragopedia().execute();

        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            launchMainActivity();
                        }
                    });
                }
            }).start();
        }

    }

    private class SyncAragopedia extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // COMUNIDADES
            String nextpage = "http://opendata.aragon.es/recurso/territorio/ComunidadAutonoma?_sort=label&_page=0&_pageSize=50";
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
                        Thread.sleep(20);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Comunidades.NOMBRE, nombre);
                        values.put(DatabaseConstants.Comunidades.PAIS, "España");

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_COMUNIDADES,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // PROVINCIAS
            nextpage = "http://opendata.aragon.es/recurso/territorio/Provincia?_sort=label&_page=0&_pageSize=50";
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
                        Thread.sleep(20);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Provincias.NOMBRE, nombre);
                        values.put(DatabaseConstants.Provincias.COMUNIDAD, "Aragón");

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_PROVINCIAS,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // COMARCAS
            nextpage = "http://opendata.aragon.es/recurso/territorio/Comarca?_sort=label&_page=0&_pageSize=50";
            while (!TextUtils.isEmpty(nextpage)) {
                String response = Utils.getResponse(nextpage + "&api_key=" + Utils.API_KEY);
                try {
                    JSONObject result = new JSONObject(response).getJSONObject("result");
                    nextpage = result.optString("next");

                    JSONArray items = result.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String nombre = item.getString("label");
                        String provincia = item.optString("provincia");

                        publishProgress(nombre);
                        Thread.sleep(20);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Comarcas.NOMBRE, nombre);
                        values.put(DatabaseConstants.Comarcas.PROVINCIA, provincia);

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_COMARCAS,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // MUNICIPIOS
            nextpage = "http://opendata.aragon.es/recurso/territorio/Municipio?_sort=label&_page=0&_view=ampliada&_pageSize=50";
            while (!TextUtils.isEmpty(nextpage)) {
                String response = Utils.getResponse(nextpage + "&api_key=" + Utils.API_KEY);
                try {
                    JSONObject result = new JSONObject(response).getJSONObject("result");
                    nextpage = result.optString("next");

                    JSONArray items = result.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String nombre = item.getString("label");
                        String comarca = item.optString("comarca");
                        Double area = item.optDouble("areaTotal");
                        Integer pobHombres = item.optInt("hombres");
                        Integer pobMujeres = item.optInt("mujeres");
                        String alcalde = toProperCase(item.optString("alcalde"));
                        if (!TextUtils.isEmpty(comarca)) {
                            comarca = comarca.substring(comarca.lastIndexOf("/") + 1);
                            comarca = comarca.replace("_", " ");
                        }

                        publishProgress(nombre);
                        Thread.sleep(20);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseConstants.Municipios.NOMBRE, nombre);
                        values.put(DatabaseConstants.Municipios.COMARCA, comarca);
                        values.put(DatabaseConstants.Municipios.AREA, area);
                        values.put(DatabaseConstants.Municipios.POB_HOMBRES, pobHombres);
                        values.put(DatabaseConstants.Municipios.POB_MUJERES, pobMujeres);
                        values.put(DatabaseConstants.Municipios.ALCALDE, alcalde);

                        getContentResolver().insert(DatabaseConstants.CONTENT_URI_MUNICIPIOS,
                                values);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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

            SharedPreferences sharedPref = PreferenceManager
                    .getDefaultSharedPreferences(SplashActivity.this);
            Editor edit = sharedPref.edit();
            edit.putBoolean("first-run", false);
            edit.apply();

            launchMainActivity();
        }

        private String toProperCase(String text) {
            if (TextUtils.isEmpty(text)) {
                return text;
            }

            String[] words = text.split("[.\\s]+");

            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].substring(0, 1).toUpperCase()
                        + words[i].substring(1).toLowerCase();
            }

            return TextUtils.join(" ", words);
        }
    }

    private void launchMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
