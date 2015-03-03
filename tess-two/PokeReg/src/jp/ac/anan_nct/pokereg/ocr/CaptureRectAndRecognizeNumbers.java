package jp.ac.anan_nct.pokereg.ocr;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class CaptureRectAndRecognizeNumbers extends CaptureRectangle {
	private Activity activity;
	public CaptureRectAndRecognizeNumbers(Activity activity, SurfaceView surfaceView, EventListener listener) {
		super(activity, surfaceView, listener);
		this.activity = activity;
	}


	@Override
	protected boolean up(MotionEvent event, Rect rect) {
		//this.capture(rect);
		AsyncOcrTask task = new AsyncOcrTask(this.activity, this);
		try {
			Log.i("CAP","START");
			task.execute(rect).get();
			Log.i("CAP","FINISH");
			return true;
		} catch (Exception e){
			return false;
		}
	}

}
