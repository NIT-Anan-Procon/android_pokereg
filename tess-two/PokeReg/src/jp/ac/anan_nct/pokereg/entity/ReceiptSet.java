package jp.ac.anan_nct.pokereg.entity;

import java.io.IOException;


public class ReceiptSet extends Entities<ReceiptSet, Receipt> {
	
	@Override
	public void afterAdd(Receipt receipt){
		try {
			Data.getInstance().getFiles().write(receipt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void beforeRemove(Receipt receipt){
		receipt.deleteImage();
		receipt.deleteFile();
	}
	@Override
	public void afterUpdate(Receipt receipt){
		try {
			Data.getInstance().getFiles().write(receipt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
