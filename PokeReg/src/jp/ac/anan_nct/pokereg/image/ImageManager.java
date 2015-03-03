package jp.ac.anan_nct.pokereg.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImageManager {

	private String folder;
	public ImageManager(String folder){
		this.folder = folder;
		File root = new File(this.getFolderFullPath());
		root.mkdir();
	}
	public String getFolderFullPath(){
		File extStrageDir = Environment.getExternalStorageDirectory();
		return String.format("%s/%s", extStrageDir.getAbsolutePath(), this.folder);
	}
	public File getFile(String filename){
		return new File(this.getFolderFullPath(), filename);
	}

	public File[] getImages(){
		File dir = new File(this.getFolderFullPath());
		return dir.listFiles();
	}

	public void clear(){
		File dir = new File(this.getFolderFullPath());
		for(File f : dir.listFiles()){
			f.delete();
		}
	}
	public void remove(File f){
		f.delete();
	}
	
	public Bitmap readBitmap(File image) throws FileNotFoundException{
		FileInputStream fis = new FileInputStream(image);
		return BitmapFactory.decodeStream(fis);
	}
	public Bitmap readBitmap(String filename) throws FileNotFoundException{
		FileInputStream fis = new FileInputStream(this.getFile(filename));
		return BitmapFactory.decodeStream(fis);
	}
	
	public File saveBitmap(Bitmap bitmap, String filename, CompressFormat format, int quality)
			throws IOException{
		if(bitmap == null) return null;
		File file = this.getFile(filename);
		FileOutputStream outStream = new FileOutputStream(file);
	    bitmap.compress(format, quality, outStream);
	    outStream.close();		
	    return file;
	}
	public File saveBitmap(Bitmap bitmap, String filename, CompressFormat format) throws IOException {
		return this.saveBitmap(bitmap, filename, format, 100);
	}
	public File saveBitmap(Bitmap bitmap, String filename) throws IOException {
		return this.saveBitmap(bitmap, filename, CompressFormat.PNG);
	}
}
