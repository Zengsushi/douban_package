package moon.bitter.Component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import moon.bitter.Config.SpringbootKafkaConfig;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@Slf4j
public class KafkaDataConsumer {


//    @KafkaListener(topics = SpringbootKafkaConfig.TOPIC_TEST, groupId = SpringbootKafkaConfig.GROUP_ID)
//    public void topic_test(List<String> messages , @Header(KafkaHeaders.RECEIVED) String topic){
//        for (String message : messages){
//            final JSONObject entries = JSONUtil.parseObj(message);
//            System.out.println("消费了");
//        }
//    }
}
