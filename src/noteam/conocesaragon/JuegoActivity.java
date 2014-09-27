package noteam.conocesaragon;

import java.util.ArrayList;
import java.util.Random;

import noteam.conocesaragon.db.DatabaseConstants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class JuegoActivity extends ActionBarActivity implements OnClickListener {

    private TextView txtPregunta, txtTiempo, txtPuntos, txtNumPregunta;
    private Button btn1, btn2, btn3, btn4;
    private ArrayList<Button> buttons = new ArrayList<Button>();

    private Random rand = new Random();

    private Temporizador tempo = new Temporizador(10000, 20);

    private SharedPreferences sharedPrefs;
    private int numPregunta, puntos, milis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        txtPregunta = (TextView) findViewById(R.id.txtPregunta);
        txtTiempo = (TextView) findViewById(R.id.txtTiempo);
        txtPuntos = (TextView) findViewById(R.id.txtPuntos);
        txtNumPregunta = (TextView) findViewById(R.id.txtNumPregunta);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        reordenaButtons();
        generaPregunta();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle extras = getIntent().getExtras();
        if ((extras == null) || (!extras.getBoolean("new", false))) {
            puntos = sharedPrefs.getInt("puntos", 0);
            numPregunta = sharedPrefs.getInt("pregunta", 0);

        } else {
            puntos = 0;
            numPregunta = 0;
        }

        txtNumPregunta.setText((numPregunta + 1) + "/10");
        txtPuntos.setText("Puntos: " + puntos);
    }

    private void reordenaButtons() {
        ArrayList<Integer> seleccionados = new ArrayList<Integer>();

        do {
            int r = rand.nextInt(4);
            if (!seleccionados.contains(r)) {
                seleccionados.add(r);
                int btnId = getResources().getIdentifier("btn" + (r + 1), "id", getPackageName());
                buttons.add((Button) findViewById(btnId));
            }
        } while (seleccionados.size() < 4);
    }

    private void generaPregunta() {
        String[] projection = new String[] { DatabaseConstants.Municipios._ID,
                DatabaseConstants.Municipios.NOMBRE, DatabaseConstants.Municipios.COMARCA };

        Cursor cursor = getContentResolver().query(DatabaseConstants.CONTENT_URI_MUNICIPIOS,
                projection, null, null, "RANDOM()");

        int tipo = rand.nextInt(5);
        int position = 0;
        while ((cursor.moveToNext()) & (position < 4)) {
            String nombre = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.NOMBRE));
            String comarca = cursor.getString(cursor
                    .getColumnIndex(DatabaseConstants.Municipios.COMARCA));

            if (tipo < 4) {
                if (position == 0) {
                    txtPregunta.setText("¿A qué comarca pertenece el municipio de " + nombre + "?");
                }

                boolean repetido = false;
                for (int i = 0; i < position; i++) {
                    if (buttons.get(i).getText().toString().equalsIgnoreCase(comarca)) {
                        repetido = true;
                        break;
                    }
                }

                if (repetido) {
                    continue;
                }

                buttons.get(position).setText(comarca);
                position++;

            } else {
                if (position == 0) {
                    txtPregunta
                            .setText("¿Cuál de los siguientes municipios pertenece a la comarca "
                                    + comarca + "?");
                }

                boolean repetido = false;
                for (int i = 0; i < position; i++) {
                    if (buttons.get(i).getText().toString().equalsIgnoreCase(nombre)) {
                        repetido = true;
                        break;
                    }
                }

                if (repetido) {
                    continue;
                }

                buttons.get(position).setText(nombre);
                position++;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(JuegoActivity.this, ResultActivity.class);
        intent.putExtra("pregunta", numPregunta);

        if (v.equals(buttons.get(0))) {
            ((Button) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_acierto, 0, 0, 0);
            intent.putExtra("ganador", true);

        } else {
            ((Button) v).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
            intent.putExtra("ganador", false);
            intent.putExtra("por_tiempo", false);
            milis = 0;
        }

        tempo.cancel();
        removeListenerButtons();

        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        puntos += milis;
        numPregunta++;

        Editor edit = sharedPrefs.edit();
        edit.putInt("puntos", puntos);
        edit.putInt("pregunta", numPregunta);
        edit.apply();

        if (numPregunta < 10) {
            Intent intent = new Intent(this, JuegoActivity.class);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(this, FinalActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private class Temporizador extends CountDownTimer {
        public Temporizador(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            txtTiempo.setText(String.format("%.2f", (millisUntilFinished / 1000d)) + " segundos");
            String milistemp = String.format("%.2f", (millisUntilFinished / 1000d))
                    .replace(".", "").replace(",", "");
            milis = Integer.valueOf(milistemp);
        }

        @Override
        public void onFinish() {
            txtTiempo.setText("Fin del tiempo");
            milis = 0;
            removeListenerButtons();

            Intent intent = new Intent(JuegoActivity.this, ResultActivity.class);
            intent.putExtra("pregunta", numPregunta);
            intent.putExtra("ganador", false);
            intent.putExtra("por_tiempo", true);

            startActivityForResult(intent, 1000);
        }
    }

    private void removeListenerButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        tempo.start();
    }

    @Override
    protected void onStop() {
        tempo.cancel();

        super.onStop();
    }
}
