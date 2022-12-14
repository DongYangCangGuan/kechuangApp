package com.cloud.servicewechat.utils.weixin.json;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by psl on 2018-04-03.
 */
public class JsonTool {
    private static JSONObject gson=new JSONObject();

    //转换对象为JSON字符串
    public static String ToJson(Object o){
        String result = gson.toJSONString(o);
        return result;
    }


    //转换JSON字符串为对象
    public static Object JsonToObject(String jsonStr,Class clazz)
    {
        Object o =JSONObject.parseObject(jsonStr, clazz.getClass());
        String json2 = gson.toJSONString(o);
        System.out.println("Json字符串 = " + json2);
        return null;
    }

    /**
     * json转map类型
     * @param jsonStr
     * @return
     */
    public Map<String,String> JsonToMap(String jsonStr)
    {
        Map<String,String> map = JSONObject.parseObject(jsonStr, Map.class);
        System.out.println(map.get("ent_name"));
        return map;
    }

    public static void main(String[] args) {
        JsonTool j = new JsonTool();
        String json = "{\"data\":[{\"friend\":[{\"address\":\"广州\",\"name\":\"好友1\"},{\"address\":\"深圳\",\"name\":\"好友2\"}],\"username\":\"包青天\"},{\"friend\":[],\"username\":\"bqt\"}],\"num\":2}";
        j.JsonToObject(json,GsonBean.class);
    }
    class GsonBean{

        public GsonBean() {
        }
        public GsonBean(int num, ArrayList<Account> data) {
            super();
            this.num = num;
            this.data = data;
        }
        public int num;
        public ArrayList<Account> data;
        /**data数组里的对象*/
        public class Account {
            public String username;
            public ArrayList<Person> friend;
            @Override
            public String toString() {
                return "账户【username=" + username + "，friend=" + friend + "】";
            }
            public Account(String username, ArrayList<Person> friend) {
                super();
                this.username = username;
                this.friend = friend;
            }
        }
        @Override
        public String toString() {
            return "Gson 【num=" + num + "，data=" + data + "】";
        }
    }

    class Person{
        //不要求一定有get、set方法，也不要求一定有无参构造方法，甚至不要求其成员是public还是private
        //但要求所有字段名必须和json字符串中的一致
        public String name;
        public String address;
        public Person(String name, String address) {
            this.name = name;
            this.address = address;
        }
        @Override
        public String toString() {
            return "name=" + name + " & " + "address=" + address;
        }
    }
}
