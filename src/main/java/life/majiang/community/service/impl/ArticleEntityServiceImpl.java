package life.majiang.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.ArticleEntityMapper;
import life.majiang.community.service.ArticleEntityService;
import life.majiang.community.service.CookieService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lpf
 * @description 新的文章类
 * @date 2021/9/26
 */
@Service
public class ArticleEntityServiceImpl extends ServiceImpl<ArticleEntityMapper, ArticleEntity> implements ArticleEntityService {

    @Resource
    private CookieService cookieService;

    @Resource
    private ArticleEntityMapper articleEntityMapper;
    @Override
    public void addArticle(String title, String description, String myTags, String articleType, String releaseForm, String fileUrl, HttpServletRequest request) throws Exception {
        UserEntity personInformation = cookieService.getPersonInformation(request);
        if (personInformation == null){
            throw new Exception("请登录");
        }
        Integer userId = personInformation.getUserId();
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(title==null?"":title);//标题
        articleEntity.setAuthorId(userId);//作者id
        articleEntity.setPicturePath(fileUrl==null?"":fileUrl);//封面路径
        articleEntity.setLabel(myTags==null?"":myTags);//标签
        articleEntity.setClassification(""); //暂时用不上（分类）
        articleEntity.setType(articleType);//文章类型：原创。。。
        articleEntity.setReleaseForm(releaseForm);//文章发布形式：公开。。
        articleEntity.setContent(description==null?"":description);//文章内容
        articleEntity.setReleaseTime(System.currentTimeMillis());//发布时间
        articleEntityMapper.insert(articleEntity);

    }
}
