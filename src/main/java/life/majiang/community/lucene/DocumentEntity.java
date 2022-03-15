package life.majiang.community.lucene;

import life.majiang.community.entity.ArticleEntity;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.entity.UserEntity;
import org.apache.lucene.document.*;
import org.springframework.stereotype.Component;

/**
 * @author lpf
 * @description 将entity对象转化成document对象
 * @date 2022/1/3
 */
@Component
public class DocumentEntity {

    /**
     * 将文章对象转换成Document对象
     * @param articleEntity
     * @return
     */
    public Document getDocumentArticleEntity(ArticleEntity articleEntity) {
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(articleEntity.getId()), Field.Store.YES));
        doc.add(new TextField("title", articleEntity.getTitle(), Field.Store.YES));
        doc.add(new StoredField("picturePath", articleEntity.getPicturePath()));
        doc.add(new TextField("label", articleEntity.getLabel(), Field.Store.YES));
        doc.add(new TextField("classification", articleEntity.getClassification(), Field.Store.YES));
        doc.add(new StoredField("type", articleEntity.getType()));
        doc.add(new StoredField("releaseForm", articleEntity.getReleaseForm()));
        doc.add(new TextField("content", articleEntity.getContent(), Field.Store.YES));
        doc.add(new StringField("authorId", String.valueOf(articleEntity.getAuthorId()), Field.Store.YES));
        doc.add(new StoredField("x", articleEntity.getX()));
        doc.add(new StoredField("releaseTime", articleEntity.getReleaseTime()));
        return doc;
    }

    /**
     * 将commentity对象转化成doc对象，便于创建索引
     * @param commentEntity
     * @return
     */
    public Document getDocumentCommentEntity(CommentEntity commentEntity, String username, String title) {
        Document doc = new Document();
        doc.add(new StringField("commentId", String.valueOf(commentEntity.getId()), Field.Store.YES));
        doc.add(new StringField("commentator", String.valueOf(commentEntity.getCommentator()), Field.Store.YES));
        doc.add(new StringField("reviewedByMan", String.valueOf(commentEntity.getReviewedByMan()), Field.Store.YES));
        doc.add(new StringField("articleId", String.valueOf(commentEntity.getArticleId()), Field.Store.YES));
        doc.add(new StringField("commentType", String.valueOf(commentEntity.getCommentType()), Field.Store.YES));
        doc.add(new TextField("commentContent", commentEntity.getCommentContent(), Field.Store.YES));
        doc.add(new StringField("likeCount", String.valueOf(commentEntity.getLikeCount()), Field.Store.YES));
        doc.add(new StoredField("commentPicture", commentEntity.getCommentPicture()));
        doc.add(new LongPoint("commentTime", commentEntity.getCommentTime()));
        doc.add(new StoredField("commentTime", commentEntity.getCommentTime()));
        doc.add(new TextField("username", username, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        return doc;
    }

    /**
     * 将userEntity对象转化成Document对象，方便存储索引
     * @param userEntityEntity
     * @return
     */
    public Document getDocumentUserEntity(UserEntity userEntityEntity) {
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(userEntityEntity.getId()), Field.Store.YES));
        doc.add(new TextField("username", userEntityEntity.getUserName(), Field.Store.YES));
        doc.add(new StoredField("password", userEntityEntity.getPassWord()));
        doc.add(new StoredField("mail", userEntityEntity.getMail()));
        doc.add(new StringField("telephone", userEntityEntity.getTelephone(), Field.Store.YES));
        doc.add(new StoredField("medal", userEntityEntity.getMedal()));
        doc.add(new StoredField("qq", userEntityEntity.getQq()));
        doc.add(new StoredField("weChat", userEntityEntity.getWeChat()));
        doc.add(new StoredField("introduction", userEntityEntity.getIntroduction()));
        doc.add(new TextField("address", userEntityEntity.getAddress(), Field.Store.YES));
        doc.add(new StoredField("registerTime", userEntityEntity.getRegisterTime()));
        doc.add(new LongPoint("registerTime", userEntityEntity.getRegisterTime()));
        doc.add(new StoredField("likeCount", userEntityEntity.getLikeCount()));
        doc.add(new StoredField("postCount", userEntityEntity.getPostCount()));
        doc.add(new StoredField("heatNumber", userEntityEntity.getHeatNumber()));
        doc.add(new StringField("uuid", userEntityEntity.getUuid(), Field.Store.YES));
        doc.add(new StringField("touXiangUrl", userEntityEntity.getTouXiangUrl(), Field.Store.YES));
        return doc;
    }

}
