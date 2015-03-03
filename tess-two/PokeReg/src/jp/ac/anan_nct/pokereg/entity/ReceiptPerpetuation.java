package jp.ac.anan_nct.pokereg.entity;
import java.util.ArrayList;

public interface ReceiptPerpetuation {
	int save(Receipt receipt);
	Receipt get(int id);
	ArrayList<Receipt> select(Condition<Receipt> condt);
	void delete(int id);
}
