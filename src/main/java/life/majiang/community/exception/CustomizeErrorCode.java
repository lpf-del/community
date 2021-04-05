package life.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND("这个问题不存在");
    @Override
    public String getMessage(){
        return message;
    }

    private String message;
    CustomizeErrorCode(String message){
        this.message = message;
    }
}
