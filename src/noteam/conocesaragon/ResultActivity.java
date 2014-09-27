package noteam.conocesaragon;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity implements OnClickListener {

    private TextView txtTitulo, txtTexto;
    private ImageView imgIcon;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFinishOnTouchOutside(false);
        }

        txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTexto = (TextView) findViewById(R.id.txtTexto);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        int numPregunta = extras.getInt("pregunta", 0);
        if (numPregunta == 9) {
            btnNext.setText("Finalizar partida");
        } else {
            btnNext.setText("Siguiente pregunta");
        }

        if (extras.getBoolean("ganador", false)) {
            imgIcon.setImageResource(R.drawable.ic_acierto);
            txtTitulo.setText("¡Enhorabuena!");
            txtTitulo.setTextColor(Color.GREEN);
            txtTexto.setText("Has acertado la pregunta. Sigue así.");

        } else {
            imgIcon.setImageResource(R.drawable.ic_error);
            txtTitulo.setText("¡Has fallado!");
            txtTitulo.setTextColor(Color.RED);

            if (extras.getBoolean("por_tiempo", false)) {
                txtTexto.setText("Has agotado todo el tiempo disponible. Sé más rápido la próxima vez.");
            } else {
                txtTexto.setText("La respuesta marcada no es la correcta. La próxima vez seguro que aciertas.");
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        setResult(RESULT_OK);
        finish();
    }

}
