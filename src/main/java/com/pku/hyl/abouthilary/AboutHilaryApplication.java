package com.pku.hyl.abouthilary;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@LineMessageHandler
public class AboutHilaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AboutHilaryApplication.class, args);
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        final String originalMessageText = event.getMessage().getText();
        String resultMessage = "";
        switch (originalMessageText) {
            case "我是蘇冠融":
                resultMessage = "我愛你";
                break;
            case "我是姜佳宜":
                resultMessage = "好想妳～可愛的姜佳宜(cony kiss)";
                break;
            default:
                resultMessage = originalMessageText;
                break;
        }
        return new TextMessage(resultMessage);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
