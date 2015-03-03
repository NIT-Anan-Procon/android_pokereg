package jp.ac.anan_nct.pokereg.entity;


public class FlickSet extends Entities<FlickSet, Flick> {

	public int findActionId(int[] indexes){
		Flick f = this.find(indexes);
		return f!=null ? f.getActionId() : -1;
	}
	public int findArg(int[] indexes){
		Flick f = this.find(indexes);
		return f!=null ? f.getArg() : 0;
	}
	public void add(int[] indexes, int actionId, int arg){
		this.add(new Flick(indexes, actionId, arg));
	}
	
	public Flick find(int[] indexes){
		for(Flick f : this){
			if(f.isSameIndexes(indexes)){
				return f;
			}
		}
		return null;
	}
}
