package jp.ac.anan_nct.smaoni_ver3.model;

/**
 * Created by skriulle on 2015/02/15.
 */
public class GameData {

    public static int gridNum;
    public static int playerNum;

    public GameData(){
        gridNum = 8;
        playerNum = 4;
    }
    public GameData(GameData gd){
        gridNum = gd.gridNum;
        playerNum = gd.playerNum;
    }
}
