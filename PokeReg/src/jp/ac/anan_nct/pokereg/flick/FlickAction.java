package jp.ac.anan_nct.pokereg.flick;

public enum FlickAction{
	NOP(0, "何もしない"),
    OPEN_CART(1, "カートを開く"),
    RESET_PRICE(10, "単価をリセットする"),
    ADD_PRICE(11, "単価に加算する", "単価に@加算する", "加算量", 1, 99999),
    SUB_PRICE(12, "単価に減算する", "単価に@減算する", "減算量", 1, 99999),
    PUSH_PRICE(13, "単価にプッシュする", "単価に@をプッシュする", "値", 0, 9),
    POP_PRICE(14, "単価からポップする"),
    SET_PRICE(15, "単価をセットする", "単価を@にセットする", "単価", 0, 99999),
    ENTER(16, "入力を確定する"),
    CLEAR(17, "入力をクリアする"),

    RESET_DISCOUNT(20, "割引率をリセットする"),
    ADD_DISCOUNT(21, "割引率を加算する", "割引率に@%加算する", "加算割引%", 1, 99),
    SUB_DISCOUNT(22, "割引率を減算する", "割引率を@%減算する", "減算割引%", 1, 99),
    SET_DISCOUNT(23, "割引率をセットする", "割引率を@%にセットする", "割引%", 0, 99),

    RESET_REDUCTION(30, "値引額をリセットする"),
    ADD_REDUCTION(31, "値引額を加算する", "値引額に¥@加算する", "加算値引額", 1, 99999),
    SUB_REDUCTION(32, "値引額を減算する", "値引額を¥@減算する", "減算値引額", 1, 99999),
    SET_REDUCTION(33, "値引額をセットする", "値引額を¥@にセットする", "値引額", 0, 99999),
    
    RESET_AMOUNT(40, "数量をリセットする"),
    ADD_AMOUNT(41, "数量を加算する", "数量に@加算する", "加算数量", 1, 99),
    SUB_AMOUNT(42, "数量を減算する", "数量を@減算する", "減算数量", 1, 99),
    SET_AMOUNT(43, "数量をセットする", "数量を@にセットする", "数量", 1, 99),
    RESET_TOTAL(50, "カートをリセットする"),
    REVERT_CART(60, "直前の商品を取り消す"),
    RESET_CATEGORY(70, "カテゴリをリセット"),
    NEXT_CATEGORY(71, "次のカテゴリへ"),
    PREV_CATEGORY(72, "前のカテゴリへ"),
    SET_CATEGORY(73, "カテゴリをセットする", "カテゴリを[@]にセットする", "カテゴリ", ArgType.Category),
    TOGGLE_INCLUDING_TAX(80, "税込み/税抜き 切り替え"),
    SET_INCLUDING_TAX(81, "税込み入力にする"),
    SET_UNINCLUDING_TAX(82, "税抜き入力にする"),
    TOGGLE_USING_CAMERA(90, "数値認識使用/不使用 切り替え"),
    SET_USING_CAMERA(91, "数値認識を使用する"),
    SET_UNUSING_CAMERA(92, "数値認識を使用しない"),
    TOGGLE_HAND(100, "左手/右手 切り替え"),
    SET_LEFT_HAND(101, "右手持ちにする"),
    SET_RIGHT_HAND(102, "左手持ちにする"),	
    TOGGLE_KEY_SET_PATTERN(110, "キーセット切り替え"),
    SET_BOTTOMUP_KEY_SET_PATTERN(111, "キーセットをボトムアップにする"),
    SET_TOPDOWN_KEY_SET_PATTERN(112, "キーセットをトップダウンにする"),	

    CAPTURE(120, "数値認識を行う"),
    ECHO_MY_LUCK(1100, "占いをする");
	
	public enum ArgType{
		Num, Category;
	}
    private int id;
    private String label;
    private String caption;
    private boolean useArg;
    private ArgType type;
    private String argCaption;
    private int min;
    private int max;
    private FlickAction(int id, String label) {
        this.id = id;
        this.label = label;
        this.useArg = false;
        this.caption = label;
    }
    private FlickAction(int id, String label, String caption, String argCaption, ArgType type) {
        this.id = id;
        this.label = label;
        this.useArg = true;
        this.type = type;
        this.caption = caption;
        this.argCaption = argCaption;
        this.min = 0;
        this.max = 9;
    }
    private FlickAction(int id, String label, String caption, String argCaption, int min, int max) {
        this.id = id;
        this.label = label;
        this.useArg = true;
        this.type = ArgType.Num;
        this.caption = caption;
        this.argCaption = argCaption;
        this.min = min;
        this.max = max;
    }
    public int getId(){
    	return this.id;
    }
	public String getLabel(){
		return this.label;
	}
	public boolean isUseArg(){
		return this.useArg;
	}
	public ArgType getType(){
		return this.type;
	}
	public String getArgCaption(){
		return this.argCaption;
	}
	public String getCaption(){
		return this.caption;
	}
    public int getMin(){
    	return this.min;
    }
    public int getMax(){
    	return this.max;
    }
    public static FlickAction getFlickAction(int aid){
    	for(FlickAction f : FlickAction.values()){
    		if(f.getId() == aid) return f;
    	}
    	return null;
    }
    @Override
    public String toString(){
    	return this.label;
    }
}