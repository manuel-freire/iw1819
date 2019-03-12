package es.ucm.fdi.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public final static SimpleDateFormat dateHtmlFormat = new SimpleDateFormat("yyy-MM-dd");
    public final static SimpleDateFormat dateWithHourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public final static SimpleDateFormat fullDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    public final static SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

    public DateUtil() { }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static SimpleDateFormat getDateWithHourFormat() {
        return dateWithHourFormat;
    }
    
    public static String hourCorrectFormat(String horaIniString) {
        horaIniString = horaIniString.substring(0, 5);//eliminar am / pm
        horaIniString += ":00";//añadir segs
        return horaIniString;
    }

    public static Date dateCorrectFormat(String fechaIniString, String horaIniString) {
        String days = fechaIniString.substring(0, 2);
        String months = fechaIniString.substring(3, 5);
        String years = fechaIniString.substring(6, 10);

        return new Date(years + "/" + months + "/" + days + " " + horaIniString);
    }

    public static Date getFullDate(String fechaStr, String horaStr) {
    	Date date = null;
    	try {
			 date = fullDate.parse(fechaStr + " " + horaStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return date;
    }
    
    public static Date getDateWithoutHour(String fechaStr) {
    	Date date = null;
    	try {
			 date = dateHtmlFormat.parse(fechaStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return date;
    }
    
    public static String horaMostrarString(Date fecha) {
        String am_pm = "a.m.";
        String hora = hourFormat.format(fecha);
        String res = "";
        String auxHora = "";

        int horas = Integer.parseInt(hora.substring(0,2));

        if(horas > 12) {
            am_pm = "p.m.";
        }
        else if(horas == 12) {
            am_pm = "p.m.";
        }

        if(horas < 10)
            auxHora = "0";

        res = auxHora + horas + hora.substring(2) + " " + am_pm;

        return res;
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha); // Configuramos la fecha que se recibe

        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
	
}