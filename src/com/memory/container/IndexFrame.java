package com.memory.container;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.memory.db.Proxy;
import com.memory.db.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/19 0019 8:38
 * @Description:
 */
public class IndexFrame {
    public static DefaultMutableTreeNode root = null;
    public static DefaultMutableTreeNode gongsi = null;
    public static DefaultTreeModel dt = null;
    public static JTree tree = null;
    public static void init(){
        JFrame jFrame = new JFrame();
        Font font =new Font("微软雅黑", Font.PLAIN, 16);//设置字体
        Font font1 =new Font("微软雅黑", Font.PLAIN, 12);//设置字体

        Panel panel = new Panel(new GridLayout(1, 5));
        JLabel label_name=new JLabel("姓名: ");
        label_name.setFont(font);
        panel.add(label_name);

        JLabel label_money=new JLabel("个人回款: ");
        label_money.setFont(font);
        panel.add(label_money);

        JLabel label_money_sum=new JLabel("团队回款: ");
        label_money_sum.setFont(font);
        panel.add(label_money_sum);

        Panel panel1 = new Panel(new GridLayout(1, 5, 3,0));

        JComboBox comboBox=new JComboBox();
        for (int i = Utils.getBeginMonth(); i <= Utils.getEndMonth(); i++) {
            comboBox.addItem(i);
        }
        comboBox.setSelectedItem(Utils.getCurrentMonth());
        panel1.add(comboBox);

        JButton btn_hk=new JButton("款");
        btn_hk.setFont(font1);
        panel1.add(btn_hk);

        JButton btn_add=new JButton("增");
        btn_add.setFont(font1);
        panel1.add(btn_add);

        JButton btn_upd=new JButton("修");
        btn_upd.setFont(font1);
        panel1.add(btn_upd);

        JButton btn_del=new JButton("删");
        btn_del.setFont(font1);
        panel1.add(btn_del);

        panel.add(panel1);

        //核心
        gongsi = initTree(null, "", Utils.getJsonArray());

        root = new DefaultMutableTreeNode();
        root.add(gongsi);
        dt = new DefaultTreeModel(root);
        tree = new JTree(dt);
        tree.setFont(font);

        expandAll(tree, new TreePath(root), true);

        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane jScrollPane = new JScrollPane(tree, v, h);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());

        jFrame.add(panel, BorderLayout.NORTH);
        jFrame.add(jScrollPane, BorderLayout.CENTER);

        jFrame.setSize(1000, 800);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                    .getLastSelectedPathComponent();
            if(node != null){
                try {
                    Proxy proxy = (Proxy)node.getUserObject();
                    label_name.setText("姓名："+proxy.getName());
                    label_money.setText("回款："+proxy.getMoney());
                    label_money_sum.setText("总回款："+proxy.getMoneySum());

                    Utils.setProxy(proxy);
                } catch (Exception e1) {
                }
            }
        });
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                     Utils.setCurrentMonth((int)comboBox.getSelectedItem());
                    reload();
                }
            }
        });
        btn_hk.addActionListener(e -> {
            if(Utils.getProxy()!=null){
                //弹层，添加
                HkFrame.init();
            }
        });
        btn_add.addActionListener(e -> {
            if(Utils.getProxy()!=null){
                //弹层，添加
                 ModelFrame.init("add", Utils.getProxy());
            }
        });
        btn_upd.addActionListener(e -> {
            if(Utils.getProxy()!=null&&!"".equals(Utils.getProxy().getId())){
                //弹层，修改
                ModelFrame.init("upd", Utils.getProxy());
            }
        });
        btn_del.addActionListener(e -> {
            if(Utils.getProxy()!=null&&!"".equals(Utils.getProxy().getId())&&ModelFrame.jFrame==null){
                int flag = JOptionPane.showConfirmDialog(null,
                        "确认删除<"+Utils.getProxy().getName()+"> ?",
                        "删 除",
                        JOptionPane.YES_NO_OPTION);
                if(flag==0){
                    if(Utils.getProxy().getCount()==0){
                        JSONObject object = Utils.getObj(Utils.getProxy().getId());
                        Utils.getJsonArray().remove(object);
                        Utils.write2LocalDB();
                        reload();
                    }else{
                        //Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null,
                                "请先删除子节点",
                                "错 误",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
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
                Utils.write2LocalDB();
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
    }

    /**
     * 渲染节点核心方法-标记子节点数量
     * @param parentNode
     * @param parent
     * @param array
     * @return
     */
    private static DefaultMutableTreeNode initTree(DefaultMutableTreeNode parentNode, String parent, JSONArray array){
        if(parentNode == null){
            parentNode = new DefaultMutableTreeNode();
            Proxy top_proxy = new Proxy();
            top_proxy.setId("");
            top_proxy.setName("公司管理");
            top_proxy.setMoney(0.0);
            top_proxy.setMoneySum(0.0);
            top_proxy.setCount(0);
            top_proxy.setParent("");
            top_proxy.setParentName("");

            parentNode.setUserObject(top_proxy);
        }
        int parent_count = 0;
        double parent_moneySum = 0;
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if(parent.equals(obj.getString("parent"))){
                parent_count++;

                DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                //初始化数据
                Proxy proxy = new Proxy();
                proxy.setId(obj.getString("id"));
                proxy.setName(obj.getString("name"));
                double money = 0.0;
                if(obj.containsKey("monthMoney")){
                    JSONObject monthMoneyObj = obj.getJSONObject("monthMoney");
                    if(monthMoneyObj.containsKey(""+Utils.getCurrentMonth())){
                        money = monthMoneyObj.getDouble(""+Utils.getCurrentMonth());
                    }
                }
                proxy.setMoney(money);
                proxy.setMoneySum(proxy.getMoney());
                proxy.setCount(0);
                proxy.setParent(obj.getString("parent"));
                proxy.setParentName(parentNode.getUserObject().toString());

                node.setUserObject(proxy);

                parentNode.add(node);

                if(Utils.hasNode(obj.getString("id"))){
                    //内置操作节点数据
                    initTree(node, obj.getString("id"), array);
                }
                //统计for循环内操作后的节点数据汇总，提供父节点使用
                parent_count+=proxy.getCount();
                parent_moneySum+=proxy.getMoneySum();
            }
        }
        //展示父节点
        Proxy proxy = (Proxy) parentNode.getUserObject();
        proxy.setCount(proxy.getCount()+parent_count);
        proxy.setMoneySum(proxy.getMoneySum()+parent_moneySum);
        return parentNode;
    }

    /**
     * 展开，收起节点
     * @param tree
     * @param parent
     * @param expand
     */
    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0)
        {
            for (Enumeration e = node.children(); e.hasMoreElements();)
            {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand)
        {
            tree.expandPath(parent);
        } else
        {
            tree.collapsePath(parent);
        }
    }
    public static void reload(){
        //核心
        root.removeAllChildren();
        gongsi = initTree(null, "", Utils.getJsonArray());
        root.add(gongsi);
        dt.reload();
        expandAll(tree, new TreePath(root), true);
    }
}
