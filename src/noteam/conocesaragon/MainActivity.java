package noteam.conocesaragon;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, OnClickListener {

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Location location;

    private Button btnJugar, btnDescubre, btnDonde;

    private float distancia = 5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJugar = (Button) findViewById(R.id.btnJugar);
        btnDescubre = (Button) findViewById(R.id.btnDescubre);
        btnDonde = (Button) findViewById(R.id.btnDonde);

        btnJugar.setOnClickListener(this);
        btnDescubre.setOnClickListener(this);
        btnDonde.setOnClickListener(this);

        mLocationClient = new LocationClient(this, this, this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnected(Bundle arg0) {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() < distancia) {
            this.location = location;
            btnDonde.setEnabled(true);
            btnDonde.setText("Dónde estoy?");
            mLocationClient.removeLocationUpdates(this);

        } else {
            distancia *= 2;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDescubre:
                startActivity(new Intent(this, InfoActivity.class));
                break;

            case R.id.btnDonde:
                Intent intent = new Intent(this, InfoActivity.class);
                intent.putExtra("latitud", location.getLatitude());
                intent.putExtra("longitud", location.getLongitude());
                startActivity(intent);
                break;

            case R.id.btnJugar:

                break;

        }
    }

}
