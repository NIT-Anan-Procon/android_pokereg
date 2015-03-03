package jp.ac.anan_nct.pokereg;

import jp.ac.anan_nct.pokereg.R.id;
import jp.ac.anan_nct.pokereg.entity.Category;
import jp.ac.anan_nct.pokereg.entity.CategorySet;
import jp.ac.anan_nct.pokereg.entity.Data;
import jp.ac.anan_nct.pokereg.entity.EntitiesObserver;
import jp.ac.anan_nct.pokereg.entity.Flick;
import jp.ac.anan_nct.pokereg.entity.Entity;
import jp.ac.anan_nct.pokereg.entity.FlickSet;
import jp.ac.anan_nct.pokereg.flick.FlickAction;
import jp.ac.anan_nct.pokereg.view.Flickable10Key;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewFlickActivity extends Activity
		implements EntitiesObserver {

	private FlickAction action;
	private int arg;
	private int[] flicks;
	private FlickSet flickSet = Data.getInstance().getFlickSet();
	private CategorySet categorySet = Data.getInstance().getCategories();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_flick);

		Flickable10Key fk = (Flickable10Key)findViewById(R.id.flickable10Key);
		fk.setKeepPath(true);
		fk.setEventListner(new Flickable10Key.Flickable10KeyEventListener() {
			@Override
			public void onTouch(Key key) { }
			@Override
			public void onFlick(int[] sequence) {
				flicks = sequence;
			}
		});
		
		Button addNewButton = (Button)findViewById(R.id.addNewButton);
		addNewButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(flicks != null && flicks.length >= 2){
					if(flickSet.find(flicks) != null){
						Toast.makeText(AddNewFlickActivity.this,
								"既にそのフリックは使用されています",
								Toast.LENGTH_LONG).show();
						return;
					}
					Flick f = new Flick(flicks, action.getId(), arg);
					flickSet.add(f);
					Toast.makeText(AddNewFlickActivity.this,
							"新しいフリックを追加しました",
							Toast.LENGTH_LONG).show();
					finish();
				}else{
					Toast.makeText(AddNewFlickActivity.this,
							"2つ以上のキーを使用するフリックを設定してください",
							Toast.LENGTH_LONG).show();
				}
			}});
		
		this.createSpinner();
	}
	
	private void createSpinner(){
		 ArrayAdapter<FlickAction> adapter = new ArrayAdapter<FlickAction>(this, android.R.layout.simple_spinner_dropdown_item);
		 for(FlickAction fa : FlickAction.values()){
			 adapter.add(fa);
		 }
	     Spinner spinner = (Spinner) findViewById(id.spinner1);
	     spinner.setAdapter(adapter);
	     spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Spinner spinner = (Spinner) arg0;
                FlickAction item = (FlickAction) spinner.getSelectedItem();
                action = item;
                LinearLayout layout = (LinearLayout)findViewById(R.id.argLayout);
                layout.removeAllViews();
                if(item.isUseArg()){
                	switch(item.getType()){
                	case Num:
                		createForNumArg(item, layout); break;
                	case Category:
                		createForCategoryArg(item, layout); break;
                	}
                }else{
                	createForNothingArg(item, layout);
                }
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}});
	}
	private void createForNothingArg(FlickAction item, LinearLayout layout){
    	TextView noarg = new TextView(AddNewFlickActivity.this);
    	noarg.setText("設定可能な引数なし");
    	arg = 0;
    	layout.addView(noarg);
	}
	private void createForNumArg(FlickAction item, LinearLayout layout){
    	TextView ct = new TextView(AddNewFlickActivity.this);
    	ct.setText(item.getArgCaption());
    	layout.addView(ct);
    	NumberPicker picker = new NumberPicker(this);
    	arg = item.getMin();
    	picker.setMinValue(item.getMin());
    	picker.setMaxValue(item.getMax());
    	picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				arg = newVal;
			}
		});
    	layout.addView(picker);
    }
	private void createForCategoryArg(FlickAction item , LinearLayout layout){
    	TextView ct = new TextView(AddNewFlickActivity.this);
    	ct.setText(item.getArgCaption());
    	layout.addView(ct);
    	arg = -1;
    	Spinner spinner = new Spinner(this);
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        
        for(Category c : this.categorySet){
        	adapter.add(c);
        }
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Entity<Category> c = categorySet.getByIndex(position);
				arg = c.getId();
			}
		});
        layout.addView(spinner);
	}

	@Override
	public void updatedEntities() {
		// TODO Auto-generated method stub
		
	}
}
