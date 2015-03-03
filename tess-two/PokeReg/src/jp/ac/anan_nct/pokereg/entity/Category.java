package jp.ac.anan_nct.pokereg.entity;

public class Category extends Entity<Category>{
	public Category(String caption){
		super();
		this.caption = caption;
	}
	public Category(Category c){
		super(c);
		this.caption = c.caption;
	}
	
	public String getCaption() {
		return caption;
	}
	private String caption;
	@Override
	public String toString(){
		return this.caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof Category){
			Category c = (Category)o;
			return this.caption.equals(c.caption);
		}
		return false;
	}
	@Override
	public int hashCode(){
		return this.caption.hashCode();
	}
}
