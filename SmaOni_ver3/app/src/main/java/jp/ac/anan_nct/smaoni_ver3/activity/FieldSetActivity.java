package jp.ac.anan_nct.smaoni_ver3.activity;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import jp.ac.anan_nct.smaoni_ver3.model.Field;


public class FieldSetActivity extends GPSActivity {

    Button fieldSet, fieldTypeSet;

    boolean setting;

    ArrayList<Location> points;
    Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_set);

        points = new ArrayList<Location>();
        setting = false;
        field = new Field();

        fieldSet = (Button)findViewById(R.id.fieldSet);
        fieldTypeSet = (Button)findViewById(R.id.fieldTypeSet);
        fieldTypeSet.setText(Integer.toString(field.getFieldType()));

        setAction();
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        if(setting){
            field.addPoints(location);
        }
    }

    public void setAction(){
        fieldTypeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field.setFieldType((field.getFieldType() == 0) ? 1 : 0);
                fieldTypeSet.setText(Integer.toString(field.getFieldType()));
            }
        });
        fieldSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!setting){
                    setting = true;
                    fieldSet.setText("finish setting");
                }else{
                    setting = false;
                    fieldSet.setText("field has been set");
                    field.setField(field.getPoints());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_field_set, menu);
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
