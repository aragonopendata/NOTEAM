package noteam.conocesaragon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends ActionBarActivity implements OnClickListener {

    private TextView txtPuntos;
    private Button btnCompartir, btnNuevo, btnMenu;

    private int puntos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        txtPuntos = (TextView) findViewById(R.id.txtPuntuacion);
        btnCompartir = (Button) findViewById(R.id.btnCompartir);
        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnNuevo = (Button) findViewById(R.id.btnNueva);

        btnCompartir.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnNuevo.setOnClickListener(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        puntos = sharedPref.getInt("puntos", 0);

        txtPuntos.setText("Has obtenido " + puntos + " puntos");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCompartir:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "He conseguido una puntuación de " + puntos
                        + " puntos en el juego 'Conoces Aragón?' #ConocesAragon #Jacathon");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Selecciona aplicacion"));

                break;

            case R.id.btnMenu:
                finish();
                break;

            case R.id.btnNueva:
                Intent juego = new Intent(this, JuegoActivity.class);
                juego.putExtra("new", true);
                startActivity(juego);
                finish();
                break;
        }
    }

}
