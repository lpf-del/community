package life.majiang.community.enums;

public enum  CommentTypeEnum {
    QUESTION(0L),
    COMMENT(1L);
    private Long type;
    CommentTypeEnum(Long type){
        this.type = type;
    }

    public Long getType() {
        return type;
    }
}
