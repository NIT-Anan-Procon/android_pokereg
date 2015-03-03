package jp.ac.anan_nct.pokereg.file;

import java.io.*;

import android.os.Environment;
import android.text.format.Time;

import jp.ac.anan_nct.pokereg.entity.*;

public class FileManager {
	private String rootDir;
	public FileManager(String rootDir){
		this.rootDir = rootDir;
	}
	public String getFolderFullPath(){
		File extStrageDir = Environment.getExternalStorageDirectory();
		return String.format("%s/%s", extStrageDir.getAbsolutePath(), this.rootDir);
	}
	public File getFile(Receipt receipt){
		return new File(this.getFolderFullPath(), String.format("%04d.csv", receipt.getId()));
	}
	public void write(Receipt receipt) throws IOException{
		FileWriter fw = new FileWriter(this.getFile(receipt));

		BufferedWriter bw = new BufferedWriter(fw);
		Time t = receipt.getTime();
		bw.append( String.format("%d,%d,%d,%d,%d",
					t.year, t.month, t.monthDay, t.hour, t.minute
				) );
		bw.newLine();
		bw.append( this.buildLabel() );
		bw.newLine();
		for(ReceiptRow row : receipt){
			bw.append( this.buildLine(row) );
			bw.newLine();
		}
		bw.close();
		fw.close();
	}
	public void write(ReceiptSet set) throws IOException{
		for(Receipt r : set){
			this.write(r);
		}
	}
	
	private String buildLabel() throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
				"カテゴリ",
				"単価",
				"税抜き/税込み",
				"数量",
				"値引額",
				"割引率",
				"税抜き小計",
				"税込み小計"
			));
		return sb.toString();		
	}
	private String buildLine(ReceiptRow row){
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s,%d,%s,%d,%d,%d,%d,%d",
				row.getCategory().getCaption(),
				row.getPrice(),
				row.isIncludeTax() ? "税込み" : "税抜き",
				row.getAmount(),
				row.getReduction(),
				row.getDiscount(),
				row.getTotalWithoutTax(),
				row.getTotalWithinTax()				
			));
		return sb.toString();
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
}
