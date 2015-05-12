package org.wb.hust.dongguess.data;

/**
 * Created by Administrator on 2014/12/27.
 */
public class Const {

    public static final int INDEX_FILE_NAME = 0;
    public static final int INDEX_SONG_NAME = 1;
    public static final int TOTAL_COINS = 1000;
    public static final String SONG_INFO[][] = {
            {"__00000.m4a","征服"},
            {"__00001.m4a", "童话"},
            {"__00002.m4a", "同桌的你"},
            {"__00003.m4a", "七里香"},
            {"__00004.m4a", "传奇"},
            {"__00005.m4a", "大海"},
            {"__00006.m4a", "后来"},
            {"__00007.m4a", "你的背包"},
            {"__00008.m4a", "再见"},
            {"__00009.m4a", "老男孩"},
            {"__00010.m4a", "龙的传人"}
    };

    /**
     * 存放游戏的数据文件名称
     */
    public static final String DATA_FILE = "game.dat";

    /**
     * 在数据文件中获取当前关卡的索引
     */
    public static final int INDEX_DATA_CURRENT_STAGE = 0;

    /**
     * 在数据文件中获取当前的金币数的索引
     */
    public static final int INDEX_DATA_CURRENT_CONIS = 1;

    /**
     * Bmob Application key
     */
    public static final String BMOB_APPLICATION_KEY = "5c13bac935f0c11eb5aa762c0f43fd6c";


}
