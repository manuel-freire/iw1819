package es.ucm.fdi.iw.parser;

import java.text.ParseException;
import java.util.Date;

import es.ucm.fdi.util.DateUtil;


public class Parser {

    private static final int PARSE_COD_NULL = 000;
    private static final int PARSE_COD_STRING_EMPTY = 001;
    private static final String PE_MSG_CAMPO_OBLIGATORIO = "Por favor, rellena el campo obligatorio";

    protected static final int PARSE_COD_FORMATO_INCORRECTO = 202;
    protected static final String PE_MSG_FORMATO_INCORRECTO = "Formato incorrecto";
    

    public static boolean isNotNull(Object obj) throws ParseException {
        if (obj == null)
            throw new ParseException(PE_MSG_CAMPO_OBLIGATORIO, PARSE_COD_NULL);

        return true;
    }

    public static boolean isNotEmptyString(String str) throws ParseException {
        if (str.isEmpty())
            throw new ParseException(PE_MSG_CAMPO_OBLIGATORIO, PARSE_COD_STRING_EMPTY);

        return true;
    }

    //Fechas y horas
    public static Date parseDate(String dateStr) throws ParseException {
        Date date = null;
        try {
            date = DateUtil.getDateFormat().parse(dateStr);
        } catch(ParseException pe) {
            throw new ParseException(PE_MSG_FORMATO_INCORRECTO, PARSE_COD_FORMATO_INCORRECTO);
        }
        return date;
    }

    protected String getError(String err) {
        String error = err;
    	int pos = err.indexOf("(at");
        
    	if(pos != -1)
            error = err.substring(0,pos);
        
    	return error;
    }

}
