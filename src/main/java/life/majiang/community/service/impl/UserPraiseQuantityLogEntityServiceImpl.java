package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.UserPraiseQuantityLogEntity;
import life.majiang.community.mapper.UserPraiseQuantityLogEntityMapper;
import life.majiang.community.service.UserPraiseQuantityLogEntityService;
import org.springframework.stereotype.Service;

/**
 * @author lpf
 * @description 用户的点赞日志
 * @date 2021/10/5
 */
@Service
public class UserPraiseQuantityLogEntityServiceImpl extends ServiceImpl<UserPraiseQuantityLogEntityMapper, UserPraiseQuantityLogEntity> implements UserPraiseQuantityLogEntityService {
}
