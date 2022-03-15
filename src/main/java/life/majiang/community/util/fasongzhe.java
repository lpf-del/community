package life.majiang.community.util;
//
//import com.rabbitmq.client.MessageProperties;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

/**
 * @author lpf
 * @description
 * @date 2022/3/10
 */
@Controller
public class fasongzhe {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @GetMapping("/test")
//    public Object topicSend(String routingKey, String message){
//        if (null == routingKey){
//            routingKey = "changsha.kf";
//        }
//
//        rabbitTemplate.convertAndSend("topicExchange", "topic.man", new HashMap<>());
//        return "message sended : routingKey" + routingKey + "message" + message;
//    }
}
