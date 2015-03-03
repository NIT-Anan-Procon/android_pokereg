package jp.ac.anan_nct.pokereg.entity;

import java.util.Iterator;
import java.util.LinkedList;

import jp.ac.anan_nct.pokereg.entity.Entity.EntityObserver;

public class Entities<U extends Entities<U, T>, T extends Entity<T>>
		extends Entity<U>
		implements EntityObserver<T>, Iterable<T> {
	
	private LinkedList<T> data = new LinkedList<T>();
	private LinkedList<EntitiesObserver> observers = new LinkedList<EntitiesObserver>();
	private int nextId = 1;
	
	public int size(){
		return this.data.size();
	}
	
	public T getByIndex(int location){
		return this.data.get(location);
	}
	public T getById(int id){
		for(T entity : this.data){
			if(entity.getId() == id) return entity;
		}
		return null;
	}
	public int getIndex(T entity){
		int index = 0;
		for(T e : this.data){
			if(entity.equals(e)) return index;
			index++;
		}
		return -1;
	}
	
	public T add(T entity){
		this.data.add(entity);
		entity.setId(this.nextId);
		entity.setOverser(this);
		this.nextId++;
		this.afterAdd(entity);
		this.notifyForChanged();
		return entity;
	}
	public T push(T entity){
		this.data.push(entity);
		entity.setId(this.nextId);
		entity.setOverser(this);
		this.nextId++;
		this.afterAdd(entity);
		this.notifyForChanged();
		return entity;
	}
	public T pop(){
		T entity = this.data.getLast();
		this.beforeRemove(entity);
		this.data.pop();
		this.notifyForChanged();
		return entity;
	}
	
	public T remove(T entity){
		this.data.remove(entity);
		this.notifyForChanged();
		this.beforeRemove(entity);
		return entity;
	}
	public T remove(int location){
		T entity = this.getByIndex(location);
		this.beforeRemove(entity);
		return this.remove(entity);
	}
	public T removeById(int id){
		T entity = this.getById(id);
		this.beforeRemove(entity);
		return this.remove(entity);
	}
	
	public void clear(){
		for(T e : this.data){
			this.beforeRemove(e);
		}
		this.data.clear();
		this.nextId = 1;
		this.notifyForChanged();
	}
	
	public void addOvserver(EntitiesObserver observer){
		this.observers.add(observer);
	}
	protected void notifyForChanged(){
		for(EntitiesObserver ob : this.observers){
			ob.updatedEntities();
		}
	}

	@Override
	public Iterator<T> iterator() {
		return this.data.iterator();
	}

	@Override
	public void updatedEntity() {
		for(T e : this.data){
			if(e.isUpdated()){
				this.afterUpdate(e);
			}
		}
		for(EntitiesObserver o : this.observers){
			o.updatedEntities();
		}
	}
	
	/* Callbacks */
	public void afterAdd(T entity){ }
	public void beforeRemove(T entity){ }
	public void afterUpdate(T entity){ }

}
