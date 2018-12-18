package com.memory;

import com.memory.container.MainJFrame;
import com.memory.db.Utils;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/18 0018 14:17
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        Utils.read2System();
        MainJFrame.init();
        //Utils.write2LocalDB();
    }
}
