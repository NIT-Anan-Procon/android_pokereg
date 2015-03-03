package jp.ac.anan_nct.pokereg.entity;

public class CategorySet extends Entities<CategorySet, Category> {
	private int defaultCategoryIndex;
	public CategorySet(){
		this.defaultCategoryIndex = 0;
	}
	
	public void setDefaultCategoryIndex(int index){
		this.defaultCategoryIndex = index;
	}
	public int getDefaultCategoryIndex(){
		return this.defaultCategoryIndex;
	}
	public Category getDefaultCategory(){
		return (this.defaultCategoryIndex < this.size()) ?
				this.getByIndex(this.defaultCategoryIndex) : null;
	}
	public Category nextCategory(Category c){
		CategorySet cs = Data.getInstance().getCategories();
		int size = cs.size();
		if(size == 0) return null;
		int index = cs.getIndex(c);
		return cs.getByIndex((index + 1) % size);
	}
	public Category prevCategory(Category c){
		CategorySet cs = Data.getInstance().getCategories();
		int size = cs.size();
		if(size == 0) return null;
		int index = cs.getIndex(c);
		return cs.getByIndex((size + index - 1) % size);
	}

}