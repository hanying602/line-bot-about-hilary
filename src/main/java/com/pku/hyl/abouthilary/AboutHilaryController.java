package com.pku.hyl.abouthilary;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class AboutHilaryController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content){
        String text = content.getText();
        switch (text) {
            case "學歷":
                String imageUrlPKU = createUri("/static/images/pku.jpg");
                String imageUrlNTNU = createUri("/static/images/ntnu.jpg");
                CarouselTemplate educationTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrlPKU, "北京大學 2018-", "軟體工程研究所", Arrays.asList(
                                        new PostbackAction("主修課程",
                                                "研究所主修課程",
                                                "軟體架構設計、——")
//                                        ,new PostbackAction("Say hello1",
//                                                "hello こんにちは")
                                )),
                                new CarouselColumn(imageUrlNTNU, "國立臺灣師範大學 2013-2017", "科技應用學系", Arrays.asList(
                                        new PostbackAction("主修課程",
                                                "大學主修課程",
                                                "數據挖掘、——")
//                                        ,new MessageAction("Say message",
//                                                "Rice=米")
                                ))
                        ));
                TemplateMessage educationMessage = new TemplateMessage("林函盈的學歷", educationTemplate);
                this.reply(replyToken, educationMessage);
                break;
            case "經歷":
                String imageUrlGtom = createUri("/static/images/ntnu.jpg");
                String imageUrlLavarta = createUri("/static/images/pku.jpg");
                CarouselTemplate experienceTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrlGtom, "北京大學 2018-", "實習生", Arrays.asList(
                                        new URIAction("Go to line.me",
                                                "https://line.me", null),
                                        new PostbackAction("Say hello1",
                                                "hello こんにちは")
                                )),
                                new CarouselColumn(imageUrlLavarta, "國立臺灣師範大學 2013-2017", "實習生", Arrays.asList(
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                "hello こんにちは"),
                                        new MessageAction("Say message",
                                                "Rice=米")
                                ))
                        ));
                TemplateMessage experienceMessage = new TemplateMessage("林函盈的經歷", experienceTemplate);
                this.reply(replyToken, experienceMessage);
                break;
            default:
                replyText(replyToken, text);
                break;
        }
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString();
    }
}
