package jp.ac.anan_nct.pokereg.ocr;

import jp.ac.anan_nct.pokereg.camera.CameraControl;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

public abstract class CameraCapture {
	private CameraControl cp;
	private Activity activity;
	private SurfaceView surfaceView;
	private Rect region;
	
	public CameraCapture(Activity activity){
		this.activity = activity;
	}
	
	public void preparation(SurfaceView surfaceView){
		this.surfaceView = surfaceView;
    	cp = new CameraControl(this.surfaceView);
		cp.setOnCaptureListener(new CameraControl.OnCaptureListener() {
			@Override
			public void onCapture(Bitmap bitmap) {
				Matrix mat = new Matrix();  
				mat.postRotate(90);
				Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);  
				CameraCapture.this.onCapture(activity, extractRegion(bmp));
			}
		});
	}
	
	public void capture(Rect region){
		this.region = region;
		this.cp.capture();
	}
	
	private Bitmap extractRegion(Bitmap bitmap){
		Matrix mat = new Matrix();
		mat.setScale(1.0f, 1.0f);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, false);

		float xscale = (float)bitmap.getWidth() / this.surfaceView.getWidth();
		float yscale = (float)bitmap.getHeight() / this.surfaceView.getHeight();
		Matrix matrix = new Matrix();
		matrix.setScale(1.0f, 1.0f);
		int x = (int)(this.region.left * xscale);
		int y = (int)(this.region.top * yscale);
		int width = (int)((this.region.right - this.region.left) * xscale);
		int height = (int)((this.region.bottom - this.region.top) * yscale);
		if(width <= 100) { width = 100; x -= 50; }
		if(height <= 100) { height = 100; y -= 50; }
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(x + width > bitmap.getWidth()) width = bitmap.getWidth() - x;
		if(y + height > bitmap.getHeight()) height = bitmap.getHeight() - y;
		Log.i("Rect", String.format("%d %d %d %d", x, y, width, height, matrix));

		bitmap = Bitmap.createBitmap(bitmap, x, y, width, height, matrix, false);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, false);

	}
	
	public void release(){
		this.cp.release();
	}
	
	public void stop(){
		this.cp.stop();
	}
	
	public void start(){
		this.cp.start();
	}
	
	protected abstract void onCapture(Activity activity, Bitmap bitmap);
}
