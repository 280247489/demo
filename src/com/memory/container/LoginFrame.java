package com.memory.container;


import javax.swing.*;
import java.awt.*;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/18 0018 14:41
 * @Description:
 */
public class LoginFrame {
    public static void init(){
        Font font =new Font("微软雅黑", Font.PLAIN, 16);//设置字体
        JFrame jf = new JFrame("代理加密系统");
        jf.setResizable(false);
        jf.setSize(400, 300);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);


        JLabel label_loginname=new JLabel("用户名: ");
        label_loginname.setBounds(20, 50, 60, 50);
        label_loginname.setFont(font);
        panel.add(label_loginname);

        JTextField txt_loginname=new JTextField();
        txt_loginname.setBounds(100, 50, 250, 50);
        txt_loginname.setFont(font);
        panel.add(txt_loginname);


        JLabel label_password=new JLabel("密码: ");
        label_password.setBounds(20, 120, 60, 50);
        label_password.setFont(font);
        panel.add(label_password);

        JPasswordField txt_pasword=new JPasswordField();
        txt_pasword.setBounds(100, 120, 250, 50);
        txt_pasword.setFont(font);
        panel.add(txt_pasword);


        JButton btn_login=new JButton("登 陆");
        btn_login.setBounds(100, 200, 100, 50);
        btn_login.setFont(font);
        panel.add(btn_login);


        JButton btn_exit=new JButton("退 出");
        btn_exit.setBounds(250, 200, 100, 50);
        btn_exit.setFont(font);
        panel.add(btn_exit);

        btn_login.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "账号："+txt_loginname.getText()+ " |密码："+new String(txt_pasword.getPassword()),
                    "提示框",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btn_exit.addActionListener(e -> {
            System.exit(0);
        });

        jf.setContentPane(panel);
        jf.setVisible(true);
    }
}
