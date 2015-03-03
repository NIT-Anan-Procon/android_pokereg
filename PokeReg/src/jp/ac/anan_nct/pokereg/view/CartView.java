package jp.ac.anan_nct.pokereg.view;


import jp.ac.anan_nct.pokereg.R;
import jp.ac.anan_nct.pokereg.config.Configuration;
import jp.ac.anan_nct.pokereg.entity.Category;
import jp.ac.anan_nct.pokereg.entity.CategorySet;
import jp.ac.anan_nct.pokereg.entity.EntitiesObserver;
import jp.ac.anan_nct.pokereg.entity.Receipt;
import jp.ac.anan_nct.pokereg.entity.ReceiptRow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class CartView extends View 
		implements EntitiesObserver {
	
	public void setListener(View.OnTouchListener listener){
		this.setOnTouchListener(listener);
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = Math.min(Math.max(price, 0), 99999);
		this.invalidate();
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = Math.min(Math.max(0, discount), 100);
		this.invalidate();
	}
	public int getReduction() {
		return reduction;
	}
	public void setReduction(int reduction) {
		this.reduction = Math.min(Math.max(0, reduction), 99999);
		this.invalidate();
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = Math.min(Math.max(1,amount), 99);
		this.invalidate();
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
		this.invalidate();
	}

	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
		this.invalidate();
	}
	public boolean isIncludeTax() {
		return includeTax;
	}
	public void setIncludeTax(boolean includeTax) {
		this.includeTax = includeTax;
		this.invalidate();
	}
	public Receipt getReceipt() {
		return receipt;
	}
	public void setReceipt(Receipt r) {
		receipt = r;
		this.receipt.addOvserver(this);
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
		this.invalidate();
	}
	public void setCategoryById(int id) {
		this.category = this.categories.getById(id);
		this.invalidate();
	}
	
	public void setCategorySet(CategorySet categories){
		this.categories = categories;
		this.categories.addOvserver(this);
		this.resetCategory();
	}
	
	private CategorySet categories;
	private Receipt receipt;
	private int price = 0;
	private int amount = 1;
	private int total = 0;
	private int discount = 0;
	private int reduction = 0;
	private Category category = null;

	private Paint bg = new Paint();
	private Paint fg = new Paint();
	private Paint fgm = new Paint();
	private Paint fgl = new Paint();
	private Paint fgs = new Paint();
	private Paint icon = new Paint();
	private Paint budgetFgEnough = new Paint();
	private Paint budgetFgCrisis = new Paint();
	private Paint budgetFgOver = new Paint();
	private void initPaint(){
		bg.setColor(0x66ffffff);
		fg.setColor(0xff000000);
		fg.setTextSize(50f);
		fgm.setColor(0xff000000);
		fgm.setTextSize(120f);
		fgl.setColor(0xff000000);
		fgl.setTextSize(140f);
		fgs.setColor(0xff000000);
		fgs.setTextSize(40f);
		icon.setColor(0xaa000000);
		
		budgetFgEnough.setTextSize(30f);
		budgetFgEnough.setColor(0xff000000);
		budgetFgCrisis.setTextSize(30f);
		budgetFgCrisis.setColor(0xffaaaa00);
		budgetFgOver.setTextSize(30f);
		budgetFgOver.setColor(0xffaa0000);
	}

	private int tax = 8;
	private boolean includeTax = false;
	
	/* Constructors */
	public CartView(Context context) {
		super(context);
		this.init();
	}
	public CartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}
	public CartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init();
	}

	private Bitmap[] carts = new Bitmap[4];
	private void init(){
		this.setClickable(true);
		Bitmap bmp0 = BitmapFactory.decodeResource(this.getResources(), R.drawable.cart0);
		Bitmap bmp1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.cart1);
		Bitmap bmp2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.cart2);
		Bitmap bmp3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.cart3);
		Matrix rm0 = new Matrix();
		Matrix rm1 = new Matrix();
		Matrix rm2 = new Matrix();
		Matrix rm3 = new Matrix();
		rm0.setScale(160f / bmp0.getWidth(), 160f / bmp0.getHeight());
		rm1.setScale(160f / bmp1.getWidth(), 160f / bmp1.getHeight());
		rm2.setScale(160f / bmp2.getWidth(), 160f / bmp2.getHeight());
		rm3.setScale(160f / bmp3.getWidth(), 160f / bmp3.getHeight());
		this.carts[0] = Bitmap.createBitmap(bmp0, 0,0, bmp0.getWidth(), bmp0.getHeight(), rm0, true);
		this.carts[1] = Bitmap.createBitmap(bmp1, 0,0, bmp0.getWidth(), bmp1.getHeight(), rm1, true);
		this.carts[2] = Bitmap.createBitmap(bmp2, 0,0, bmp0.getWidth(), bmp2.getHeight(), rm2, true);
		this.carts[3] = Bitmap.createBitmap(bmp3, 0,0, bmp0.getWidth(), bmp3.getHeight(), rm3, true);
		this.initPaint();
	}
	
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    setMeasuredDimension(1000,340);
	}

	private Rect tmp = new Rect();
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(0, 0, 1000, 340, bg);
		canvas.drawText("単価:",10,110,fg);
		canvas.drawText(String.format("¥%d", this.price), 130,110,fgm);
		canvas.drawText("×",740,110,fg);
		canvas.drawText(String.format("%d", this.amount), 770,110,fgm);
		if(this.discount != 0){
			canvas.drawText(String.format("%d%%オフ", this.discount), 550,60,fgs);
		}
		if(this.reduction != 0){
			canvas.drawText(String.format("-¥%d", this.reduction), 550,120,fgs);
		}
		canvas.drawText("合計:",10,250,fg);
		canvas.drawText(String.format("¥%d", this.total), 120,250,fgl);
		canvas.drawText(String.format("点数:%d", this.getTotalAmount()), 10,320,fgs);
		canvas.drawText(String.format(this.includeTax ? "税込み入力(%d%%)" : "税抜き入力(%d%%)", this.tax), 210,320,fgs);
		if(this.getCategory() != null){
			canvas.drawText(String.format("カテゴリ:%s", this.getCategory().getCaption()), 580,320,fgs);
		}
		String str = String.format("%d%%", Configuration.getInstance().getSpentBudgetParcent(this.total));
		Paint p = budgetFgEnough;
		Bitmap cart = this.carts[0];
		switch(Configuration.getInstance().getBudgetState(this.total)){
		case Empty: break;
		case Enough:
		case Warning:
			cart = this.carts[1];
			p = budgetFgEnough; break;
		case Crisis:
			cart = this.carts[2];
			p = budgetFgCrisis; break;
		case Over:
		case Downfall:
		case Zimbabwe:
			cart = this.carts[3];
			p = budgetFgOver; break;
		}
		p.getTextBounds(str, 0, str.length(), tmp);
		canvas.drawBitmap(cart, 800, 120, icon);
		canvas.drawText(str, 890 - tmp.width() / 2, 230,p);
	}


	private int getTotalAmount(){
		int c = 0;
		if(this.receipt == null) return 0;
		for(ReceiptRow r : this.receipt){
			c += r.getAmount();
		}
		return c;
	}
	
	public void incrementAmount(){
		this.setAmount(this.amount+1);
		this.invalidate();
	}
	public void decrementAmount(){
		this.setAmount(this.amount-1);
		this.invalidate();
	}
	public void clear(){
		this.price = 0;
		this.amount = 1;
		this.discount = 0;
		this.reduction = 0;
		this.invalidate();
	}
	public void resetAll(){
		this.price = 0;
		this.amount = 1;
		this.total = 0;
		this.discount = 0;
		this.reduction = 0;
		this.receipt.clear();

	}
	public void pushNumber(int n){
		int tmp = this.price * 10 + n;
		if(tmp < 100000){
			this.price = tmp;
		}
		this.invalidate();
	}
	public void popNumber(){
		this.price /= 10;
		this.invalidate();
	}
	public void enter(){
		if(getPrice() == 0) return ;
		ReceiptRow r = new ReceiptRow(this.price, this.amount, this.discount, this.reduction,
				this.getCategory(), this.tax, this.includeTax);
		this.total += r.getTotalWithinTax();
		this.price = 0;
		this.amount = 1;
		this.discount = 0;
		this.reduction = 0;
		this.receipt.push(r);
		this.invalidate();
	}
	public void cancel(){
		if(this.receipt.size() > 0){
			this.receipt.pop();
		}
	}
	
	public void resetCategory(){
		this.category = this.categories.getDefaultCategory();
		this.invalidate();
	}

	public void nextCategory(){
		this.category = this.categories.nextCategory(this.category);
		this.invalidate();
	}

	public void prevCategory(){
		this.category = this.categories.prevCategory(this.category);
		this.invalidate();
	}
	
	public void refresh(){
		this.total = 0;
		for(ReceiptRow r : this.receipt){
			this.total += r.getTotalWithinTax();
		}
		this.invalidate();
	}
	
	
	@Override
	public void updatedEntities() {
		this.refresh();
	}
}
