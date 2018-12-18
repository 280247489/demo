package com.memory.container;

import com.alibaba.fastjson.JSONObject;
import com.memory.db.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/18 0018 14:41
 * @Description:
 */
public class MainJFrame {
    public static void init(){
        final JFrame jf = new JFrame("代理加密系统");
        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        for (int i = 0; i < Utils.getJsonArray().size(); i++) {
            JSONObject object = Utils.getJsonArray().getJSONObject(i);
            JButton btn = new JButton(object.getString("name")+" [+]");
            panel.add(btn);
        }
        /*JButton btn = new JButton("Show New Window");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 点击按钮, 显示新的一个窗口
                JOptionPane.showMessageDialog(null, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
            }
        });
        panel.add(btn);*/

        jf.setContentPane(panel);
        jf.setVisible(true);
    }
}
