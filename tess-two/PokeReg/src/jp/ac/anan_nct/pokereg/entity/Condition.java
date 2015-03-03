package jp.ac.anan_nct.pokereg.entity;

public interface Condition<T> {
	boolean useString();
	boolean where(T r);
	String where();
}
