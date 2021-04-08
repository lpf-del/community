package life.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND(201L,"这个问题不存在"),
    TARGET_PARAM_NOT_FOUND(202L,"未选中任何问题或评论进行回复"),
    NO_LOGIN(203L,"未登录,请先登录"),
    SYS_ERROR(204L,"");
    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Long getCode() {
        return code;
    }

    private Long code;
    private String message;
    CustomizeErrorCode(Long code, String message){
        this.message = message;
        this.code = code;
    }
}
