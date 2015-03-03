package jp.ac.anan_nct.smaoni_ver3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.ac.anan_nct.smaoni_ver3.model.GameData;


public class OptionActivity extends ActionBarActivity {

    Button gridPlus, gridMinus, gameStart;
    TextView gridNum;
    public static GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        gridPlus = (Button)findViewById(R.id.gridPlus);
        gridMinus = (Button)findViewById(R.id.gridMinus);
        gameStart = (Button)findViewById(R.id.gameStart);
        gridNum = (TextView)findViewById(R.id.gridNum);
        gameData = new GameData();

        gridNum.setText(Integer.toString(gameData.gridNum));

        setAction();
    }

    public void setAction(){

        gridPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(gameData.gridNum < 12)
                    gridNum.setText(Integer.toString(++gameData.gridNum));
            }
        });
        gridMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameData.gridNum > 4)
                    gridNum.setText(Integer.toString(--gameData.gridNum));
            }
        });
        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionActivity.this, GameActivity.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
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
