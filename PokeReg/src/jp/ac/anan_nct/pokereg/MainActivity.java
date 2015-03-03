package jp.ac.anan_nct.pokereg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import jp.ac.anan_nct.pokereg.entity.Data;
import jp.ac.anan_nct.pokereg.config.ComfortableHand;
import jp.ac.anan_nct.pokereg.config.Configuration;
import jp.ac.anan_nct.pokereg.entity.Receipt;
import jp.ac.anan_nct.pokereg.entity.ReceiptRow;
import jp.ac.anan_nct.pokereg.flick.FlickActions;
import jp.ac.anan_nct.pokereg.ocr.*;
import jp.ac.anan_nct.pokereg.view.CartView;
import jp.ac.anan_nct.pokereg.view.Flickable10Key;
import jp.ac.anan_nct.pokereg.view.SmallReceiptView;
import jp.ac.anan_nct.pokereg.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
    
	private CaptureRectangle captureRect;
	private Receipt currentReceipt;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Data.getInstance().initialize();
        
        final View previewImage = (View)this.findViewById(R.id.imageView1);
        previewImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				previewImage.setVisibility(View.INVISIBLE);
			}
		});
        
        this.currentReceipt = Data.getInstance().getCurrentReceipt();
        
        final CartView cart = (CartView)this.findViewById(R.id.cartView);
        cart.setCategorySet(Data.getInstance().getCategories());
        cart.setReceipt(this.currentReceipt);
        cart.setTax(Configuration.getInstance().getTax());
        cart.setIncludeTax(Configuration.getInstance().isIncludingTax());

        final SmallReceiptView receipt = (SmallReceiptView)this.findViewById(R.id.smallReceiptView);
        receipt.setReceipt(this.currentReceipt);

        cart.setListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					openCart();
				}
				return true;
			}
		});
        
        receipt.setCallback(new SmallReceiptView.Callback(){
			@Override
			public void afterDeleteItem(ReceiptRow item) {
				cart.setPrice(item.getPrice());
				cart.setDiscount(item.getDiscount());
				cart.setReduction(item.getReduction());
				cart.setAmount(item.getAmount());
				cart.setIncludeTax(item.isIncludeTax());
				cart.setCategory(item.getCategory());
			}
        });

        Flickable10Key fkeys = (Flickable10Key)this.findViewById(R.id.flickable10Key);
        fkeys.setKeySetPattern(Configuration.getInstance().getKeySetPattern());
        fkeys.setEventListner(new Flickable10Key.Flickable10KeyEventListener() {			
			@Override
			public void onTouch(Key key) {
			    Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
			    vib.vibrate(100);
			    
				CartView cartView = (CartView)findViewById(R.id.cartView);
				switch(key){
				case Number0: cartView.pushNumber(0); break;
				case Number1: cartView.pushNumber(1); break;
				case Number2: cartView.pushNumber(2); break;
				case Number3: cartView.pushNumber(3); break;
				case Number4: cartView.pushNumber(4); break;
				case Number5: cartView.pushNumber(5); break;
				case Number6: cartView.pushNumber(6); break;
				case Number7: cartView.pushNumber(7); break;
				case Number8: cartView.pushNumber(8); break;
				case Number9: cartView.pushNumber(9); break;
				case Clear:
					cartView.clear();
					break;
				case Enter:
					cartView.enter();
					break;
				}
			}
			
			@Override
			public void onFlick(int[] sequence) {
				CartView cartView = (CartView)findViewById(R.id.cartView);
				FlickActions flickActions = new FlickActions(
						Data.getInstance().getFlickSet(),
						MainActivity.this, cartView);
				if(flickActions.execute(sequence)){
					Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
					vib.vibrate(100);
				}
			}
		});
        
        this.setApplicationWallpaper();
    }
    
    public void updateComfortableHand(){
    	RelativeLayout key = (RelativeLayout)findViewById(R.id.flickable10KeyLayout);
    	RelativeLayout receipt = (RelativeLayout)findViewById(R.id.smallReceiptViewLayout);
    	switch(Configuration.getInstance().getComfortableHand()){
    	case RightHand:
    		key.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
    		receipt.setGravity(Gravity.LEFT | Gravity.BOTTOM);
    		break;
    	case LeftHand:
    		key.setGravity(Gravity.LEFT | Gravity.BOTTOM);
    		receipt.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
    		break;
    	}
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
        this.updateComfortableHand();
    }
    @Override
    protected void onResume() {
    	super.onResume();

    	//View root = (View)this.findViewById(R.id.topLevel);
    	//Flickable10Key fkeys = (Flickable10Key)this.findViewById(R.id.flickable10Key);
        //fkeys.setWidth(root.getWidth() );
        //fkeys.invalidate();
        
    	this.refreshCamera();
    }
    private final int SURFACE_VIEW = 10000000;
    private void refreshCamera(){
		RelativeLayout top = (RelativeLayout)findViewById(R.id.topLevel);
    	if(Configuration.getInstance().isUseCamera() && this.captureRect == null){
    		SurfaceView sv = new SurfaceView(this);
    		sv.setId(SURFACE_VIEW);
    		top.addView(sv, 0);
    		ImageView imageView = (ImageView)findViewById(R.id.imageView1);
    		ImageView backgroundImageView = (ImageView)findViewById(R.id.backgroundImageView);
    		imageView.setVisibility(View.VISIBLE);
    		backgroundImageView.setVisibility(View.INVISIBLE);  
    		final ImageView  previewImage = (ImageView)this.findViewById(R.id.imageView1);
			this.captureRect = new CaptureRectAndRecognizeNumbers(this, sv,
					new CaptureRectAndRecognizeNumbers.EventListener(){
						@Override
						public boolean startCapture(MotionEvent event, Rect rect) {
							//Toast.makeText(MainActivity.this, "認識開始", Toast.LENGTH_SHORT).show();				    	
							Flickable10Key fkeys = (Flickable10Key)findViewById(R.id.flickable10Key);
							fkeys.setAcceptInput(false);
							previewImage.setImageBitmap(null);
							previewImage.setVisibility(View.VISIBLE);
							return true;
						}
						@Override
						public void finishCapture(int captured) {
							Flickable10Key fkeys = (Flickable10Key)findViewById(R.id.flickable10Key);
							fkeys.setAcceptInput(true);
							Toast.makeText(MainActivity.this, String.valueOf(captured), Toast.LENGTH_SHORT).show();
							CartView cartView = (CartView)findViewById(R.id.cartView);
							cartView.setPrice(captured);
						}
	
						@Override
						public void failedCapture(Exception e) {
							Flickable10Key fkeys = (Flickable10Key)findViewById(R.id.flickable10Key);
							fkeys.setAcceptInput(true);
							Toast.makeText(MainActivity.this, "認識失敗", Toast.LENGTH_SHORT).show();
						}});
    	}else if(!Configuration.getInstance().isUseCamera() && this.captureRect != null){
    		SurfaceView sv = (SurfaceView)findViewById(SURFACE_VIEW);
    		top.removeView(sv);
    		ImageView imageView = (ImageView)findViewById(R.id.imageView1);
    		ImageView backgroundImageView = (ImageView)findViewById(R.id.backgroundImageView);
    		imageView.setVisibility(View.INVISIBLE);
    		backgroundImageView.setVisibility(View.VISIBLE);
    		if(this.captureRect != null){
        		this.captureRect.release();
        		this.captureRect = null;
        	}
    		Log.i("RELEASED", "SurfaceView Deleted");
    	}
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	if(this.captureRect != null){
    		this.captureRect.release();
    		this.captureRect = null;
    	}
    }
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item1 = menu.findItem(R.id.useBinarize);
        item1.setChecked(Configuration.getInstance().useBinarizeForOCR());
        //item1.setVisible(false);

        MenuItem item2 = menu.findItem(R.id.binarizeThreshold);
        this.syncBinaryThresholdTitle(item2);
        //item2.setVisible(false);

        MenuItem item3 = menu.findItem(R.id.includingTax);
        item3.setChecked(Configuration.getInstance().isIncludingTax());
		
        MenuItem item4 = menu.findItem(R.id.tax);
        item4.setTitle(String.format("税率:%d%%", Configuration.getInstance().getTax()));

        MenuItem item5 = menu.findItem(R.id.useCamera);
        item5.setChecked(Configuration.getInstance().isUseCamera());

        MenuItem item6 = menu.findItem(R.id.editBudget);
        item6.setTitle(String.format("予算:¥%d", Configuration.getInstance().getBudget()));
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.useBinarize){
            Configuration.getInstance().setUsingBinarizeForOCR(!item.isChecked());
        	getFragmentManager().invalidateOptionsMenu();
            return true;
        }
        if(id == R.id.openCart){
        	this.openCart();
        	return true;
        }
        if(id == R.id.useCamera){
        	this.toggleUsingCamera();
            return true;
        }
        if(id == R.id.toggle_hand){
            this.toggleHand();
            return true;
        }
        if(id == R.id.toggle_key_set_pattern){
            this.toggleKeySetPattern();
            return true;
        }
        if(id == R.id.binarizeThreshold){
        	this.showSettingBinaryThresholdDialog();
            return true;
        }
        if(id == R.id.settingFlick){
        	Intent setting = new Intent(MainActivity.this, FlickSettingActivity.class);
        	startActivity(setting);
            return true;
        }
        if(id == R.id.settingCategory){
        	Intent setting = new Intent(MainActivity.this, CategoryActivity.class);
        	startActivity(setting);
            return true;
        }
        if(id == R.id.startHistoryActivity){
        	Intent receipts = new Intent(MainActivity.this, ReceiptsActivity.class);
        	startActivity(receipts);
            return true;
        }
        if(id == R.id.includingTax){
        	this.toggleIncludingTax();
            return true;
        }
        if(id == R.id.tax){
        	this.showSettingTaxDialog();
            return true;
        }
        if(id == R.id.editBudget){
        	this.editBudget();
            return true;
        }
        if(id == R.id.selectWallpaper){
        	this.openGallay();
            return true;
        }
        
        
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void editBudget(){
        final EditText editView = new EditText(MainActivity.this);
        editView.setText(String.valueOf(Configuration.getInstance().getBudget()));
        new AlertDialog.Builder(MainActivity.this)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("予算")
            .setView(editView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	try{
                		int value = Integer.parseInt(editView.getText().toString());
                		Configuration.getInstance().setBudget(value);
                	}catch(Exception e){ }
                	getFragmentManager().invalidateOptionsMenu();
                }
            }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).show();
    }
    
    private void showSettingBinaryThresholdDialog(){
        final EditText editView = new EditText(MainActivity.this);
        editView.setText(String.valueOf(Configuration.getInstance().getBinarizeThresholdForOCR()));
        new AlertDialog.Builder(MainActivity.this)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("二値化閾値")
            .setView(editView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	try{
                		int value = Integer.parseInt(editView.getText().toString());
                		Configuration.getInstance().setBinarizeThresholdForOCR(value);
                	}catch(Exception e){ }
                	getFragmentManager().invalidateOptionsMenu();
                }
            }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).show();
    }
    private void showSettingTaxDialog(){
        final EditText editView = new EditText(MainActivity.this);
        editView.setText(String.valueOf(Configuration.getInstance().getTax()));
        new AlertDialog.Builder(MainActivity.this)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("税率")
            .setView(editView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	try{
                		int value = Integer.parseInt(editView.getText().toString());
                		Configuration.getInstance().setTax(value);
        				CartView cartView = (CartView)findViewById(R.id.cartView);
        				cartView.setTax(value);
                	}catch(Exception e){ }
                	getFragmentManager().invalidateOptionsMenu();
                }
            }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).show();
    }
    private void syncBinaryThresholdTitle(MenuItem item) {
        item.setTitle(String.format("二値化閾値:%d",
        		Configuration.getInstance().getBinarizeThresholdForOCR()));
    }
    
    
    public void openCart(){
		this.currentReceipt.setToNow();
		Data.getInstance().setDetailReceipt(this.currentReceipt);
    	Intent detail = new Intent(MainActivity.this, ReceiptDetailActivity.class);
    	startActivity(detail);
    }
    
    public void toggleIncludingTax(){
        Configuration.getInstance().setIncludingTax(!Configuration.getInstance().isIncludingTax());
		CartView cartView = (CartView)findViewById(R.id.cartView);
		cartView.setIncludeTax(Configuration.getInstance().isIncludingTax());
    	getFragmentManager().invalidateOptionsMenu();
    }
    public void setIncludingTax(boolean including){
        Configuration.getInstance().setIncludingTax(including);
		CartView cartView = (CartView)findViewById(R.id.cartView);
		cartView.setIncludeTax(Configuration.getInstance().isIncludingTax());
    	getFragmentManager().invalidateOptionsMenu();
    }
    
    public void toggleUsingCamera(){
        Configuration.getInstance().setUseCamera(!Configuration.getInstance().isUseCamera());
		this.refreshCamera();
    	getFragmentManager().invalidateOptionsMenu();
    }
    public void setUsingCamera(boolean using){
        Configuration.getInstance().setUseCamera(using);
		this.refreshCamera();
    	getFragmentManager().invalidateOptionsMenu();
    }
    
    public void toggleKeySetPattern(){
    	switch(Configuration.getInstance().getKeySetPattern()){
    	case TopDown:
    		Configuration.getInstance().setKeySetPattern(KeySetPattern.BottomUp);
    		break;
    	case BottomUp:
    		Configuration.getInstance().setKeySetPattern(KeySetPattern.TopDown);
    		break;
    	}
		Flickable10Key fk = (Flickable10Key)findViewById(R.id.flickable10Key);
		fk.setKeySetPattern(Configuration.getInstance().getKeySetPattern());
    	getFragmentManager().invalidateOptionsMenu();
    }
    public void setKeySetPattern(KeySetPattern pattern){
		Configuration.getInstance().setKeySetPattern(pattern);
		Flickable10Key fk = (Flickable10Key)findViewById(R.id.flickable10Key);
		fk.setKeySetPattern(Configuration.getInstance().getKeySetPattern());
    	getFragmentManager().invalidateOptionsMenu();
    }
    
    public void toggleHand(){
		Configuration.getInstance().toggleComfortableHand();
		this.updateComfortableHand();
    	getFragmentManager().invalidateOptionsMenu();
    }
    public void setLeftHand(){
		Configuration.getInstance().setComfortableHand(ComfortableHand.LeftHand);
		this.updateComfortableHand();
    	getFragmentManager().invalidateOptionsMenu();
    }
    public void setRightHand(){
		Configuration.getInstance().setComfortableHand(ComfortableHand.RightHand);
		this.updateComfortableHand();
    	getFragmentManager().invalidateOptionsMenu();
    }

	Random rand = new Random();
    public void echoMyLuck(){
    	String[] lucks = new String[]{
    			"大吉", "中吉", "小吉", "凶", "大凶"
    	};
    	Toast.makeText(this, lucks[this.rand.nextInt(lucks.length)], Toast.LENGTH_SHORT).show();
    }

	private static int REQUEST_ACTION_PICK = 1;    
    private void openGallay(){
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    	intent.setType("image/*");
    	startActivityForResult(Intent.createChooser(intent,"壁紙選択"), REQUEST_ACTION_PICK);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_ACTION_PICK){
                try {
                    InputStream iStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bm = BitmapFactory.decodeStream(iStream);
                    iStream.close();
                    Data.getInstance().getImages().saveBitmap(bm, "wallpaper.png");
                    this.setApplicationWallpaper();
                }catch (IOException e) {}
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void setApplicationWallpaper(){
        try {
        	Bitmap wallpaper = Data.getInstance().getImages().readBitmap("wallpaper.png");
	        ImageView bg = (ImageView)findViewById(R.id.backgroundImageView);
	        bg.setImageBitmap(wallpaper);
		} catch (Exception e) {
		}
    }
    
    public void capture(){
    	if(Configuration.getInstance().isUseCamera()){
	    	View root = (View)this.findViewById(R.id.topLevel);
	    	View cart = (View)this.findViewById(R.id.cartView);
	    	View fk = (View)this.findViewById(R.id.flickable10Key);
	    	float top = cart.getY() + cart.getHeight();
	    	float bottom = fk.getY();
	    	float width = root.getWidth();
	    	Rect r = new Rect();
	    	r.set(0,(int)top,(int)width,(int)bottom);
	    	this.captureRect.capture(r);
    	}
    }
}
