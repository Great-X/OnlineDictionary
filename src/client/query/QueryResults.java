package client.query;

import java.util.*;

public class QueryResults{
    //搜索结果集
    private List<QueryResult> results = new ArrayList<>();

    public QueryResults(){
        results.add(new QueryResult("jinshan"));
        results.add(new QueryResult("youdao"));
        results.add(new QueryResult("biying"));
    }

    /**
     * 返回该工具搜索出得结果
     * @param tool
     * @return
     */
    public QueryResult getResult(String tool){
        for(QueryResult qr: results)
            if(qr.toString() == tool)
                return qr;
        return null;
    }
    public QueryResult getResult(int i){
        return results.get(i);
    }

    /**
     * 设置搜索结果内容
     * @param map
     */
    public void setResultsContent(HashMap<String, String> map){
        for(Map.Entry<String, String> entry: map.entrySet()){
            getResult(entry.getKey()).setContent(entry.getValue());
        }
    }

    /**
     * 设置搜索结果点赞数
     * @param list
     */
    public void setResultsFavourNum(List<Integer> list){
        getResult("jinshan").setFavourNum(list.get(0));
        getResult("youdao").setFavourNum(list.get(1));
        getResult("biying").setFavourNum(list.get(2));
    }

    /**
     * 设置用户对搜索结果的点赞情况
     * @param list
     */
    public void setResultsUserFavour(List<Boolean> list){
        getResult("jinshan").setUserFavour(list.get(0));
        getResult("youdao").setUserFavour(list.get(1));
        getResult("biying").setUserFavour(list.get(2));
    }

    /**
     * 根据点赞数排序
     */
    public void sort(){
        results.sort(new Comparator<QueryResult>() {
            @Override
            public int compare(QueryResult o1, QueryResult o2) {
                if(o1.getUserFavour() && !o2.getUserFavour())
                    return -1;
                else if(!o1.getUserFavour() && o2.getUserFavour())
                    return 1;
                else
                    return o2.getFavourNum() - o1.getFavourNum();
            }
        });
    }


}



class QueryResult {
    //搜索工具
    private String tool = "";
    //点赞数
    private int favourNum = 0;
    //搜索结果
    private String content = "";
    //该用户对当前单词的点赞情况
    private Boolean userFavour = false;

    /**
     * 构造函数
     * @param tool 搜索用的工具
     */
    public QueryResult(String tool){
        this.tool = tool;
    }

    @Override
    public String toString(){
        return tool;
    }

    public String getTool(){
        return tool;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public void setFavourNum(int favourNum){
        this.favourNum = favourNum;
    }

    public int getFavourNum(){
        return favourNum;
    }

    public void setUserFavour(boolean userFavour){
        this.userFavour = userFavour;
    }

    public boolean getUserFavour(){
        return userFavour;
    }
}
