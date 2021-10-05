package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.UserCommentLogEntity;
import life.majiang.community.mapper.UserCommentLogEntityMapper;
import life.majiang.community.service.UserCommentLogEntityService;
import org.springframework.stereotype.Service;

/**
 * @author lpf
 * @description 用户的访问日志
 * @date 2021/10/5
 */
@Service
public class UserCommentLogEntityServiceImpl extends ServiceImpl<UserCommentLogEntityMapper, UserCommentLogEntity> implements UserCommentLogEntityService {
}
