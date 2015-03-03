package jp.ac.anan_nct.pokereg.entity;

import jp.ac.anan_nct.pokereg.Key;
import jp.ac.anan_nct.pokereg.KeySetPattern;
import jp.ac.anan_nct.pokereg.flick.FlickAction;

public class Flick extends Entity<Flick> {
	private int[] indexes;
	private int actionId;
	private int arg
	;
	public int getArg() {
		return arg;
	}
	public int getActionId() {
		return actionId;
	}
	public int[] getIndexes() {
		return indexes;
	}
	public Key[] getKeys(KeySetPattern pattern){
		Key[] keys = new Key[this.indexes.length];
		for(int i=0; i<this.indexes.length; i++){
			keys[i] = pattern.getKeyFromIndex(this.indexes[i]);
		}
		return keys;
	}
	public Flick(int[] indexes, int actionId, int arg){
		this.indexes = indexes;
		this.actionId = actionId;
		this.arg = arg;
	}
	public Flick(Key[] keys, KeySetPattern pattern, int actionId, int arg){
		this.indexes = new int[keys.length];
		for(int i=0; i<keys.length; i++){
			this.indexes[i] = pattern.getIndexFromKey(keys[i]);
		}
		this.actionId = actionId;
		this.arg = arg;
	}
	
	public boolean isSameIndexes(int[] indexes){
		if(this.indexes.length != indexes.length) return false;
		for(int i=0; i<this.indexes.length; i++){
			if(this.indexes[i] != indexes[i]) return false;
		}
		return true;
	}

	public String getCaption(){
		FlickAction action = FlickAction.getFlickAction(this.actionId);
		if(!action.isUseArg()) return action.getCaption();
		String replacement = "";
		switch(action.getType()){
		case Num:
			replacement = String.valueOf(this.arg);
			break;
		case Category:
			Category c = Data.getInstance().getCategories().getById(this.arg);
			replacement = (c != null) ? c.toString() : "???";
			break;
		}
		return action.getCaption().replaceAll("@", replacement);
	}

}
