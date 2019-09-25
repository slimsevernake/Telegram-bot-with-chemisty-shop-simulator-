package ru.project.bots.view;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.project.bots.dto.FormDTO;
import ru.project.bots.dto.MessageContainer;
import ru.project.bots.dto.SubstanceDTO;
import ru.project.bots.dto.SubstanceType;

import java.util.List;

public class MessageViewImpl implements MessageView {

    private static String SHOP_NAME;
    private static String QIWI_WALLET;
    private static String BITCOIN;
    private static String BANK_CARD;

    public static void setBankCard(String bankCard) {
        BANK_CARD = bankCard;
    }


    private MessageContainer getDefaultMessage(long chatId, String text){
        return new MessageContainer(new SendMessage().enableMarkdown(true).enableHtml(true).setText(text).setChatId(chatId));
    }

    @Override
    public MessageContainer getEmptyMessage(long chatId){

        return getDefaultMessage(chatId,
                "Ваше сообщение пусто. К сожалению, я ещё не умею читать ваши мысли.");
    }

    @Override
    public MessageContainer getAcceptedDocumentMessage(long chatId) {

        return getDefaultMessage(chatId,
                "Документ принят..");
    }

    @Override
    public MessageContainer getUnknownCommandMessage(long chatId){

        return getDefaultMessage(chatId,
                "Мне такие команды ещё неизвестны.");
    }

    @Override
    public MessageContainer getUnknownSubstanceMessage(long chatId) {

        return getDefaultMessage(chatId,
                "Выбранного вами товара нет среди перечисленных.\n" +
                        "Пожалуйста выбирайте реальные вещи.");
    }

    @Override
    public MessageContainer getToLongMessage(long chatId){

        return getDefaultMessage(chatId,
                "Ваше сообщение слишком длинно и не умещается в моей памяти!");
    }

    @Override
    public MessageContainer getDefaultErrorMessage(long chatId) {

        return getDefaultMessage(chatId,
                "<b>Упс, кажется что-то пошло не так.</b> Попробуйте повторить позже.");
    }

    @Override
    public MessageContainer getHelpMessage(long chatId) {

        MessageContainer messageContainer = getDefaultMessage(chatId,
                "<b>Помощь</b>\n" +
                        "Если у вас возникли какие либо вопросы, то вы всегда можете связаться лично с нами по следующим контактам: \n" +
                        "<code>- Телеграм:</code> <b>@alchemistshop</b>\n\n" +
                        "<code>Примерное время ответа от 15 минут до 1-го дня.</code>\n\n" +
                        "Реквизиты:" +
                        "<code>- Qiwi:</code> <b>" + QIWI_WALLET + "</b>\n" +
                        "<code>- Card:</code> <b>" + BANK_CARD + "</b>\n" +
                        "<code>- Bitcoin:</code> <b>" + BITCOIN + "</b>\n");

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Отмена"));
        keyboard.getKeyboard().add(row);

        messageContainer.getSendMessage().setReplyMarkup(keyboard);

        return messageContainer;
    }

    @Override
    public MessageContainer getWelcomeMessage(long chatId) {

        MessageContainer messageContainer = getDefaultMessage(chatId,
                "<b>Добро пожаловать в наш магазин " + SHOP_NAME +".</b>\n" +
                        "У нас вы можете легко заказать интересующий вас товар.\n" +
                        "Мы - команда профессионалов, ответственно подходящих к своему делу.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Приобрести"));
        row.add(new KeyboardButton("Помощь"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Мои заказы");

        keyboardMarkup.getKeyboard().add(row);
        keyboardMarkup.getKeyboard().add(row2);
        messageContainer.getSendMessage().setReplyMarkup(keyboardMarkup);

        return messageContainer;
    }

    @Override
    public MessageContainer getEmptyOrderListMessage(long chatId) {
        return getDefaultMessage(chatId, "<b>Кажется у вас ещё нет заказов. Но они обязательно появятся здесь.</b>");
    }

    @Override
    public MessageContainer getOrderListMessage(List<FormDTO> orders, long chatId) {

        MessageContainer messageContainer = getDefaultMessage(chatId, "");
        StringBuilder text = new StringBuilder("<b>Список заказов: </b>\n");

        for (FormDTO order: orders){

            text.append("Заказ номер <b>").append(order.getUuid()).append("</b>\n")
                .append("- Город: <i>").append(order.getCity()).append("</i>\n")
                .append("- Товар: <i>").append(order.getSubstance().getName()).append("</i>\n")
                .append(getFormSubstanceString(order.getSubstance().getType(), order.getWeight()))
                .append("- Цена: <i>").append(order.getPrice()).append("</i>\n\n");

        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("отмена"));

        replyKeyboardMarkup.getKeyboard().add(row);

        messageContainer.getSendMessage().setText(text.toString());
        messageContainer.getSendMessage().setReplyMarkup(replyKeyboardMarkup);

        return messageContainer;
    }

    @Override
    public MessageContainer getCitySelectionMessage(long chatId) {

        MessageContainer messageContainer = getDefaultMessage(chatId,
                "<b>Шаг 1.</b> <code>Определение местоположения</code>\n\n" +
                        "Напишите ваш город.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Вернуться"));
        keyboardMarkup.getKeyboard().add(row);

        messageContainer.getSendMessage().setReplyMarkup(keyboardMarkup);

        return messageContainer;
    }

    @Override
    public MessageContainer getNotExistCityMessage(long chatId) {

        return getDefaultMessage(chatId,
                "<b>Ошибка</b>\n" +
                        "Кажется у нас нет такого города.\n" +
                        "Вы можете приобрести товар в ближайших из доступных городов");
    }

    @Override
    public MessageContainer getPriceListMessage(long chatId, List<SubstanceDTO> substances) {

        SendMessage message = new SendMessage();

        message.enableMarkdown(true);
        message.setChatId(chatId);
	    message.enableHtml(true);
        StringBuilder text = new StringBuilder("<b>Шаг 2.</b> <code>Выбор товара</code>\n\n" +
                "На данный момент в вашем городе доступны следующие товары: \n");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        for (SubstanceDTO substance : substances){
            text.append("- ").append(substance.getName())
                    .append(", ").append(substance.getPrice()).append(" рублей ")
                    .append(substanceType(substance.getType())).append("\n");
            keyboardMarkup.getKeyboard().add(new KeyboardRow(){{
                add(new KeyboardButton(substance.getName()));
            }});
        }
        text.append("\n<code>(если нет того, что вы хотели, то вы всягда можете с нами связаться, " +
                "используя контактную информацию из раздела 'помощь')</code>");

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Вернуться"));
        keyboardMarkup.getKeyboard().add(row);

        message.setText(text.toString());
        message.setReplyMarkup(keyboardMarkup);

        return new MessageContainer(message);
    }

    private String substanceType(SubstanceType type){
        switch (type){
            case DUST:
                return "(по 10 грамм)";
            case TABLET:
                return "(гранулы)";
            case PACK:
                return "(упаковки)";
            case FLUID:
                return "(по 100 мл)";
        }
        return "";
    }

    @Override
    public MessageContainer getWeightSelectionMessage(long chatId, SubstanceType type) {

        MessageContainer messageContainer = getDefaultMessage(chatId,
                    "<b>Шаг 3.</b> <code>Определение объёма товара</code>\n\n" +
                            getNameSubstanceForm(type));
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        for(int i = 1; i <= 10; i++){
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(String.valueOf(i)));
            keyboardMarkup.getKeyboard().add(row);
        }
	    keyboardMarkup.getKeyboard().add(new KeyboardRow() {{
            add(new KeyboardButton("Вернуться"));
        }});
        messageContainer.getSendMessage().setReplyMarkup(keyboardMarkup);

        return messageContainer;
    }

    @Override
    public MessageContainer getBigWeightMessage(long chatId) {

        return getDefaultMessage(chatId, "Для заказа такого объёма свяжитесь лично с нами.\n" +
                "<code>- Телеграм:</code> <b>@alchemistshop</b>");
    }

    private String getNameSubstanceForm(SubstanceType type){
        switch (type){
            case DUST:
                return "Выберите желаемый вес";
            case PACK:
                return "Выберите желаемое количество упаковок";
            case TABLET:
                return "Выберите желаемое количество гранул";
            case FLUID:
                return "Выберите желаемый объем";
        }
        return "";
    }

    @Override
    public MessageContainer getBadWeightMessage(long chatId) {

        return getDefaultMessage(chatId,
                "<b>Ошибка</b>\n" +
                        "Вес продукта должен быть целым числом.");
    }

    @Override
    public MessageContainer getValidMessage(long chatId, FormDTO formDTO) {

        StringBuilder sb = new StringBuilder("<b>Шаг 5.</b> <code>Валидация</code>\n\n" +
                "Проверьте правильность вашего заказа:\n");

        sb.append("- <code>Ваш город: </code><i>").append(formDTO.getCity()).append("</i>\n")
                .append("- <code>Выбранный товар: </code><i>").append(formDTO.getSubstance().getName()).append("</i>\n")
                .append(getFormSubstanceString(formDTO.getSubstance().getType(), formDTO.getWeight()))
                .append("- <b>Итоговая цена: </b><i>").append(formDTO.getPrice()).append("</i>");

        MessageContainer messageContainer =  getDefaultMessage(chatId, sb.toString());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Вернуться"));
        row.add(new KeyboardButton("Далее"));
        keyboardMarkup.getKeyboard().add(row);

        row = new KeyboardRow();
        row.add(new KeyboardButton("Заполнить снова"));
        keyboardMarkup.getKeyboard().add(row);

        messageContainer.getSendMessage().setReplyMarkup(keyboardMarkup);

        return messageContainer;
    }

    private String getFormSubstanceString(SubstanceType type, int val){
        StringBuilder sb = new StringBuilder();
        switch (type){
            case DUST:
                return sb.append("- <code>Вес товара: </code><i>").append(val)
                        .append("</i> грамм\n").toString();
            case PACK:
                return sb.append("- <code>Количество: </code><i>").append(val)
                        .append("</i> упаков").append(val==1?"ка\n":"ок\n").toString();
            case TABLET:
                return sb.append("- <code>Количество: </code><i>").append(val)
                        .append("</i> гранул").append(val == 1?"а\n":"\n").toString();
            case FLUID:
                return sb.append("- <code>Объём: </code><i>").append(val)
                        .append("</i> мл").toString();
        }
        return "";
    }

    @Override
    public MessageContainer getPaymentMessage(long chatId, FormDTO form) {

        MessageContainer messageContainer = getDefaultMessage(chatId,
                "<b>Шаг 6.</b> <code>Оплата заказа</code>\n\n" +
                        "Для завершения сделки № <i>"+form.getUuid()+"</i> необходимо отправить <i>" + form.getPrice() + "</i> рублей на следующие счёта:\n" +
                        "Qiwi: " + QIWI_WALLET + "\n" +
                        "Card: " + BANK_CARD + " \n" +
                        "Bitcoin: " + BITCOIN + " \n\n" +
                        "<b>ВАЖНО!</b> После оплаты заказа обязательно сохраните чек и любым удобным способом отправьте его в данный диалог, " +
                        "чтобы мы без проблем смогли идентифицировать ваш заказ. При возникновении трудностей в оплате обращайтесь к нам, " +
                        "используя контактные данные, указанные в разделе 'Помощь' (/help)\n\n" +
                        "Процесс обработки заказа обычно занимает от 1-го часа до 1-го дня. После проверки оплаты, " +
                        "мы отправим вам координаты и фотографии необходимого места.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Я оплатил"));
	    keyboardMarkup.getKeyboard().add(row);

        messageContainer.getSendMessage().setReplyMarkup(keyboardMarkup);

        return messageContainer;
    }

    @Override
    public MessageContainer getEndDialogMessage(long chatId, long uuid) {

        MessageContainer messageContainer = getDefaultMessage(chatId,
                "<b>Шаг 7.</b> <code>Ожидание</code>\n" +
                        "Ваш заказ № <i>" + uuid + "</i> был принят на обработку. " +
                        "В течение ближайшего времени мы постараемся оперативно обработать ваш заказ и выслать вам дальнейшие инструкции.\n" +
                        "Спасибо, что покупаете у нас.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Вернуться"));
        row.add(new KeyboardButton("Заказать ещё"));
	    keyboardMarkup.getKeyboard().add(row);

        messageContainer.getSendMessage().setReplyMarkup(keyboardMarkup);

        return messageContainer;
    }

    public static void setShopName(String shopName) {
        SHOP_NAME = shopName;
    }

    public static void setQiwiWallet(String qiwiWallet) {
        QIWI_WALLET = qiwiWallet;
    }

    public static void setBitcoin(String bitcoin){BITCOIN = bitcoin;}
}
