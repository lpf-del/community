package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.service.UserEntityService;
import org.springframework.stereotype.Service;

/**
 * @author lpf
 * @description 新user类
 * @date 2021/9/14
 */
@Service
public class UserEntityServiceImpl extends ServiceImpl<UserEntityMapper, UserEntity> implements UserEntityService{
}
