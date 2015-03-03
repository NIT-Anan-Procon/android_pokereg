package jp.ac.anan_nct.pokereg.config;

import jp.ac.anan_nct.pokereg.KeySetPattern;

public class Configuration {
	private Configuration(){ }
	private static Configuration instance = new Configuration();
	public static Configuration getInstance(){ return instance; }

	private ComfortableHand comfortableHand = ComfortableHand.LeftHand;
	public ComfortableHand getComfortableHand(){
		return this.comfortableHand;
	}
	public void setComfortableHand(ComfortableHand value){
		this.comfortableHand = value;
	}
	public boolean isRightHandComfortable(){
		return this.comfortableHand == ComfortableHand.RightHand;
	}
	public boolean isLeftHandComfortable(){
		return this.comfortableHand == ComfortableHand.LeftHand;
	}
	public void toggleComfortableHand(){
		this.comfortableHand = this.comfortableHand == ComfortableHand.LeftHand ?
				ComfortableHand.RightHand : ComfortableHand.LeftHand;
	}
	
	// Camera
	private boolean useCamera = true;
	public boolean isUseCamera() {
		return useCamera;
	}
	public void setUseCamera(boolean useCamera) {
		this.useCamera = useCamera;
	}
	private boolean useBinarize = true;
	private int binarizeThreshold = 150;
	public boolean useBinarizeForOCR(){
		return this.useBinarize;
	}
	public int getBinarizeThresholdForOCR(){
		return this.binarizeThreshold;
	}
	public void setUsingBinarizeForOCR(boolean value){
		this.useBinarize = value;
	}
	public void setBinarizeThresholdForOCR(int value){
		this.binarizeThreshold = value;
	}
	
	// Tax
	private boolean includingTax = false;
	private int tax = 8;
	public boolean isIncludingTax() {
		return includingTax;
	}
	public void setIncludingTax(boolean includingTax) {
		this.includingTax = includingTax;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	
	// Flick
	private KeySetPattern keySetPattern = KeySetPattern.BottomUp;
	public KeySetPattern getKeySetPattern() {
		return keySetPattern;
	}
	public void setKeySetPattern(KeySetPattern keySetPattern) {
		this.keySetPattern = keySetPattern;
	}
	
	// Budget
	private int budget = 10000;
	public int getSpentBudgetParcent(int spent){
		return spent * 100 / budget;
	}
	public BudgetState getBudgetState(int spent){
		return BudgetState.get(this.getSpentBudgetParcent(spent));
	}
	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
	
	// Network
	private String proxyHost = "proxy.anan-nct.ac.jp";
	private int proxyPort = 8080;
	private boolean useProxy = false;
	public boolean isUseProxy() {
		return useProxy;
	}
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxy_host) {
		this.proxyHost = proxy_host;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(int proxy_port) {
		this.proxyPort = proxy_port;
	}
}
