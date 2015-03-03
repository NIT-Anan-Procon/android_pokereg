package jp.ac.anan_nct.pokereg;

import java.util.LinkedList;

import jp.ac.anan_nct.pokereg.entity.Category;
import jp.ac.anan_nct.pokereg.entity.Data;
import jp.ac.anan_nct.pokereg.entity.Receipt;
import jp.ac.anan_nct.pokereg.entity.ReceiptRow;
import jp.ac.anan_nct.pokereg.view.IconsBarView;
import jp.ac.anan_nct.pokereg.view.IconsBarView.IconMenu;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiptDetailActivity extends Activity {

	private Receipt receipt;
	private LinkedList<CheckBox> cbs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt_detail);
		
		IconsBarView iconsBar = (IconsBarView)this.findViewById(R.id.iconsBarView1);
		iconsBar.setListener(new IconsBarView.OnTouchIconListener(){
			@Override
			public void OnTouchIcon(int index, IconMenu icon) {
			    Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
			    vib.vibrate(100);
				switch(icon.getId()){
				case 0: doCheck(); break;
				case 1: doChangeCategory(); break;
				case 2: doDelete(); break;
				case 3: doCapture(); break;
				case 4: doShowImage(); break;
				case 5: finish(); break;
				}
			}});
		
		this.update();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		this.invalidateOptionsMenu();
	}
	
	private void update(){
		LinearLayout rows = (LinearLayout)this.findViewById(R.id.rows);
		rows.removeAllViews();
		TextView dateTextView = (TextView)this.findViewById(R.id.dateTextView);
		TextView totalTextView = (TextView)this.findViewById(R.id.totalTextView);
		this.receipt = Data.getInstance().getDetailReceipt();
		dateTextView.setText(String.format("%d年%-2d月%-2d日 %-2d時 %-2d分",
				this.receipt.getTime().year,
				this.receipt.getTime().month + 1,
				this.receipt.getTime().monthDay,
				this.receipt.getTime().hour,
				this.receipt.getTime().minute
				));
		totalTextView.setText(String.format("合計 ¥%10d   点数 %d", receipt.getTotal(), receipt.getCount() ));
		
		this.cbs = new LinkedList<CheckBox>();
		for(ReceiptRow row : receipt){
			LinearLayout r = new LinearLayout(this);
			r.setOrientation(LinearLayout.HORIZONTAL);
			CheckBox cb = new CheckBox(this);
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					invalidateOptionsMenu();
				}});
			this.cbs.add(cb);
			r.addView(cb);
			LinearLayout r2 = new LinearLayout(this);
			r2.setOrientation(LinearLayout.VERTICAL);
			TextView label = new TextView(this);
			label.setTextSize(16.0f);
			label.setText(String.format("%-10s ¥%-8d",
					row.getCategory().getCaption(),
					row.getTotalWithinTax() ));
			r2.addView(label);
			TextView price = new TextView(this);
			price.setText(String.format("    ¥%-6d × %-2d",
					row.getPrice(),
					row.getAmount()
					));
			r2.addView(price);
			r.addView(r2);
			rows.addView(r);
		}
		this.invalidateOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receipt_detail, menu);

		IconsBarView iconsBar = (IconsBarView)this.findViewById(R.id.iconsBarView1);
		Resources res = this.getResources();
		iconsBar.clearIconMenus();

		boolean anychecked = false;
		for(int i=0; i<this.receipt.size(); i++){
			anychecked = anychecked || cbs.get(i).isChecked();
		}
		iconsBar.addIconMenus(5, "戻る", BitmapFactory.decodeResource(res, R.drawable.back_key), true);
		if(this.receipt == Data.getInstance().getCurrentReceipt()){
			boolean checkable = true;
			MenuItem item1 = menu.findItem(R.id.check);
			item1.setVisible(true);
			if(this.receipt.size() == 0){
				checkable = false;
				item1.setEnabled(false);
			}
			iconsBar.addIconMenus(0, "精算", BitmapFactory.decodeResource(res, R.drawable.cacher), checkable);
			iconsBar.addIconMenus(1, "カテゴリ変更", BitmapFactory.decodeResource(res, R.drawable.change_category), anychecked);
			iconsBar.addIconMenus(2, "削除", BitmapFactory.decodeResource(res, R.drawable.trash), anychecked);
		}else{
			iconsBar.addIconMenus(1, "カテゴリ変更", BitmapFactory.decodeResource(res, R.drawable.change_category), anychecked);
			iconsBar.addIconMenus(3, "レシート撮影", BitmapFactory.decodeResource(res, R.drawable.camera), true);
			iconsBar.addIconMenus(4, "レシート画像", BitmapFactory.decodeResource(res, R.drawable.show), this.receipt.getImage() != null);
		}
		if(!anychecked){
			MenuItem item1 = menu.findItem(R.id.deleteRows);
			MenuItem item2 = menu.findItem(R.id.changeCategory);
			item1.setEnabled(false);
			item2.setEnabled(false);
		}
		if(this.receipt.getImage() == null){
	        MenuItem item1 = menu.findItem(R.id.showReceiptImage);
	        item1.setVisible(false);
		}
		iconsBar.invalidate();
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.deleteRows) {
			this.doDelete();
			return true;
		}
		if (id == R.id.changeCategory) {
			this.doChangeCategory();
			return true;
		}
		if(id == R.id.captureReceipt){
			this.doCapture();
        	return true;
		}
		if(id == R.id.showReceiptImage){
			this.doShowImage();
        	return true;
		}
		if (id == R.id.check) {
			this.doCheck();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void doCapture(){
		if(this.receipt.getImage() != null){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	        alertDialogBuilder.setTitle("撮影確認");
	        alertDialogBuilder.setMessage("既にレシート画像が撮影されています。新しく撮影すると上書きしますが、よろしいですか？");
	        alertDialogBuilder.setPositiveButton("はい",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	Intent intent = new Intent(ReceiptDetailActivity.this, CaptureReceiptActivity.class);
	                    	startActivity(intent);
	                    }
	                });
	        alertDialogBuilder.setNegativeButton("いいえ", null);
	        alertDialogBuilder.setCancelable(true);
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
		}else{
	    	Intent intent = new Intent(this, CaptureReceiptActivity.class);
	    	startActivity(intent);
		}
	}
	private void doShowImage(){
    	Intent intent = new Intent(this, ShowImageActivity.class);
    	Data.getInstance().setShowImageBitmap(this.receipt.getImage());
    	startActivity(intent);
	}
	private void doCheck(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("精算確認");
        alertDialogBuilder.setMessage("本当に精算しますか？");
        alertDialogBuilder.setPositiveButton("はい",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                		receipt = Data.getInstance().addCurrentReceiptToReceiptSet();
                		Data.getInstance().initCurrentReceipt();
                		Data.getInstance().setDetailReceipt(receipt);
                		invalidateOptionsMenu();
                		Toast.makeText(ReceiptDetailActivity.this, "精算しました", Toast.LENGTH_LONG).show();
                    }
                });
        alertDialogBuilder.setNegativeButton("いいえ", null);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
	private void doChangeCategory(){
		final Spinner spinner = this.getCategoryViews();
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("カテゴリの変更")
            .setView(spinner)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	Category category = (Category)spinner.getSelectedItem();
                	android.util.Log.i("s", category.getCaption());
        			for(int i=receipt.size()-1; i>=0; i--){
        				CheckBox cb = cbs.get(i);
        				if(cb.isChecked()){
        					receipt.getByIndex(i).setCategory(category);
        				}
        			}
        			update();
            		getFragmentManager().invalidateOptionsMenu();
                }
            }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { }
            }).show();
	}
	private void doDelete(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("削除確認");
        alertDialogBuilder.setMessage("本当に削除しますか？");
        alertDialogBuilder.setPositiveButton("はい",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
            			for(int i=receipt.size()-1; i>=0; i--){
            				CheckBox cb = cbs.get(i);
            				if(cb.isChecked()){
            					receipt.remove(receipt.getByIndex(i));
            				}
            			}
            			update();
                    }
                });
        alertDialogBuilder.setNegativeButton("いいえ", null);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}

	private Spinner getCategoryViews(){
    	Spinner spinner = new Spinner(this);
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        
        for(Category c : Data.getInstance().getCategories()){
        	adapter.add(c);
        }
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
        return spinner;
	}
}
