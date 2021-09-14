package life.majiang.community.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.deo.*;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class UtilLi {
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper1;

    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;

    @Autowired
    @SuppressWarnings("all")
    private CommentMapper commentMapper;


    @Autowired
    @SuppressWarnings("all")
    private NotificationMapper notificationMapper;

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
        map.put("code",200);
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

    public List<CommentUserDTO> listByQuestionId(Long id, Long i) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",i).eq("parent_id",id).orderByDesc("gmt_create");
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        if (comments.size() == 0){
            return new ArrayList<>();
        }
        //去重评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userId = new ArrayList<>();
        userId.addAll(commentators);
        //获取评论人并转换为map
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("id",userId);
        List<User> users = userMapper1.selectList(queryWrapper1);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user->user));

        List<CommentUserDTO> commentUserDTOS = comments.stream().map(comment -> {
            CommentUserDTO commentUserDTO = new CommentUserDTO();
            BeanUtils.copyProperties(comment,commentUserDTO);
            commentUserDTO.setUser(userMap.get(comment.getCommentator()));
            return commentUserDTO;
        }).collect(Collectors.toList());

        return commentUserDTOS;
    }

    public List<Question> selectRelated(QuestionDTO questionDTO) {
        if (questionDTO.getTag() == null){
            return new ArrayList<>();
        }
        Pattern p = Pattern.compile("[|]");
        String[] split = p.split(questionDTO.getTag());
        
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("tag",split[0]);
        queryWrapper.notIn("id",questionDTO.getId());
        List<Question> list = questionMapper.selectList(queryWrapper);
        queryWrapper.toString();
        return list;
    }

    public void tongzhi(long l, CommentDTO commentDTO,HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        Question question = questionMapper.selectById(commentDTO.getParentId());
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(user.getId());
        notification.setReceiver(question.getCreator());
        notification.setType(l);
        notification.setIfication(commentDTO.getContent());
        notification.setQuestionId(commentDTO.getParentId());
        notificationMapper.insert(notification);
    }
}
