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
                String imageUrlTFG = createUri("/static/images/tfg.jpg");
                CarouselTemplate educationTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrlPKU, "北京大學", "2018 - 至今\n碩士·軟件工程所", Arrays.asList(
                                        new PostbackAction("主修課程",
                                                "\uD83D\uDD38雲計算技術及應用\n" +
                                                        "\uD83D\uDD38軟件體系結構與設計\n" +
                                                        "\uD83D\uDD38數據挖掘及應用\n" +
                                                        "\uD83D\uDD38推薦技術及應用\n" +
                                                        "\uD83D\uDD38機器學習\n" +
                                                        "\uD83D\uDD38人工智能實踐\n" +
                                                        "\uD83D\uDD38移動平台應用軟件開發\n" +
                                                        "\uD83D\uDD38數據分析工具實踐\n" +
                                                        "\uD83D\uDD38計算機動畫",
                                                "研究所主修課程",
                                                null)
                                )),
                                new CarouselColumn(imageUrlNTNU, "國立臺灣師範大學", "2013 - 2017\n學士·科技應用學系", Arrays.asList(
                                        new PostbackAction("主修課程",
                                                "\uD83D\uDD38計算機概論\n" +
                                                        "\uD83D\uDD38資料結構\n" +
                                                        "\uD83D\uDD38作業系統\n" +
                                                        "\uD83D\uDD38計算機結構\n" +
                                                        "\uD83D\uDD38物件導向程式語言\n" +
                                                        "\uD83D\uDD38電腦動畫\n" +
                                                        "\uD83D\uDD38數位學習概論\n" +
                                                        "\uD83D\uDD38多媒體教材設計與製作\n" +
                                                        "\uD83D\uDD38微積分\n" +
                                                        "\uD83D\uDD38虛擬實境設計\n" +
                                                        "\uD83D\uDD38創造力開發",
                                                "大學主修課程",
                                                null)
                                )),
                                new CarouselColumn(imageUrlTFG, "臺北市立第一女子高級中學", "2014 - 2017\n普通科", Arrays.asList(
                                        new PostbackAction(" ",
                                                "無",
                                                "高中主修課程",
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
                                        new MessageAction("參與項目",
                                                "在中譯語通的負責項目"),
                                        new URIAction("公司網站",
                                                "http://www.gtcom.com.cn", null)
                                )),
                                new CarouselColumn(imageUrlLarvata, "台北·果子云數位科技有限公司", "2017.07 - 2018.08\nAndroid開發工程師\nAndroid Mobile App維護與開發。", Arrays.asList(
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
                String gtcomProjectText = String.valueOf(Character.toChars(0x100060)) + "LanguageBox翻譯盒子\n"
                        + "\uD83D\uDD38" + "實現GATT Server藍芽功能收發訊息讓移動裝置端能作為遙控器\n"
                        + "\uD83D\uDD38" + "實現串接HDMI和Camera顯示畫面在Surface View上並且可以切換\n"
                        + "\uD83D\uDD38" + "修改Bugs\n";
                this.reply(replyToken, Arrays.asList(gtcomProjectMessage, new TextMessage(gtcomProjectText)));
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
            case "技能":
                String skillText = String.valueOf(Character.toChars(0x100060)) + "DITEL通訊軟體\n"
                        + "\uD83D\uDD38" + "運用WeRTC平台OpenTok完成視訊通話功能\n"
                        + "\uD83D\uDD38" + "運用Google Firebase Database完成聊天功能\n"
                        + "\uD83D\uDD38" + "運用Google FCM推送通知\n"
                        + String.valueOf(Character.toChars(0x100060)) + "DIWORK團隊日曆\n"
                        + "\uD83D\uDD38" + "使用okhttp大量串接API\n"
                        + "\uD83D\uDD38" + "月曆週曆的佈局顯示\n";
                replyText(replyToken, skillText);
                break;
            case "Side Projects":
                String sideProjectsText = String.valueOf(Character.toChars(0x100060)) + "Circular Menu Floating Action Button \n" +
                        "\uD83D\uDD3AAndroid mobile Application插件\n" +
                        "\uD83D\uDD38浮動按鈕\n" +
                        "\uD83D\uDD38User可以自定義多個子按鈕\n" +
                        "\uD83D\uDD39Java\n" +
                        "\uD83D\uDD39Android Studio\n" +
                        "\uD83D\uDD39https://github.com/hanying602/CircularMenuFloatingActionButton\n"+
                        String.valueOf(Character.toChars(0x100060))+"my-first-django-blog\n" +
                        "\uD83D\uDD3ADjango框架Blog\n" +
                        "\uD83D\uDD38登入後可新增和編輯貼文\n" +
                        "\uD83D\uDD39Python\n" +
                        "\uD83D\uDD39Pycharm\n" +
                        "\uD83D\uDD39https://github.com/hanying602/my-first-django-blog\n"+
                        String.valueOf(Character.toChars(0x100060))+"WX\n" +
                        "\uD83D\uDD3AWeather微信小程序\n" +
                        "\uD83D\uDD38查看當前地區天氣\n" +
                        "\uD83D\uDD38查看未來7天氣象預測\n" +
                        "\uD83D\uDD39JavaScript\n" +
                        "\uD83D\uDD39微信小程序Console\n" +
                        "\uD83D\uDD39https://github.com/hanying602/WeixinLittleApp\n"+
                        String.valueOf(Character.toChars(0x100060))+"SHLOVE\n" +
                        "\uD83D\uDD3A情侶互動iOS App\n" +
                        "\uD83D\uDD38計算在一起的天數\n" +
                        "\uD83D\uDD38紀錄未來想一起做和已經做過的事情\n" +
                        "\uD83D\uDD39Objective C\n" +
                        "\uD83D\uDD39Swift\n" +
                        "\uD83D\uDD39https://github.com/hanying602/SHLOVE";
                replyText(replyToken, sideProjectsText);
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
