package life.majiang.community;


//import com.rabbitmq.client.*;
import life.majiang.community.controller.CreateLuceneController;
import life.majiang.community.controller.DeleteLuceneController;
import life.majiang.community.entity.CommentEntity;
import life.majiang.community.lucene.UserEntitySearchIndex;
import life.majiang.community.mapper.CommentEntityMapper;
//import life.majiang.community.service.LucenenServiceOld;
import life.majiang.community.util.RedisUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wltea.analyzer.lucene.IKAnalyzer;


import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class CommunityApplicationTests {
    @Resource
    UserEntitySearchIndex userEntitySearchIndex;

    @Resource
    CreateLuceneController createLuceneController;

    @Resource
    DeleteLuceneController deleteLuceneController;

    @Autowired
    RedisUtil redisUtil;

    @Test
    void contextLoads() throws Exception {


    }

//    void OrderSystem() throws IOException {
//        Connection connection = RabbitMQUtil.getConnection();
//        Channel channel = connection.createChannel();
//        channel.queueDeclare("sms", false, false, false, null);
//
//        for (int i = 0; i < 100; i++) {
//
//        }
//    }
}
//class RabbitMQUtil{
//    static ConnectionFactory connectionFactory = new ConnectionFactory();
//    static {
//        connectionFactory.setHost("121.40.218.249");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("root");
//        connectionFactory.setPassword("root");
//        connectionFactory.setVirtualHost("/");
//    }
//    public static Connection getConnection(){
//        Connection connection = null;
//        try {
//            connection = connectionFactory.newConnection();
//            return connection;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
//class Reciver extends DefaultConsumer{
//    private Channel channel;
//    //?????????????????????Channel???????????????????????????????????????handleFelivery????????????
//    public Reciver(Channel channel) {
//        super(channel);
//        this.channel = channel;
//    }
//
//    @Override
//    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//        String message = new String(body);
//        System.out.println("??????????????????????????????" + message);
//
//        System.out.println("?????????TagId???" + envelope.getDeliveryTag());
//        //false??????????????????????????????????????????true?????????????????????????????????????????????????????????
//        channel.basicAck(envelope.getDeliveryTag(), false);
//    }
//}