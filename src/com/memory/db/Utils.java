package com.memory.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.UUID;

/**
 * @Auther: cui.Memory
 * @Date: 2018/12/18 0018 15:07
 * @Description:
 */
public class Utils {
    public static final String loginname = "root";
    public static final String password = "rootroot";

    public static FileLock lock = null;

    private static Proxy proxy = null;

    private static JSONArray jsonArray = null;

    public static JSONArray getJsonArray() {
        return jsonArray;
    }
    public static Proxy getProxy() {
        return proxy;
    }
    public static void setProxy(Proxy proxy) {
        Utils.proxy = proxy;
    }

    /***************************************************************************/

    public static JSONObject createObj(String name, double money, String parent){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", Utils.getShortUuid());
        jsonObject.put("name", name);
        jsonObject.put("money", money);
        jsonObject.put("parent", parent);

        jsonArray.add(jsonObject);

        return jsonObject;
    }

    public static JSONObject getObj(String id){
        JSONObject jsonObject = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            if(id.equals(jsonArray.getJSONObject(i).getString("id"))){
                jsonObject = jsonArray.getJSONObject(i);
                break;
            }
        }
        return jsonObject;
    }

    public static boolean hasNode(String id){
        boolean flag = false;
        for (int i = 0; i < jsonArray.size(); i++) {
            if(id.equals(jsonArray.getJSONObject(i).getString("parent"))){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /***************************************************************************/

    private static final String dbpath = "src/com/memory/db/local.db";

    public static void read2System() throws Exception{
        File file = new File(dbpath);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        FileChannel fc = raf.getChannel();

        try{
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            StringBuffer stringBuffer = new StringBuffer("");
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            if(!"".equals(stringBuffer.toString())){
                jsonArray = JSONArray.parseArray(stringBuffer.toString());
            }else{
                jsonArray = new JSONArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock  = fc.tryLock();
    }

    public static void write2LocalDB(){
        File file = new File(dbpath);
        String content = jsonArray.toJSONString();
        try{
            FileWriter fileWriter=new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /***************************************************************************/

    private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };
    public static String getShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
