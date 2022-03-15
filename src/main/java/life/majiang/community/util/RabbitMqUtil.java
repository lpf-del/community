package life.majiang.community.util;

//import org.springframework.amqp.core.Binding;
//
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * @author lpf
 * @description rabbitmq消息中间件工具类
 * @date 2022/3/10
 */
@Configuration
public class RabbitMqUtil {

//    @Bean
//    public Queue topocQ1(){return new Queue("topic_sb_1");}
//
//    @Bean
//    public Queue topocQ2(){return new Queue("topic_sb_2");}
//
//    //声明exchange
//    @Bean
//    public TopicExchange setTopicExchange(){return new TopicExchange("TopicExchange");}
//
//    //声明binding，需要声明一个roytingKey
//    @Bean
//    public Binding bindTopicHebei1(){return BindingBuilder.bind(topocQ1()).to(setTopicExchange()).with("x.*"); }
//
//    @Bean
//    public Binding bindTopicHebei2(){return BindingBuilder.bind(topocQ2()).to(setTopicExchange()).with("#.x"); }

}
