package com.memory.container;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.memory.db.Proxy;
import com.memory.db.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/19 0019 8:38
 * @Description:
 */
public class IndexFrame {
    public static JFrame jFrame = null;
    public static void init(){
        Font font =new Font("微软雅黑", Font.PLAIN, 16);//设置字体
        Font font1 =new Font("微软雅黑", Font.PLAIN, 12);//设置字体
        jFrame = new JFrame();

        Panel panel = new Panel(new GridLayout(1, 5));
        JLabel label_name=new JLabel("姓名: ");
        label_name.setFont(font);
        panel.add(label_name);

        JLabel label_money=new JLabel("回款: ");
        label_money.setFont(font);
        panel.add(label_money);

        JLabel label_money_sum=new JLabel("总回款: ");
        label_money_sum.setFont(font);
        panel.add(label_money_sum);

        JLabel label_name_parent=new JLabel("上级: ");
        label_name_parent.setFont(font);
        panel.add(label_name_parent);

        Panel panel1 = new Panel(new GridLayout(1, 3, 3,0));
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

        DefaultMutableTreeNode top = new DefaultMutableTreeNode();
        Proxy top_proxy = new Proxy();
        top_proxy.setId("");
        top_proxy.setName("公司管理");
        top_proxy.setMoney(0.0);
        top_proxy.setMoneySum(0.0);
        top_proxy.setCount(0);
        top_proxy.setParent("");
        top_proxy.setParentName("");

        top.setUserObject(top_proxy);

        initTree(top, "", Utils.getJsonArray());
        JTree tree = new JTree(top);
        tree.setFont(font);
        expandAll(tree, new TreePath(top), true);

        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane jScrollPane = new JScrollPane(tree, v, h);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());

        jFrame.add(panel, BorderLayout.NORTH);
        jFrame.add(jScrollPane, BorderLayout.CENTER);

        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                    .getLastSelectedPathComponent();
            if(node != null){
                Proxy proxy = (Proxy)node.getUserObject();
                label_name.setText("姓名："+proxy.getName());
                label_money.setText("回款："+proxy.getMoney());
                label_money_sum.setText("总回款："+proxy.getMoneySum());
                label_name_parent.setText("上级："+proxy.getParentName());

                Utils.setProxy(proxy);
            }
        });
        btn_add.addActionListener(e -> {
            if(Utils.getProxy()!=null){
                //弹层，添加
                 ModelFrame.init("add", Utils.getProxy());
            }else{

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
                jFrame = null;
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
    private static void initTree(DefaultMutableTreeNode parentNode, String parent, JSONArray array){
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
                proxy.setMoney(obj.getDouble("money"));
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
        jFrame.setVisible(false);// 本窗口隐藏,
        jFrame.dispose();//本窗口销毁,释放内存资源
        jFrame = null;
        init();
    }
}