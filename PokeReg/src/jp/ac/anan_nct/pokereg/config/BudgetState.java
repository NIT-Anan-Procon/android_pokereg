package jp.ac.anan_nct.pokereg.config;

public enum BudgetState {
	Empty,
	Enough,
	Warning,
	Crisis,
	Over,
	Downfall,
	Zimbabwe;
	
	
	public static BudgetState get(int percent){
		if(percent == 0) return Empty;
		if(percent <= 60) return Enough;
		if(percent <= 80) return Warning;
		if(percent <= 100) return Crisis;
		if(percent <= 150) return Over;
		if(percent <= 200) return Downfall;
		return Zimbabwe;
	}
}
