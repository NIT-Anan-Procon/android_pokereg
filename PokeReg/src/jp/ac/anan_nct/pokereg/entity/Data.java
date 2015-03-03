package jp.ac.anan_nct.pokereg.entity;

import java.util.LinkedList;
import java.util.Random;

import android.graphics.Bitmap;
import jp.ac.anan_nct.pokereg.file.FileManager;
import jp.ac.anan_nct.pokereg.flick.FlickAction;
import jp.ac.anan_nct.pokereg.image.ImageManager;

public class Data {
	private static Data instance = new Data();
	public static Data getInstance(){ return instance; }
	private Data(){
		addDefaultFlicksAsTest();
		addDefaultCategoriesAsTest();
	}
	public void initialize(){
		this.addDummyReceipt(8, 20);
		this.addDummyReceipt(8, 22);
		this.addDummyReceipt(8, 24);
		this.addDummyReceipt(8, 30);
		this.addDummyReceipt(9, 2);
		this.addDummyReceipt(9, 7);
		this.addDummyReceipt(9, 19);
		this.addDummyReceipt(9, 21);
		this.addDummyReceipt(9, 30);
		this.addDummyReceipt(10, 1);
		this.addDummyReceipt(10, 3);
		this.addDummyReceipt(10, 7);
	}

	Random rand = new Random();
	private void addDummyReceipt(int mounth, int day){
		Receipt receipt1 = new Receipt();
		receipt1.setToNow();
		receipt1.setMonth( mounth );
		receipt1.setMonthDay( day );
		receipt1.setHour( rand.nextInt(10) + 10);
		receipt1.setMinute( rand.nextInt(59) + 1);
		int size = rand.nextInt(10) + 3;
		for(int i=0; i<size; i++){
			receipt1.add(new ReceiptRow(rand.nextInt(2000) + 100, rand.nextInt(6)+1, false,
					this.categories.getByIndex(rand.nextInt(3)), rand.nextInt(3)*10, rand.nextInt(4)*5));
		}
		this.receipts.add(receipt1);
	}

	private ImageManager images = new ImageManager("PokeReg");
	private FileManager files = new FileManager("PokeReg");
	private CategorySet categories = new CategorySet();
	private Receipt currentReceipt = new Receipt();
	private Bitmap showImageBitmap;

	public ImageManager getImages(){
		return this.images;
	}
	public FileManager getFiles(){
		return this.files;
	}
	public Bitmap getShowImageBitmap() {
		return showImageBitmap;
	}
	public void setShowImageBitmap(Bitmap showImageBitmap) {
		this.showImageBitmap = showImageBitmap;
	}

	private Receipt detailReceipt = null;
	public Receipt getDetailReceipt() {
		return detailReceipt;
	}
	public void setDetailReceipt(Receipt detailReceipt) {
		this.detailReceipt = detailReceipt;
	}

	private ReceiptSet receipts = new ReceiptSet();
	private FlickSet flicks = new FlickSet();
	
	public CategorySet getCategories() {
		return categories;
	}
	public Receipt getCurrentReceipt(){
		return this.currentReceipt;
	}
	public Receipt addCurrentReceiptToReceiptSet(){
		Receipt receipt = new Receipt();
		for(ReceiptRow r : this.currentReceipt){
			receipt.add(r);
			receipt.setToNow();
		}
		this.receipts.add(receipt);
		receipt.setImage( this.currentReceipt.getImage() );
		return receipt;
	}
	public void initCurrentReceipt(){
		this.currentReceipt.clear();
		this.currentReceipt.deleteImage();
	}
	public ReceiptSet getReceiptSet(){
		return this.receipts;
	}
	public FlickSet getFlickSet(){
		return this.flicks;
	}
	
	
	

	private void addDefaultCategoriesAsTest(){
		this.categories.add(new Category("その他"));
		this.categories.add(new Category("食品"));
		this.categories.add(new Category("雑貨"));
		this.categories.setDefaultCategoryIndex(0);
	}
	
	private void addDefaultFlicksAsTest(){
		this.flicks.add(new int[]{
				0,1,2
		}, FlickAction.ADD_AMOUNT.getId(), 1);
		this.flicks.add(new int[]{
				2,1,0
		}, FlickAction.SUB_AMOUNT.getId(), 1);
		this.flicks.add(new int[]{
				2,1,0,3,6,7,8
		}, FlickAction.REVERT_CART.getId(), 0);
		this.flicks.add(new int[]{
				7,6,3,0,1,2,5,8,7
		}, FlickAction.RESET_TOTAL.getId(), 0);
		this.flicks.add(new int[]{
				0,3,6
		}, FlickAction.NEXT_CATEGORY.getId(), 0);
		this.flicks.add(new int[]{
				6,3,0
		}, FlickAction.PREV_CATEGORY.getId(), 0);
		this.flicks.add(new int[]{
				0,3,6,7,8
		}, FlickAction.POP_PRICE.getId(), 0);
		this.flicks.add(new int[]{
				3,4,5
		}, FlickAction.ADD_DISCOUNT.getId(), 5);
		this.flicks.add(new int[]{
				5,4,3
		}, FlickAction.SUB_DISCOUNT.getId(), 5);
		this.flicks.add(new int[]{
				6,7,8
		}, FlickAction.ADD_REDUCTION.getId(), 5);
		this.flicks.add(new int[]{
				8,7,6
		}, FlickAction.SUB_REDUCTION.getId(), 5);
		this.flicks.add(new int[]{
				2,1,0,3,6,7,8,5,4
		}, FlickAction.OPEN_CART.getId(), 1);
		this.flicks.add(new int[]{
				0,1,2,4,6,7,8
		}, FlickAction.TOGGLE_USING_CAMERA.getId(), 0);
		this.flicks.add(new int[]{
				6,3,0,4,8,5,2
		}, FlickAction.TOGGLE_HAND.getId(), 0);
		this.flicks.add(new int[]{
				9,10,11
		}, FlickAction.CAPTURE.getId(), 0);
		

		this.flicks.add(new int[]{
				2,5,8
		}, FlickAction.SET_DISCOUNT.getId(), 10);
		this.flicks.add(new int[]{
				1,2,5,4,7,8
		}, FlickAction.SET_DISCOUNT.getId(), 20);
		this.flicks.add(new int[]{
				1,2,5,4,5,8,7
		}, FlickAction.SET_DISCOUNT.getId(), 30);
		this.flicks.add(new int[]{
				1,4,5,2,5,8
		}, FlickAction.SET_DISCOUNT.getId(), 40);
		this.flicks.add(new int[]{
				2,1,4,5,8,7
		}, FlickAction.SET_DISCOUNT.getId(), 50);


	}
}
