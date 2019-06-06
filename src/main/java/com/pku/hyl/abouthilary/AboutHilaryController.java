package com.pku.hyl.abouthilary;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
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

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        this.replyText(replyToken, event.getPostbackContent().getData());
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

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        String text = content.getText();
        switch (text) {
            case "學歷":
                String imageUrlPKU = createUri("/static/images/pku.jpg");
                String imageUrlNTNU = createUri("/static/images/ntnu.jpg");
                CarouselTemplate educationTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrlPKU, "北京大學", "2018 - 至今\n軟體工程研究所", Arrays.asList(
                                        new PostbackAction("主修課程",
                                                "軟體架構設計、——",
                                                "研究所主修課程",
                                                null)
                                )),
                                new CarouselColumn(imageUrlNTNU, "國立臺灣師範大學", "2013 - 2017\n科技應用學系", Arrays.asList(
                                        new PostbackAction("主修課程",
                                                "資料結構、——",
                                                "大學主修課程",
                                                null)
                                ))
                        ));
                TemplateMessage educationMessage = new TemplateMessage("林函盈的學歷", educationTemplate);
                this.reply(replyToken, educationMessage);
                break;
            case "經歷":
                String imageUrlGtcom = createUri("/static/images/gtcom.png");
                String imageUrlLarvata = createUri("/static/images/larvata.jpg");
                CarouselTemplate experienceTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrlGtcom, "北京·中譯語通科技股份有限公司", "2018.03 - 至今\nAndroid實習生\nAndroid TV App維護與開發。", Arrays.asList(
//                                        new PostbackAction("主要職責",
//                                                "Android TV維護開發、——",
//                                                "在中譯語通的職責",
//                                                null),
                                        new MessageAction("參與項目",
                                                "在中譯語通的負責項目"),
                                        new URIAction("公司網站",
                                                "http://www.gtcom.com.cn", null)
                                )),
                                new CarouselColumn(imageUrlLarvata, "台北·果子云數位科技有限公司", "2017.07 - 2018.08\nAndroid開發工程師\nAndroid Mobile App維護與開發。", Arrays.asList(
//                                        new PostbackAction("主要職責",
//                                                "Android App維護開發、——",
//                                                "在果子云的職責",
//                                                null),
                                        new MessageAction("參與項目",
                                                "在果子云的負責項目"),
                                        new URIAction("公司網站",
                                                "https://larvata.tw/", null)
                                ))
                        ));
                TemplateMessage experienceMessage = new TemplateMessage("林函盈的經歷", experienceTemplate);
                this.reply(replyToken, experienceMessage);
                break;
            case "在中譯語通的負責項目":
                String imageUrlLanguagebox = createUri("/static/images/languagebox.png");
                ImageCarouselTemplate gtcomProjectTemplate = new ImageCarouselTemplate(
                        Arrays.asList(
                                new ImageCarouselColumn(imageUrlLanguagebox,
                                        new URIAction("Learn more",
                                                "http://www.gtcom.com.cn/?c=yeecloud&a=details&id=10", null)
                                )
                        ));
                TemplateMessage gtcomProjectMessage = new TemplateMessage("林函盈在中譯語通的負責項目",
                        gtcomProjectTemplate);
                this.reply(replyToken, gtcomProjectMessage);
                this.replyText(replyToken, text);
                break;
            case "在果子云的負責項目":
                String imageUrlDitel = createUri("/static/images/ditel.png");
                String imageUrlDiwork = createUri("/static/images/diwork.png");
                ImageCarouselTemplate larvataProjectTemplate = new ImageCarouselTemplate(
                        Arrays.asList(
                                new ImageCarouselColumn(imageUrlDitel,
                                        new URIAction("Learn more",
                                                "http://ditel.com.tw", null)
                                ),
                                new ImageCarouselColumn(imageUrlDiwork,
                                        new URIAction("Learn more",
                                                "http://www.diwork.com.tw", null)
                                )
                        ));
                TemplateMessage larvataProjectMessage = new TemplateMessage("林函盈在果子云的負責項目",
                        larvataProjectTemplate);
                String larvataProjectText = String.valueOf(Character.toChars(0x100060)) + "DITEL通訊軟體\n"
                        + "\uD83D\uDD38" + "運用WeRTC平台OpenTok完成視訊通話功能\n"
                        + "\uD83D\uDD38" + "運用Google Firebase Database完成聊天功能\n"
                        + "\uD83D\uDD38" + "運用Google FCM推送通知\n"
                        + String.valueOf(Character.toChars(0x100060)) + "DIWORK團隊日曆\n"
                        + "\uD83D\uDD38" + "使用okhttp大量串接API\n"
                        + "\uD83D\uDD38" + "月曆週曆的佈局顯示\n";
                this.reply(replyToken, Arrays.asList(larvataProjectMessage, new TextMessage(larvataProjectText)));
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
