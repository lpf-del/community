package life.majiang.community.service;

import life.majiang.community.deo.Question;
import life.majiang.community.deo.QuestionDTO;
import life.majiang.community.deo.User;
import life.majiang.community.mapper1.QuestionMapper;
import life.majiang.community.mapper1.UserMapper;
import life.majiang.community.utilsli.UtilLi;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    @SuppressWarnings("all")
    UtilLi utilLi;
    public PageDTO List(Integer page) {
        Integer count = questionMapper.count();
        Integer maxsize = utilLi.maxsize(count);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPage(maxsize);
        if (page>=maxsize){
            page=maxsize;
        }
        List<Question> list = questionMapper.Lists((page-1)*10,10);
        List<QuestionDTO> questionDTOList = utilLi.questionDTOList(list);
        pageDTO.setQuestionDTOS(questionDTOList);
        pageDTO.pages(page,maxsize);
        return pageDTO;
    }

    public PageDTO Listwen(Integer page, Long id) {
        Integer count = questionMapper.countByid(id);
        Integer maxsize = utilLi.maxsize(count);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPage(maxsize);
        if (page>=maxsize){
            page=maxsize;
        }
        List<Question> list = questionMapper.ListsByid((page-1)*10,10,id);
        List<QuestionDTO> questionDTOList = utilLi.questionDTOList(list);
        pageDTO.setQuestionDTOS(questionDTOList);
        pageDTO.pages(page,maxsize);
        return pageDTO;
    }

    public QuestionDTO selectById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectById(id);
        BeanUtils.copyProperties(question, questionDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("account_id",question.getCreator());
        List<User> users = userMapper.selectByMap(map);
        if (users!=null){
            questionDTO.setUser(users.get(0));
        }
        return questionDTO;
    }
}
