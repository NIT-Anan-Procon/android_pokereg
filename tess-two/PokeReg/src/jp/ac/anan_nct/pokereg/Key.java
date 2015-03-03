package jp.ac.anan_nct.pokereg;

public enum Key{
	Enter("Ent."),
	Clear("C"),
	Number0("0"),
	Number1("1"),
	Number2("2"),
	Number3("3"),
	Number4("4"),
	Number5("5"),
	Number6("6"),
	Number7("7"),
	Number8("8"),
	Number9("9");
	
	private String label;
	private Key(String label){
		this.label = label;
	}
	@Override
	public String toString(){
		return this.label;
	}
	
}