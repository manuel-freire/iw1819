package es.ucm.fdi.iw.parser;

import java.util.Map;

public class ParserResponse {
	
	private boolean ok;
	
	private String message;
	
	private Map<String, Object> args;
	
	public ParserResponse() {
		this.setOk(false);
		this.setMessage("");
		this.setArgs(null);
	}
	
	public ParserResponse parserOKResponse(String msg, Map<String, Object> arguments) {
		ParserResponse pRes = new ParserResponse();
		pRes.setOk(true);
		pRes.setMessage(msg);
		pRes.setArgs(arguments);
		
		return pRes;
	}
	
	public ParserResponse parserFailResponse(String msg, Map<String, Object> arguments) {
		ParserResponse pRes = new ParserResponse();
		pRes.setOk(false);
		pRes.setMessage(msg);
		pRes.setArgs(arguments);
		
		return pRes;
	}
	
	public ParserResponse defaultFailResponse() {
		return parserFailResponse(Parser.PE_MSG_FORMATO_INCORRECTO, null);
	}


	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}
	
	public Object getArg(String argStr) {
		return args.get(argStr);
	}

}
