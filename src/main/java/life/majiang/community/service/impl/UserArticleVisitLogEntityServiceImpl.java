package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.UserArticleVisitLogEntity;
import life.majiang.community.mapper.UserArticleVisitLogEntityMapper;
import life.majiang.community.service.UserArticleVisitLogEntityService;
import org.springframework.stereotype.Service;

/**
 * @author lpf
 * @description 用户的访问日志
 * @date 2021/10/5
 */
@Service
public class UserArticleVisitLogEntityServiceImpl extends ServiceImpl<UserArticleVisitLogEntityMapper, UserArticleVisitLogEntity> implements UserArticleVisitLogEntityService {
}
