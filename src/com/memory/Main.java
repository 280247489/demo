package com.memory;

import com.memory.container.LoginFrame;
import com.memory.db.Utils;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/18 0018 14:17
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        try {
            Utils.read2System();
            if (Utils.lock != null && Utils.lock.isValid()) {
                System.out.println("222");
                LoginFrame.init();
            }else{
                System.out.println("1111");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
