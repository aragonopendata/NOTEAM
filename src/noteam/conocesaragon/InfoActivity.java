package noteam.conocesaragon;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import noteam.conocesaragon.db.DatabaseConstants;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends ActionBarActivity implements OnClickListener {

    private EditText editBuscar;
    private ImageButton btnBuscar, btnRandom;
    private TextView txtData;

    private Double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        editBuscar = (EditText) findViewById(R.id.editBuscar);
        txtData = (TextView) findViewById(R.id.txtData);
        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar);
        btnRandom = (ImageButton) findViewById(R.id.btnRandom);

        btnBuscar.setOnClickListener(this);
        btnRandom.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitud = extras.getDouble("latitud", 0d);
            longitud = extras.getDouble("longitud", 0d);

            editBuscar.setText(latitud + "," + longitud);

            new GetAddressTask(this).execute();
        }
    }

    private class GetAddressTask extends AsyncTask<Void, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(mContext, new Locale("es", "es"));
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitud, longitud, 1);

            } catch (IOException e1) {
                e1.printStackTrace();
                return null;

            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
                return null;
            }

            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0).getLocality();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            hideKeyboard();

            editBuscar.setText(result);
            buscaNombre(result);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscar:
                buscaNombre(editBuscar.getText().toString());
                hideKeyboard();
                break;

            case R.id.btnRandom:
                buscaRandom();
                hideKeyboard();
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editBuscar.getWindowToken(), 0);
    }

    private void buscaNombre(String buscar) {
        String[] projection = new String[] { DatabaseConstants.Municipios._ID,
                DatabaseConstants.Municipios.NOMBRE, DatabaseConstants.Municipios.COMARCA,
                DatabaseConstants.Municipios.AREA, DatabaseConstants.Municipios.POB_HOMBRES,
                DatabaseConstants.Municipios.POB_MUJERES, DatabaseConstants.Municipios.ALCALDE };

        String selection = DatabaseConstants.Municipios.NOMBRE + "=?";

        buscar = buscar.substring(0, 1).toUpperCase() + buscar.substring(1).toLowerCase();
        String[] selectionArgs = new String[] { buscar };

        Cursor cursor = getContentResolver().query(DatabaseConstants.CONTENT_URI_MUNICIPIOS,
                projection, selection, selectionArgs, null);

        if ((cursor == null) || (cursor.getCount() == 0)) {
            selection = DatabaseConstants.Municipios.NOMBRE + " like ?";
            selectionArgs = new String[] { "%" + buscar + "%" };

            cursor = getContentResolver().query(DatabaseConstants.CONTENT_URI_MUNICIPIOS,
                    projection, selection, selectionArgs, null);

            if ((cursor == null) || (cursor.getCount() == 0)) {
                txtData.setText("No se han encontrado resultados para el texto buscado. Revisa que lo hayas escrito bien y vuelve a probar.");

            } else {
                cursor.moveToFirst();

                String nombre = cursor.getString(cursor
                        .getColumnIndex(DatabaseConstants.Municipios.NOMBRE));
                String comarca = cursor.getString(cursor
                        .getColumnIndex(DatabaseConstants.Municipios.COMARCA));
                Double area = cursor.getDouble(cursor
                        .getColumnIndex(DatabaseConstants.Municipios.AREA));
                Integer pobHombres = cursor.getInt(cursor
                        .getColumnIndex(DatabaseConstants.Municipios.POB_HOMBRES));
                Integer pobMmujeres = cursor.getInt(cursor
                        .getColumnIndex(DatabaseConstants.Municipios.POB_MUJERES));
                String alcalde = cursor.getString(cursor
                        .getColumnIndex(DatabaseConstants.Municipios.ALCALDE));

                String data = "<h3>Nombre municipio: " + nombre + "</h3><br><br>" +
                        "<b>Comarca:</b> " + comarca + "<br><br>" +
                        "<b>Área:</b> " + area + " km<sup>2</sup><br><br>" +
                        "<b>Poblacion:</b> " + (pobHombres + pobMmujeres) + "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;Hombres: " + pobHombres + "<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;Mujeres: " + pobMmujeres + "<br><br>" +
                        "<b>Densidad:</b> "
                        + String.format("%.2f", ((pobHombres + pobMmujeres) / area))
                        + " hab/km<sup>2</sup><br><br>" + "<b>Alcalde:</b> " + alcalde;

                txtData.setText(Html.fromHtml(data));
                editBuscar.setText(nombre);
            }

        } else {
            cursor.moveToFirst();

            String nombre = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.NOMBRE));
            String comarca = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.COMARCA));
            Double area = cursor.getDouble(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.AREA));
            Integer pobHombres = cursor.getInt(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.POB_HOMBRES));
            Integer pobMmujeres = cursor.getInt(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.POB_MUJERES));
            String alcalde = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.ALCALDE));

            String data = "<h3>Nombre municipio: " + nombre + "</h3><br><br>" +
                    "<b>Comarca:</b> " + comarca + "<br><br>" +
                    "<b>Área:</b> " + area + " km<sup>2</sup><br><br>" +
                    "<b>Poblacion:</b> " + (pobHombres + pobMmujeres) + "<br>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;Hombres: " + pobHombres + "<br>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;Mujeres: " + pobMmujeres + "<br><br>" +
                    "<b>Densidad:</b> "
                    + String.format("%.2f", ((pobHombres + pobMmujeres) / area))
                    + " hab/km<sup>2</sup><br><br>" + "<b>Alcalde:</b> " + alcalde;

            txtData.setText(Html.fromHtml(data));
            editBuscar.setText(nombre);
        }

    }

    private void buscaRandom() {
        String[] projection = new String[] { DatabaseConstants.Municipios._ID,
                DatabaseConstants.Municipios.NOMBRE, DatabaseConstants.Municipios.COMARCA,
                DatabaseConstants.Municipios.AREA, DatabaseConstants.Municipios.POB_HOMBRES,
                DatabaseConstants.Municipios.POB_MUJERES, DatabaseConstants.Municipios.ALCALDE };

        Cursor cursor = getContentResolver().query(DatabaseConstants.CONTENT_URI_MUNICIPIOS,
                projection, null, null, "RANDOM()");

        if ((cursor == null) || (cursor.getCount() == 0)) {
            txtData.setText("No se han encontrado resultados para el texto buscado. Revisa que lo hayas escrito bien y vuelve a probar.");

        } else {
            cursor.moveToFirst();

            String nombre = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.NOMBRE));
            String comarca = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.COMARCA));
            Double area = cursor.getDouble(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.AREA));
            Integer pobHombres = cursor.getInt(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.POB_HOMBRES));
            Integer pobMmujeres = cursor.getInt(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.POB_MUJERES));
            String alcalde = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.ALCALDE));

            String data = "<h3>Nombre municipio: " + nombre + "</h3><br><br>" +
                    "<b>Comarca:</b> " + comarca + "<br><br>" +
                    "<b>Área:</b> " + area + " km<sup>2</sup><br><br>" +
                    "<b>Poblacion:</b> " + (pobHombres + pobMmujeres) + "<br>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;Hombres: " + pobHombres + "<br>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;Mujeres: " + pobMmujeres + "<br><br>" +
                    "<b>Densidad:</b> "
                    + String.format("%.2f", ((pobHombres + pobMmujeres) / area))
                    + " hab/km<sup>2</sup><br><br>" + "<b>Alcalde:</b> " + alcalde;

            txtData.setText(Html.fromHtml(data));
            editBuscar.setText(nombre);
        }
    }
}
