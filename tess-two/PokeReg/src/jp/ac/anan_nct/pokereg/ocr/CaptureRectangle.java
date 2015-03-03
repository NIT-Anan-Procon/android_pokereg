package jp.ac.anan_nct.pokereg.ocr;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public abstract class CaptureRectangle {
	private SurfaceView surfaceView;
	private Activity activity;
	private CameraCapture cameraCapture;
	private Rect rect;
	private boolean processing;
	
	public interface EventListener{
		boolean startCapture(MotionEvent event, Rect rect);
		void finishCapture(int captured);
		void failedCapture(Exception e);
	}
	private EventListener listener;
	
	public CaptureRectangle(Activity activity, SurfaceView surfaceView, EventListener listner){
		this.surfaceView = surfaceView;
		this.activity = activity;
		this.processing = false;
		this.listener = listner;

        // Preparation of Camera Capturing
		this.cameraCapture = new CameraCaptureAndRecognizeNumbers(this.activity, 
				new CameraCaptureAndRecognizeNumbers.EventListener() {
					@Override
					public void finishRecognition(int number) {
						processing = false;
						listener.finishCapture(number);
					}
					@Override
					public void failedRecognition(Exception e) {	
						processing = false;					
						listener.failedCapture(e);
					}
				});
		this.cameraCapture.preparation(this.surfaceView);
		
		this.surfaceView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!processing){
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN: return down(event);
					case MotionEvent.ACTION_UP:
						if(listener.startCapture(event, rect)){
							processing = true;
							return up(event, rect);
						}
						return false;
					case MotionEvent.ACTION_MOVE: return move(event);
					case MotionEvent.ACTION_CANCEL: return cancel(event);
					}
				}else{
					Log.i("C","CANCELED");
				}
				return true;
			}
		});
	}
	
	public boolean isProcessing() {
		return processing;
	}

	public void capture(Rect region){
		this.cameraCapture.capture(region);
	}
	
	public void release(){
		this.cameraCapture.release();
	}
	
	public void stop(){
		this.cameraCapture.stop();
	}
	
	public void start(){
		this.cameraCapture.start();
	}
	
	private boolean down(MotionEvent event){
		this.rect = new Rect();
		this.rect.top = this.rect.left = Integer.MAX_VALUE;
		this.rect.bottom = this.rect.right = Integer.MIN_VALUE;
		this.updateRect(event);
		return true;
	}
	
	protected abstract boolean up(MotionEvent event, Rect rect);
	
	private boolean move(MotionEvent event){
		this.updateRect(event);
		return true;
	}
	
	private boolean cancel(MotionEvent event){
		this.rect = null;
		return true;
	}
	
	private void updateRect(MotionEvent event){
		int x = (int)event.getX();
		int y = (int)event.getY();
		if(x < this.rect.left) this.rect.left = x;
		if(y < this.rect.top) this.rect.top = y;
		if(x > this.rect.right) this.rect.right = x;
		if(y > this.rect.bottom) this.rect.bottom = y;
	}
}
