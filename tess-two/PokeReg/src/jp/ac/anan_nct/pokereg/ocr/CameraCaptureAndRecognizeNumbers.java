package jp.ac.anan_nct.pokereg.ocr;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ac.anan_nct.pokereg.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraCaptureAndRecognizeNumbers extends CameraCapture {
	public interface EventListener {
		void finishRecognition(int number);
		void failedRecognition(Exception e);
	}
	private EventListener listener;
	
	private OCRNumber ocr;
	private Activity activity;
	public CameraCaptureAndRecognizeNumbers(Activity activity, EventListener listener) {
		super(activity);
		this.activity = activity;
		this.ocr = new OCRNumber(this.activity);
		this.listener = listener;
	}
	

	@Override
	public void preparation(SurfaceView surfaceView){
		super.preparation(surfaceView);
		try {
			this.ocr.preparation();
		} catch (IOException e) {
			Log.e("OCR-ERROR", e.getMessage());
			Toast.makeText(this.activity.getApplicationContext(),
					"学習データをSDカード上にコピーに失敗しました", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCapture(Activity activity, Bitmap bitmap) {
    	ImageView iv = (ImageView)activity.findViewById(R.id.imageView1);
    	iv.setImageBitmap(bitmap);
    	try{
	    	String result = ocr.start(bitmap);
	    	int value = toInt(result);
	    	listener.finishRecognition(value);
    	}catch(Exception e){
    		listener.failedRecognition(e);
    	}
	}
	
	private int toInt(String str) throws Exception{
		Pattern p = Pattern.compile("[1-9][0-9]*");
		Matcher m = p.matcher(str);
		if (m.find()){
		  String matchstr = m.group();
		  return Integer.parseInt(matchstr);
		}
		throw new Exception();
	}

}
