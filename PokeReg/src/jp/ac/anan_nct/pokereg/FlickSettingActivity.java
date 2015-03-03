package jp.ac.anan_nct.pokereg;

import jp.ac.anan_nct.pokereg.config.Configuration;
import jp.ac.anan_nct.pokereg.entity.Data;
import jp.ac.anan_nct.pokereg.entity.Flick;
import jp.ac.anan_nct.pokereg.view.Flickable10Key;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FlickSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flick_setting);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		this.createList();
	}
	
	private void createList(){
		LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayoutForSettingRow);
		layout.removeAllViews();
		int index = 0;
		for(Flick f : Data.getInstance().getFlickSet()){
			layout.addView(this.createNewRow(f, index++));
		}
	}
	
	private View createNewRow(Flick f, int index){
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.flick_setting_row, null);
		Flickable10Key fk = (Flickable10Key)view.findViewById(R.id.flickable10Key);
		fk.setKeySetPattern(Configuration.getInstance().getKeySetPattern());
		fk.setAcceptInput(false);
		fk.setFlicks(f.getKeys(Configuration.getInstance().getKeySetPattern()));
		TextView an = (TextView)view.findViewById(R.id.actionName);
		an.setText(f.getCaption());
		TextView path = (TextView)view.findViewById(R.id.pathText);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(Key k : f.getKeys(Configuration.getInstance().getKeySetPattern())){
			if(i>0) sb.append("→");
			sb.append(k.toString());
			i++;
		}
		path.setText(sb.toString());
		this.registerForContextMenu(view);
		view.setTag(index);
		return view;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderIcon(getResources().getDrawable(R.drawable._ic_launcher));
        menu.add(0, (Integer)view.getTag(), 0, "削除");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		android.util.Log.i("ff", String.valueOf(item.getItemId()));
		Data.getInstance().getFlickSet().remove(item.getItemId());
		this.createList();
		Toast.makeText(this, "フリックを削除しました", Toast.LENGTH_LONG).show();
		return true;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.flick_setting, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addNewFlick) {
        	Intent addnew = new Intent(this, AddNewFlickActivity.class);
        	startActivity(addnew);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
