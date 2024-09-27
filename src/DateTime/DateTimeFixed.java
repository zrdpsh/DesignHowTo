package DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/*
* В первоначальном примере дата прописана в теле функции, формат даты прописан в вызове SimpleDateFormat,
* что в принципе нехорошо, т.к. станет сложно редактировать, если кода станет много.
* Лучше сделать константами и вынести в начало.
* И одновременно с этим тот же конструктор неявно устанавливает часовой пояс --
* аналогично, лучше сделать заметным и прописать отдельно.
* Поскольку операций две, вынесем в отдельную функцию.
* Константы - до всех вызовов, сразу после открывающей скобки.
*
* Для опрятности дописать часовой пояс внутри try.
 */

public class DateTimeFixed {
    private static final String DATE_STRING = "2024-05-13 14:30:00";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_ZONE = "UTC";

    public static void main(String[] args) {

        try {
            Date date = parseDate(DATE_STRING, DATE_FORMAT, TIME_ZONE);
            System.out.println("Timezone is " + TIME_ZONE + ": " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Date parseDate(String dateString, String dateFormat, String timeZone) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        return format.parse(dateString);
    }
}
