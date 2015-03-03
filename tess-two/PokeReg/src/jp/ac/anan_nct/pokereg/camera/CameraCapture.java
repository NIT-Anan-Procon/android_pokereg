package jp.ac.anan_nct.pokereg.camera;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.SurfaceView;

public class CameraCapture {
	public interface OnCapturedListener{
		void onCaptured(Bitmap bmp);
	}
	private CameraControl cp;
	
	public CameraCapture(SurfaceView surfaceView){
    	this.cp = new CameraControl(surfaceView);
	}
	
	public void preparation(final OnCapturedListener listener){
		cp.setOnCaptureListener(new CameraControl.OnCaptureListener() {
			@Override
			public void onCapture(Bitmap bitmap) {
				Matrix mat = new Matrix();  
				mat.postRotate(90);
				Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
				listener.onCaptured(bmp);
			}
		});
	}
	
	public void capture(){
		this.cp.capture();
	}

	public void start(){
		this.cp.start();
	}
	public void release(){
		this.cp.release();
	}
}
