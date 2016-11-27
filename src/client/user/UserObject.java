package client.user;

/**
 * 用户类
 */
public class UserObject implements Comparable{

    private String name; //用户名
    private boolean status; //状态，true表示在线，false表示离线

    /**
     * 构造函数
     * @param name
     * @param status
     */
    public UserObject(String name, boolean status){
        this.name = name;
        this.status = status;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName(){
        return name;
    }

    public boolean getStatus(){
        return status;
    }

    /**
     * 重写compareTo方法
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        UserObject user = (UserObject)o;
        if(this.status == true && user.getStatus() == false)
            return -1;
        else if(this.status == false && user.getStatus() == true)
            return 1;
        else
            return this.name.compareTo(user.toString());
    }
}
