package life.majiang.community;

import life.majiang.community.deo.Question;
import life.majiang.community.mapper.QuestionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {

   @Autowired
    QuestionMapper questionMapper;

    @Test
    void contextLoads() {
        Question question = new Question();
        question.setId(1L);
        question.setTitle("111111");
        int insert = questionMapper.insert(question);
        System.out.println(insert);
//        Page<Question> page=new Page<>(2,10);
//        questionMapper.selectPage(page,null);
//        page.getRecords().forEach(System.out::println);
    }

}
