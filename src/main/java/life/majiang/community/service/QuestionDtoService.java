package life.majiang.community.service;

import life.majiang.community.deo.Question;
import life.majiang.community.deo.QuestionDTO;
import life.majiang.community.deo.User;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionDtoService {
    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;
    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper;

    public PageDTO List(Integer page) {
        Integer count = questionMapper.count();
        Integer n=0;
        if ((double)count/10==1){
            n=1;
        }
        Integer maxsize=count/10+1-n;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPage(maxsize);
        List<Question> list = questionMapper.Lists((page-1)*10,10);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (Question question : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("account_id",question.getCreator());
            List<User> users = userMapper.selectByMap(map);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOS(questionDTOList);
        pageDTO.pages(page,maxsize);
        return pageDTO;
    }

    public PageDTO Listwen(Integer page, Long id) {
        Integer count = questionMapper.countByid(id);
        Integer n=0;
        if ((double)count/2==1){
            n=1;
        }
        Integer maxsize=count/10+1-n;
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPage(maxsize);
        List<Question> list = questionMapper.ListsByid((page-1)*10,10,id);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (Question question : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("account_id",question.getCreator());
            List<User> users = userMapper.selectByMap(map);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOS(questionDTOList);
        pageDTO.pages(page,maxsize);
        return pageDTO;
    }
}
