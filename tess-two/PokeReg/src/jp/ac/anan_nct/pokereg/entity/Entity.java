package jp.ac.anan_nct.pokereg.entity;

public class Entity<T extends Entity<T>> {
	public interface EntityObserver<T>{
		void updatedEntity();
	}
	
	private EntityObserver<T> observer;
	private int id;
	private boolean updated = false;
	public boolean isUpdated(){
		return this.updated;
	}
	
	public void setOverser(EntityObserver<T> observer){
		this.observer = observer;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}

	/* Constructors */
	public Entity(){}
	public Entity(int id){
		this.id = id;
	}
	public Entity(int id, EntityObserver<T> observer){
		this.id = id;
		this.observer = observer;
	}
	public Entity(T e){
		this.observer = e.observer;
		this.id = e.id;
	}

	public void updated(){
		// Notify changed to an observer
		if(this.observer != null){
			this.updated = true;
			this.observer.updatedEntity();
			this.updated = false;
		}
	}
}