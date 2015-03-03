package jp.ac.anan_nct.pokereg.ocr;

import jp.ac.anan_nct.pokereg.config.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCRNumber {
	// 学習データ
	final static String LEARN_DATA_FILE_NAME = "jpn.traineddata";
	// 学習データの保存先パス
	final static String LEARN_DATA_PATH = Environment.getExternalStorageDirectory() + "/learn/";
	
	private Activity activity;
	
	public OCRNumber(Activity activity){
		this.activity = activity;
	}
	
	public void preparation() throws IOException {
		// 学習データのチェック
		checkLearnData();
	}
	
	public String start(Bitmap bitmap) throws IOException{
		return recognize(bitmap);
	}
	

	public String recognize(Bitmap bitmap) throws IOException {
		// 二値化処理の実行
		if(Configuration.getInstance().useBinarizeForOCR()){
			Log.i("OCR-Option", String.format("use binarize, th:%d",
					Configuration.getInstance().getBinarizeThresholdForOCR()));
			bitmap = binarizeBitmap(bitmap,
					Configuration.getInstance().getBinarizeThresholdForOCR());
		}
		// 認識処理の実行
		return recognizeBitmap(bitmap);
	}
	
	/**
	 * 画像の認識処理(OCR)
	 * @param bitmap　処理対象の画像(Bitmap)
	 * @return 認識された文字列
	 * @throws IOException
	 */
	protected String recognizeBitmap(Bitmap bitmap) throws IOException {
		Bitmap new_bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		// Tesseart-ocrのインスタンスを生成
		TessBaseAPI tes = new TessBaseAPI();
		tes.init(LEARN_DATA_PATH, "jpn"); // 日本語の学習データを使用
		tes.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789"); // 認識対象を数字だけに制限
		tes.setImage(new_bitmap);
		// 認識結果を取得
		String recognized_text = tes.getUTF8Text();
		// インスタンスの終了処理
		tes.end();
		return recognized_text;
	}
	
	/**
	 * 画像の二値化処理
	 * @param bitmap　処理対象の画像(Bitmap)
	 * @param threshold 二値化の閾値
	 * @return　二値化された画像
	 */
	protected Bitmap binarizeBitmap(Bitmap bitmap, int threshold) {
		Bitmap new_bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
			for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
				int pixel = bitmap.getPixel(x, y);
				int gray = (Color.red(pixel)+Color.green(pixel)+Color.blue(pixel))/3;
				if (gray < threshold) {
					new_bitmap.setPixel(x, y, Color.rgb(0, 0, 0));
				} else {
					new_bitmap.setPixel(x, y, Color.rgb(255, 255, 255));
				}
			}
		}
		return new_bitmap;
	}
	
	/**
	 * 学習データのチェック および assets ディレクトリからのコピー処理
	 * @throws IOException
	 * (Tesseract-OCRによる処理の為に、前もって assets ディレクトリからSDカード上に学習データをコピーする)
	 */
	protected void checkLearnData() throws IOException {
		// 学習データの保存先ディレクトリのチェック
		File learn_data_dir = new File(LEARN_DATA_PATH + "tessdata/");
		if (!learn_data_dir.exists())
			learn_data_dir.mkdir();
		
		// 学習データファイルがSDカード上に存在するか否かのチェック
		File learn_data_file = new File(LEARN_DATA_PATH + "tessdata/" + LEARN_DATA_FILE_NAME);
		if (!learn_data_file.exists()) {
			// 学習データをassetsからSDカードへコピー
			File myDir = new File(LEARN_DATA_PATH + "tessdata/");
		    if (!myDir.exists()) {
		        myDir.mkdirs();
		    }
		    Log.i("PATH", LEARN_DATA_PATH + "tessdata/" + LEARN_DATA_FILE_NAME);
		    Log.i("PATH", LEARN_DATA_PATH + "tessdata/");
		    Log.i("PATH", LEARN_DATA_PATH  + "tessdata/" + LEARN_DATA_FILE_NAME);
			Toast.makeText(this.activity.getApplicationContext(), "学習データをSDカード上にコピーしています...", Toast.LENGTH_LONG).show();
			InputStream input = this.activity.getResources().getAssets().open("tessdata/" + LEARN_DATA_FILE_NAME);

			FileOutputStream output = new FileOutputStream(LEARN_DATA_PATH  + "tessdata/" + LEARN_DATA_FILE_NAME, true);
			byte[] buffer = new byte[1024]; 
			int length; 
			while ((length = input.read(buffer)) >= 0) {
				output.write(buffer, 0, length);
		    }
			input.close();
			output.close();
			Toast.makeText(this.activity.getApplicationContext(), "学習データをSDカード上にコピーが完了しました", Toast.LENGTH_LONG).show();
		}
	}
	
}
