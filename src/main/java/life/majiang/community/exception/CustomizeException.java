package life.majiang.community.exception;

public class CustomizeException extends RuntimeException{
    private String message;
    private Long code;
    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();this.code = errorCode.getCode();
    }
    public CustomizeException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public Long getCode(){
        return  code;
    }
}
