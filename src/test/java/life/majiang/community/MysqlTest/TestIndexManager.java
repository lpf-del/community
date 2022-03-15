package life.majiang.community.MysqlTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.javafx.collections.MappingChange;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.mapper.CommentEntityMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lpf
 * @description 索引库维护
 * @date 2021/12/15
 */
@SpringBootTest
public class TestIndexManager {

}
