package jp.ac.anan_nct.pokereg;

import jp.ac.anan_nct.pokereg.camera.*;
import jp.ac.anan_nct.pokereg.camera.CameraCapture.OnCapturedListener;
import jp.ac.anan_nct.pokereg.entity.Data;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

public class CaptureReceiptActivity extends Activity {

	private CameraCapture camera;
	private SurfaceView surfaceView;
	private ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_receipt);
		this.surfaceView = (SurfaceView)this.findViewById(R.id.surfaceView1);
		this.imageView = (ImageView)this.findViewById(R.id.imageView1);
		this.camera = new CameraCapture(this.surfaceView);
		this.camera.preparation(new OnCapturedListener(){
			@Override
			public void onCaptured(Bitmap bmp) {
				imageView.setImageBitmap(bmp);
				surfaceView.setVisibility(View.INVISIBLE);
				imageView.setVisibility(View.VISIBLE);
				Data.getInstance().getDetailReceipt().setImage(bmp);
				finish();
			}});
		this.surfaceView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					camera.capture();
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.capture_receipt, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		this.camera.start();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		this.camera.release();
	}
	
}
