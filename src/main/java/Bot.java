import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.media.InputMedia;
import org.telegram.telegrambots.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = "461809532:AAGN_01-GuhjJ2k6HkKmPlIRLBjvxIpZWCA";
    private static final String BOT_USER_NAME = "Sara Photo Bot";

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String query = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (query.equals("/start")) {
                String messageText = "Hi! My name is Sara and I'm photo-bot developed " +
                        "by madam Kozlova. Please, write me a word you want get the picture of." +
                        " But write it correctly, I am not speaking to the illiterate :)";

                SendMessage message = new SendMessage()
                        .setChatId(chatId)
                        .setText(messageText);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else {
                GoogleSearch googleSearch = new GoogleSearch();
                String[] parsedQuery = QueryParser.parseQuery(query);
                List<String> resultList = new ArrayList<>();
                try {
                    resultList = googleSearch.run(parsedQuery[0], Integer.parseInt(parsedQuery[1]));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (Integer.parseInt(parsedQuery[1]) > 10 || Integer.parseInt(parsedQuery[1]) < 1) {
                    String messageText = "The number of pictures must be from 1 to 10 value. Change your query please.";

                    SendMessage message = new SendMessage()
                            .setChatId(chatId)
                            .setText(messageText);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (Integer.parseInt(parsedQuery[1]) == 1){

                    if (resultList.isEmpty()) {
                        String messageText = "There are no results for your input.";

                        SendMessage message = new SendMessage()
                                .setChatId(chatId)
                                .setText(messageText);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {

                        String photoURL = resultList.get(0);

                        SendPhoto photo = new SendPhoto()
                                .setChatId(chatId)
                                .setPhoto(photoURL);
                        try {
                            sendPhoto(photo);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    List<InputMedia> inputMedia = new ArrayList<>();

                    for(String s : resultList){
                        inputMedia.add(new InputMediaPhoto().setMedia(s));
                    }

                    SendMediaGroup mg = new SendMediaGroup();
                    mg.setMedia(inputMedia);
                    mg.setChatId(chatId);

                    try {
                        sendMediaGroup(mg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getBotUsername() {
        return BOT_USER_NAME;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
