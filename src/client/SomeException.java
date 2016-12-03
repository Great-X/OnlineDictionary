package client;


// 自定义异常类
public class SomeException extends Exception{
    private String errorMessage;

    public SomeException(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String toString(){
        return errorMessage;
    }

    public String getMessage(){
        return toString();
    }

}
