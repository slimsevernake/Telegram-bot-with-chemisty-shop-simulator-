package ru.project.bots.logic.services.functions;

import ru.project.bots.model.hibernate.SubstancesDAOImpl;
import ru.project.bots.dto.DialogLevel;
import ru.project.bots.dto.FormDTO;
import ru.project.bots.dto.SubstanceDTO;
import ru.project.bots.dto.SubstanceType;
import ru.project.bots.logic.BotContextInitializer;

import java.util.List;

public abstract class DefaultDialogFunctions {

    public static Executable onDialogStart(){

        return ((update, view, dao) -> {

            String message = update.getMessage().getText().trim().toLowerCase();

            if("отмена".equals(message)){

                return view.getWelcomeMessage(update.getMessage().getChatId());
            }else if("приобрести".equals(message)){

                if(!dao.updateDialogLevel(update.getMessage().getChatId(), DialogLevel.DIALOG_CITY_SELECTION))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                return view.getCitySelectionMessage(update.getMessage().getChatId());
            }else if("мои заказы".equals(message)){

                List<FormDTO> forms = dao.getOrders(update.getMessage().getChatId());
                if(forms.size() == 0)
                    return view.getEmptyOrderListMessage(update.getMessage().getChatId());

                return view.getOrderListMessage(forms, update.getMessage().getChatId());
            }else{

                return view.getUnknownCommandMessage(update.getMessage().getChatId());
            }

        });
    }

    public static Executable onCitySelection(){

        return ((update, view, dao) -> {

            String text = update.getMessage().getText().trim().toLowerCase();
            if("отмена".equals(text))
                return view.getCitySelectionMessage(update.getMessage().getChatId());

            else if("вернуться".equals(text)) {

                if(!dao.updateDialogLevel(update.getMessage().getChatId(), DialogLevel.DIALOG_START))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                return view.getWelcomeMessage(update.getMessage().getChatId());
            }else if(!text.matches("^[А-я|Ё|ё|\\s|-]+$"))
                return view.getNotExistCityMessage(update.getMessage().getChatId());

            if(!dao.existCity(text)) {
                return view.getNotExistCityMessage(update.getMessage().getChatId());
            }

            if(!dao.updateCity(update.getMessage().getChatId(), text))
                return view.getDefaultErrorMessage(update.getMessage().getChatId());

            List<SubstanceDTO> substances =
                    new SubstancesDAOImpl().getSubstancesByIDs(BotContextInitializer.getPriceListService().getPriceList(text));

            return view.getPriceListMessage(update.getMessage().getChatId(), substances);
        });
    }

    public static Executable onPriceList(){

        return ((update, view, dao) -> {

            String text = update.getMessage().getText().trim().toLowerCase();

            String city = dao.getCityName(update.getMessage().getChatId());
            List<SubstanceDTO> substances = new SubstancesDAOImpl().getSubstancesByIDs(
                    BotContextInitializer.getPriceListService().getPriceList(city));

            if("отмена".equals(text)) {

                return view.getPriceListMessage(update.getMessage().getChatId(), substances);
            }else if("вернуться".equals(text)){

                if(!dao.updateDialogLevel(update.getMessage().getChatId(), DialogLevel.DIALOG_CITY_SELECTION))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                return view.getCitySelectionMessage(update.getMessage().getChatId());
            }

            for (SubstanceDTO substance : substances){
                if(text.equalsIgnoreCase(substance.getName())){

                    if(!dao.updateSubstance(update.getMessage().getChatId(), substance.getId()))
                        return view.getDefaultErrorMessage(update.getMessage().getChatId());

                    SubstanceType substanceForm = new SubstancesDAOImpl().getSubstanceFormByChatId(update.getMessage().getChatId());
                    return view.getWeightSelectionMessage(update.getMessage().getChatId(), substanceForm);
                }
            }
            return view.getUnknownSubstanceMessage(update.getMessage().getChatId());
        });
    }

    public static Executable onWeightSelection(){

        return ((update, view, dao) -> {

            String text = update.getMessage().getText().trim().toLowerCase();

            if("отмена".equals(text)) {

                SubstanceType substanceForm = new SubstancesDAOImpl().getSubstanceFormByChatId(update.getMessage().getChatId());

                return view.getWeightSelectionMessage(update.getMessage().getChatId(), substanceForm);
            }
            else if("вернуться".equals(text)){

                if(!dao.updateDialogLevel(update.getMessage().getChatId(), DialogLevel.DIALOG_PRICE_LIST))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                String name = dao.getCityName(update.getMessage().getChatId());
                List<SubstanceDTO> substances =
                        new SubstancesDAOImpl().getSubstancesByIDs(BotContextInitializer.getPriceListService().getPriceList(name));

                return view.getPriceListMessage(update.getMessage().getChatId(), substances);
            }else if(!text.matches("^\\d+$")) {
                return view.getBadWeightMessage(update.getMessage().getChatId());
            }


            int weight = Integer.parseInt(text);
            if(weight > 100)
                return view.getBigWeightMessage(update.getMessage().getChatId());

            if(!dao.updateWeight(update.getMessage().getChatId(), weight))
                return view.getDefaultErrorMessage(update.getMessage().getChatId());

            FormDTO form = dao.getForm(update.getMessage().getChatId());

            return view.getValidMessage(update.getMessage().getChatId(), form);
        });
    }

    public static Executable onValid(){

        return ((update, view, dao) -> {

            String text = update.getMessage().getText().trim().toLowerCase();

            FormDTO form = dao.getForm(update.getMessage().getChatId());

            if("отмена".equals(text)) {

                return view.getValidMessage(update.getMessage().getChatId(), form);
            }else if("вернуться".equals(text)){

                if(!dao.updateDialogLevel(update.getMessage().getChatId(), DialogLevel.DIALOG_WEIGHT_SELECTION))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                SubstanceType substanceForm = new SubstancesDAOImpl().getSubstanceFormByChatId(update.getMessage().getChatId());
                return view.getWeightSelectionMessage(update.getMessage().getChatId(), substanceForm);
            }else if("заполнить снова".equals(text)){

                if(!dao.updateDialogLevel(update.getMessage().getChatId(), DialogLevel.DIALOG_START))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                return view.getWelcomeMessage(update.getMessage().getChatId());
            }else if("далее".equals(text)){

                if(!dao.endDialog(update.getMessage().getChatId()))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                return view.getPaymentMessage(update.getMessage().getChatId(), form);
            }else{
                return view.getUnknownCommandMessage(update.getMessage().getChatId());
            }

        });
    }

    public static Executable onPayment(){

        return ((update, view, dao) -> {

            String text = update.getMessage().getText().trim().toLowerCase();

            if("отмена".equals(text)) {
                FormDTO form = dao.getForm(update.getMessage().getChatId());

                return view.getPaymentMessage(update.getMessage().getChatId(), form);
            }else if("я оплатил".equals(text)){

                if(!dao.updateDialogLevel(update.getMessage().getChatId(),DialogLevel.DIALOG_END))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

		        long uuid = dao.getUuid(update.getMessage().getChatId());

                return view.getEndDialogMessage(update.getMessage().getChatId(), uuid);
            }else{
                return view.getUnknownCommandMessage(update.getMessage().getChatId());
            }
        });
    }

    public static Executable onEnd(){
        return ((update, view, dao) -> {

            String text = update.getMessage().getText().trim().toLowerCase();

            if("отмена".equals(text)) {

                long uuid = dao.getUuid(update.getMessage().getChatId());

                return view.getEndDialogMessage(update.getMessage().getChatId(), uuid);
            }else if("вернуться".equals(text)) {

                if(!dao.updateDialogLevel(update.getMessage().getChatId(), DialogLevel.DIALOG_PAYMENT))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                FormDTO form = dao.getForm(update.getMessage().getChatId());

                return view.getPaymentMessage(update.getMessage().getChatId(), form);
            }
            else if("заказать ещё".equals(text)){

                if(!dao.startDialog(update.getMessage().getChatId()))
                    return view.getDefaultErrorMessage(update.getMessage().getChatId());

                return view.getWelcomeMessage(update.getMessage().getChatId());
            }else{

                return view.getUnknownCommandMessage(update.getMessage().getChatId());
            }
        });
    }

}
