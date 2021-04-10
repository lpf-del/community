package life.majiang.community.deo;

import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultDTO <T>{
    private Long code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Long code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }

    public static Object errorOf(CustomizeErrorCode sysError) {
        return sysError.getMessage();
    }

    public static <T> ResultDTO ok(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200L);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
