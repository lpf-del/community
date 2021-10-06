package life.majiang.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.UserArticleVisitLogEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserArticleVisitLogEntityMapper;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.service.ArticleRankingEntityService;
import life.majiang.community.service.CookieService;
import life.majiang.community.service.UserArticleVisitLogEntityService;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 用户的访问日志
 * @date 2021/10/5
 */
@Service
public class UserArticleVisitLogEntityServiceImpl extends ServiceImpl<UserArticleVisitLogEntityMapper, UserArticleVisitLogEntity> implements UserArticleVisitLogEntityService {

    @Resource
    private UserArticleVisitLogEntityMapper userArticleVisitLogEntityMapper;

    @Resource
    private CookieService cookieService;

    @Resource
    private ArticleRankingEntityService articleRankingEntityService;

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 添加登录日志
     * @param articleId
     * @param request
     */
    @Override
    public void addVisitLog(Integer articleId, HttpServletRequest request) {
        //redis没有用户信息说明未登录，视为游客登录不添加访问量
        Integer userId = cookieService.getUserId(request);
        if (userId == null) return;

        UserArticleVisitLogEntity uavl = new UserArticleVisitLogEntity();
        uavl.setArticleId(articleId);
        uavl.setVisitTime(System.currentTimeMillis());
        uavl.setUserId(userId);
        userArticleVisitLogEntityMapper.insert(uavl);
    }

    @Override
    public void addArticleVisit(Integer articleId, HttpServletRequest request) {
        addVisitLog(articleId, request);
        articleRankingEntityService.articleVisit(articleId, request);
    }

    @Override
    public void addUserVisitCount(Integer authorId) {
        UserEntity userEntity = userEntityMapper.selectById(authorId);
        userEntity.setHeatNumber(userEntity.getHeatNumber() + 1);
        userEntityMapper.updateById(userEntity);
        redisUtil.set(userEntity.getTelephone(), JSON.toJSONString(userEntity));
        redisUtil.set(userEntity.getMail(), JSON.toJSONString(userEntity));
    }
}
