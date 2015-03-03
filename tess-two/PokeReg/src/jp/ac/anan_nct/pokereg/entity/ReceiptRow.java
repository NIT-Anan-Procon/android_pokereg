package jp.ac.anan_nct.pokereg.entity;

public class ReceiptRow extends Entity<ReceiptRow>{
	public ReceiptRow(int price, int amount, boolean includeTax, Category category, int reduction, int discount){
		this.price = price;
		this.amount = amount;
		this.includeTax = includeTax;
		this.category = category;
		this.reduction = reduction;
		this.discount = discount;
	}
	
	public int getPrice() {
		return price;
	}
	public int getAmount() {
		return amount;
	}
	public boolean isIncludeTax() {
		return includeTax;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
		this.updated();
	}
	public int getDiscount() {
		return discount;
	}
	public int getReduction() {
		return reduction;
	}
	
	private int discount = 0;
	private int reduction = 0;
	private int price = 0;
	private int amount = 0;
	private int tax = 8;
	private boolean includeTax = false;
	private Category category;
	
	public ReceiptRow(int price, int amount, int discount, int reduction,
			Category cat, int tax, boolean includeTax){
		this.price = price;
		this.amount = amount;
		this.tax = tax;
		this.includeTax = includeTax;
		this.category = cat;
		this.discount = discount;
		this.reduction = reduction;
	}

	public int getDiscountedPrice(){
		return Math.max(0, (this.price * (100 - this.discount) / 100 - this.reduction));
	}
	
	public int getTotalWithoutTax(){
		if(this.includeTax){
			return this.getDiscountedPrice() * this.amount * (100 - tax) / 100;
		}
		else{
			return this.getDiscountedPrice() * this.amount;
		}
	}
	public int getTotalWithinTax(){
		if(this.includeTax){
			return this.getDiscountedPrice() * this.amount;
		}
		else{
			return this.getDiscountedPrice() * this.amount * (100 + this.tax) / 100;
		}
	}
	
	@Override
	public String toString(){
		return String.format("¥%-6d %s\n¥%-6d × %2d",
				this.getTotalWithinTax(),
				this.category.getCaption(), 
				this.price, this.amount);
	}
}
