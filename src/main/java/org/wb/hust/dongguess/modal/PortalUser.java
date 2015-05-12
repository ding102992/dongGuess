package org.wb.hust.dongguess.modal;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2015/1/23.
 */
public class PortalUser extends BmobUser {
    private int coins;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
