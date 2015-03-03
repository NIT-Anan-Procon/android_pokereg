package jp.ac.anan_nct.smaoni_ver3.activity;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class GameActivity extends GPSActivity{


    public static TextView indicateXY;
    TextView txLng, txLat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        indicateXY = (TextView)findViewById(R.id.indicateXY);
        indicateXY.setText("0000000");

        txLng = (TextView)findViewById(R.id.txLng);
        txLat = (TextView)findViewById(R.id.txLat);

        setAction();
    }

    public static void setXY(int x, int y){
        indicateXY.setText(x + "   " + y);
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        txLng.setText("longitude:" + location.getLongitude());
        txLat.setText("latitude :" + location.getLatitude());

    }

    void setAction(){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
