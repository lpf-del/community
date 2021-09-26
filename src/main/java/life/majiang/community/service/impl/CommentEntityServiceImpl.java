package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.mapper.CommentEntityMapper;
import life.majiang.community.service.CommentEntityService;
import org.springframework.stereotype.Service;

/**
 * @author lpf
 * @description 新的评论类
 * @date 2021/9/26
 */
@Service
public class CommentEntityServiceImpl extends ServiceImpl<CommentEntityMapper, CommentEntity> implements CommentEntityService {

}
