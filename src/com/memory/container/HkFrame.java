package com.memory.container;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.memory.db.Utils;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/19 0019 8:38
 * @Description:
 */
public class HkFrame {
    public static JFrame jFrame = null;
    public static Object[][] tableData;
    public static int r;
    public static int c;
    public static void init(){
        if(jFrame==null){
            jFrame = new JFrame();
            Font font =new Font("微软雅黑", Font.PLAIN, 16);//设置字体

            JScrollPane jScrollPane = new JScrollPane();
            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jFrame.setLayout(new BorderLayout());

            Object[] columnTitle = {"姓名" , Utils.getCurrentMonth()+" 月-回款", "编辑-回款"};

            int count = Utils.getProxy().getCount();
            boolean flag = false;
            int index = 0;
            if(flag = !"".equals(Utils.getProxy().getId())){
                index = 1;
                count+=1;
            }
            tableData = new Object[count][];
            if(flag){
                tableData[0] = new Object[]{Utils.getProxy().getName(), Utils.getProxy().getMoney(), Utils.getProxy().getMoney(), Utils.getProxy().getId()};
            }
            initTableData(index, tableData, Utils.getProxy().getId(), Utils.getJsonArray());

            JTable jTable = new JTable();
            JTableHeader head = jTable.getTableHeader(); // 创建表格标题对象
            head.setSize(head.getWidth(), 22);// 设置表头大小
            head.setFont(font);// 设置表格字体
            jTable.setFont(font);
            jTable.setRowHeight(22);

            jScrollPane.setViewportView(jTable);

            jFrame.add(jScrollPane, BorderLayout.CENTER);
            //jFrame.pack();
            jFrame.setSize(800, 600);
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
            DefaultTableModel newTableModel = new DefaultTableModel(tableData, columnTitle){
                @Override
                public boolean isCellEditable(int row,int column){
                    if(column==2){
                        r = row;
                        c = column;
                        System.out.println("isCellEditable: r="+row+", c="+c);
                        return true;
                    }else{
                        return false;
                    }
                }
            };
            jTable.setModel(newTableModel);

            jTable.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {

                }
            });

            jFrame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {
                    jFrame = null;
                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
        }else{
            jFrame.setFocusable(true);
        }
    }

    private static int initTableData(int index, Object[][] tableData, String parentId, JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if(parentId.equals(obj.getString("parent"))){
                //初始化数据
                double money = 0.0;
                if(obj.containsKey("monthMoney")){
                    JSONObject monthMoneyObj = obj.getJSONObject("monthMoney");
                    if(monthMoneyObj.containsKey(""+Utils.getCurrentMonth())){
                        money = monthMoneyObj.getDouble(""+Utils.getCurrentMonth());
                    }
                }
                tableData[index] = new Object[]{obj.getString("name"), money, money, obj.getString("id")};
                index++;
                if(Utils.hasNode(obj.getString("id"))){
                    //内置操作节点数据
                    index = initTableData(index, tableData, obj.getString("id"), array);
                }
            }
        }
        return index;
    }
}
