package jp.ac.anan_nct.pokereg.ocr;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncOcrTask extends AsyncTask<Rect, Void, Void> {
	private Activity activity;
	private CaptureRectAndRecognizeNumbers captureRectAndRecognizeNumbers;
    private ProgressDialog waitDialog;
    
    public AsyncOcrTask(Activity activity,
    		CaptureRectAndRecognizeNumbers captureRectAndRecognizeNumbers){
    	this.activity = activity;
    	this.captureRectAndRecognizeNumbers = captureRectAndRecognizeNumbers;
    }
    
    @Override
    protected void onPreExecute() {
    	Log.i("hoge", "認識開始");
    	this.waitDialog = new ProgressDialog(this.activity);
    	this.waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	this.waitDialog.setMessage("数値認識中");
    	this.waitDialog.show();
    }
    
	@Override
	protected Void doInBackground(Rect... arg0) {
		this.captureRectAndRecognizeNumbers.capture(arg0[0]);
		return null;
	}
	  @Override
	  protected void onPostExecute(Void result) {
		  this.waitDialog.dismiss();
		  Log.i("hoge", "認識終了");
	  }	        
}
