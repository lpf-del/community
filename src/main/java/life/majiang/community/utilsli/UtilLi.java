package life.majiang.community.utilsli;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.deo.*;
import life.majiang.community.mapper1.CommentMapper;
import life.majiang.community.mapper1.QuestionMapper1;
import life.majiang.community.mapper1.UserMapper1;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class UtilLi {
    @Autowired
    @SuppressWarnings("all")
    private UserMapper1 userMapper1;

    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper1 questionMapper;

    @Autowired
    @SuppressWarnings("all")
    private CommentMapper commentMapper;

    /**
     * 将文章和用户相联系
     * @param list
     * @return
     */
    public  List<QuestionDTO> questionDTOList(List<Question> list){
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("account_id",question.getCreator());
            List<User> users = userMapper1.selectByMap(map);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    /**
     * 计算分页的最大数
     * @param count
     * @return
     */
    public Integer maxsize(Integer count){
        Integer n=0;
        if ((double)count/2==1){
            n=1;
        }
        return count/10+1-n;
    }

    /**
     * 计算文章发表为当前多少时间之前
     * @param time
     * @return
     */
    public String time(Long time){
        long l = System.currentTimeMillis();
        long l1 = time;
        
        String pattern = "yyyy-MM-dd-HH-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String time1 = simpleDateFormat.format(new Date(l));
        String time2 = simpleDateFormat.format(new Date(l1));

        Pattern p = Pattern.compile("[-]");
        String[] strings= p.split(time1);
        String[] strings1= p.split(time2);

        Integer integer = null;
        int s = 0;
        for (int i=0;i<6;i++){
            if (Integer.parseInt(strings[i])-Integer.parseInt(strings1[i])>0){
                integer = Integer.parseInt(strings[i])-Integer.parseInt(strings1[i]);
                s = i;
                break;
            }
        }
        String[] string = {"年前","月前","天前","小时前","分前","秒前"};
        return String.valueOf(integer) + string[s];
    }

    public Map<String,Object> getmap(CommentDTO commentDTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        User user = (User)request.getSession().getAttribute("user");

        map.put("user",user);

        if (user == null){
            return map;
        }

        if (commentDTO.getContent() == null || commentDTO.getContent().equals("")){
            map.put("message","不能输入空评论");
            return map;
        }

        Question question;
        Comment comment1;

        if (commentDTO.getType() != null && commentDTO.getType() == 0L){
            question = questionMapper.selectById(commentDTO.getParentId());
            if (question == null){
                map.put("massage","文章被删除");
                return map;
            }
        }else if (commentDTO.getType() != null && commentDTO.getType() == 1L){
            comment1 = commentMapper.selectById(commentDTO.getParentId());
            if (comment1 == null){
                map.put("massage","评论被删除");
                return map;
            }
        }

        map.put("massage","评论成功");
        return map;
    }


    public void countli(CommentDTO commentDTO) {
        UpdateWrapper updateWrapper = new UpdateWrapper();

        if (commentDTO.getType() == 0L && commentDTO.getType() != null){
            updateWrapper.setSql("comment_count = comment_count +1");
            updateWrapper.eq("id",commentDTO.getParentId());
            questionMapper.update(null,updateWrapper);
        }else if (commentDTO.getType() == 1L && commentDTO.getType() != null){
            updateWrapper.setSql("like_count = like_count +1");
            updateWrapper.eq("id",commentDTO.getParentId());
            commentMapper.update(null,updateWrapper);
        }
    }
}
