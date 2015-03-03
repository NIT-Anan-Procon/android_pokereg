package jp.ac.anan_nct.pokereg.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.text.format.Time;

public class Receipt extends Entities<Receipt, ReceiptRow> {
	private Time time = new Time("Asia/Tokyo");
	
	private String getImageFileName(){
		return String.format("%04d.png", this.getId());
	}
	public Bitmap getImage() {
		try {
			return Data.getInstance().getImages().readBitmap(this.getImageFileName());
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public void setImage(Bitmap image) {
		try {
			Data.getInstance().getImages().saveBitmap(image, this.getImageFileName());
		} catch (IOException e) {
		}
	}
	
	public void deleteImage(){
		File image = Data.getInstance().getImages().getFile(this.getImageFileName());
		if(image.exists()) image.delete();
	}
	public void deleteFile(){
		File file = Data.getInstance().getFiles().getFile(this);
		if(file.exists()) file.delete();
	}

	public int getTotal(){
		int total = 0;
		for(ReceiptRow r : this){
			total += r.getTotalWithinTax();
		}
		return total;
	}
	public int getCount(){
		int total = 0;
		for(ReceiptRow r : this){
			total += r.getAmount();
		}
		return total;
	}
	
	public Time getTime(){
		return this.time;
	}
	public void setToNow(){
		this.time.setToNow();
	}
	public void set(int second, int minute, int hour, int monthDay, int month, int year){
		this.time.set(second, minute, hour, monthDay, month, year);
	}
	public void setSecond(int second){
		this.time.set(second, this.time.minute, this.time.hour, this.time.monthDay, this.time.month, this.time.year);
	}
	public void setMinute(int minute){
		this.time.set(this.time.second, minute, this.time.hour, this.time.monthDay, this.time.month, this.time.year);
	}
	public void setHour(int hour){
		this.time.set(this.time.second, this.time.minute, hour, this.time.monthDay, this.time.month, this.time.year);
	}
	public void setMonthDay(int monthDay){
		this.time.set(this.time.second, this.time.minute, this.time.hour, monthDay, this.time.month, this.time.year);
	}
	public void setMonth(int month){
		this.time.set(this.time.second, this.time.minute, this.time.hour, this.time.monthDay, month, this.time.year);
	}
	public void setYear(int year){
		this.time.set(this.time.second, this.time.minute, this.time.hour, this.time.monthDay, this.time.month, year);
	}
}
