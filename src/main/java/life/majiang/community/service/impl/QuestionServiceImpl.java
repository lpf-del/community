package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.deo.Question;
import life.majiang.community.mapper1.QuestionMapper1;
import life.majiang.community.service.QuestionService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper1, Question> implements QuestionService {
}
