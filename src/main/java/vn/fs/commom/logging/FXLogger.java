package vn.fs.commom.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.fs.commom.utils.StringUtils;

public class FXLogger {

	private Logger logger;

	// private static final ObjectMapper mapper = new ObjectMapper();

	private static final String MESSAGE_TEMPLATE_2_PLACES = "{} {}";

	private static final String MESSAGE_TEMPLATE_FOR_API_INFO_REQ = "{} {} response={}";

	private static final String MESSAGE_TEMPLATE_FOR_API_INFO_REQ_RES = "{} {} request={}, response={}";

	private static final String MESSAGE_TEMPLATE_FOR_EXCEPTION_WITH_TRACE = "{}\r\n{}";

	private static boolean enableDebug = true;

	private static boolean enableTrace = true;

	public FXLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
		enableDebug = logger.isDebugEnabled();
		enableTrace = logger.isTraceEnabled();
	}

	public Logger getLogger() {
		return logger;
	}

	/**
	 * Log trace by format: yyyy-MM-dd hh:mm:ss [thread-name] TRACE
	 * packageName.ClassName at line [line-number]: Message
	 * 
	 * @param message
	 */
	public void logTrace(String message) {
		log(LogLevel.TRACE, getLineInfo() + ": " + message);
	}

	/**
	 * Log trace by format: yyyy-MM-dd hh:mm:ss [thread-name] TRACE
	 * packageName.ClassName at line [line-number]: Message {Param Object info}
	 * 
	 * @param format
	 * @param params
	 */
	public void logTrace(String message, Object... params) {
		log(LogLevel.TRACE, getLineInfo() + ": " + MESSAGE_TEMPLATE_2_PLACES, message, params);
	}

	/**
	 * Log debug by format: yyyy-MM-dd hh:mm:ss [thread-name] DEBUG
	 * packageName.ClassName at line [line-number]: Message
	 * 
	 * @param message
	 */
	public void logDebug(String message) {
		log(LogLevel.DEBUG, getLineInfo() + ": " + message);
	}

	/**
	 * Log debug by format: yyyy-MM-dd hh:mm:ss [thread-name] DEBUG
	 * packageName.ClassName at line [line-number]: Message {Param Object info}
	 * 
	 * @param format
	 * @param params
	 */
	public void logDebug(String message, Object... params) {
		log(LogLevel.DEBUG, getLineInfo() + ": " + MESSAGE_TEMPLATE_2_PLACES, message, params);
	}

	/**
	 * Log info by format: yyyy-MM-dd hh:mm:ss [thread-name] INFO
	 * packageName.ClassName: Message
	 * 
	 * @param message
	 */
	public void logInfo(String message) {
		log(LogLevel.INFO, getLineInfo() + ": " + message);
	}

	/**
	 * Log info by format: yyyy-MM-dd hh:mm:ss [thread-name] INFO
	 * packageName.ClassName: Message {Param Object info}
	 * 
	 * @param format
	 * @param params
	 */
	public void logInfo(String message, Object... params) {
		log(LogLevel.INFO, getLineInfo() + ": " + MESSAGE_TEMPLATE_2_PLACES, message, params);
	}

	/**
	 * Log warn by format: yyyy-MM-dd hh:mm:ss [thread-name] WARN
	 * packageName.ClassName: Message
	 * 
	 * @param message
	 */
	public void logWarn(String message) {
		log(LogLevel.WARN, getLineInfo() + ": " + message);
	}

	/**
	 * Log warn by format: yyyy-MM-dd hh:mm:ss [thread-name] WARN
	 * packageName.ClassName: Message {Param Object info}
	 * 
	 * @param format
	 * @param params
	 */
	public void logWarn(String message, Object... params) {
		log(LogLevel.WARN, getLineInfo() + ": " + MESSAGE_TEMPLATE_2_PLACES, message, params);
	}

	/**
	 * Log error by format: yyyy-MM-dd hh:mm:ss [thread-name] ERROR
	 * packageName.ClassName: Message
	 * 
	 * @param message
	 */
	public void logError(String message) {
		log(LogLevel.ERROR, getLineInfo() + ": " + message);
	}

	/**
	 * Log error by format: yyyy-MM-dd hh:mm:ss [thread-name] ERROR
	 * packageName.ClassName: Message {Param Object info}
	 * 
	 * @param format
	 * @param params
	 */
	public void logError(String message, Object... params) {
		log(LogLevel.ERROR, getLineInfo() + ": " + MESSAGE_TEMPLATE_2_PLACES, message, params);
	}

	/**
	 * Log fatal by format: yyyy-MM-dd hh:mm:ss [thread-name] ERROR
	 * packageName.ClassName: Message
	 * 
	 * @param message
	 */
	public void logFatal(String message) {
		log(LogLevel.ERROR, "FATAL ERROR: " + getLineInfo() + ": " + message);
	}

	/**
	 * Log fatal by format: yyyy-MM-dd hh:mm:ss [thread-name] FATAL
	 * packageName.ClassName: Message {Param Object info}
	 * 
	 * @param format
	 * @param params
	 */
	public void logFatal(String message, Object... params) {
		log(LogLevel.ERROR, "FATAL ERROR: " + getLineInfo() + ": " + MESSAGE_TEMPLATE_2_PLACES, message, params);
	}

	/**
	 * Log api info without param by format: yyyy-MM-dd hh:mm:ss [thread-name] TRACE
	 * packageName.ClassName: "requestMethod apiName message"
	 * 
	 * @param method
	 * @param api
	 * @param message
	 */
	public void logApi(RequestMethod method, String api) {
		log(LogLevel.INFO, MESSAGE_TEMPLATE_2_PLACES, method, api);
	}

	/**
	 * Log api info without param by format: yyyy-MM-dd hh:mm:ss [thread-name] TRACE
	 * packageName.ClassName: "requestMethod apiName message"
	 * 
	 * @param method
	 * @param api
	 * @param message
	 */
	public void logApi(RequestMethod method, String api, Object response) {
		try {
			log(LogLevel.DEBUG, MESSAGE_TEMPLATE_FOR_API_INFO_REQ, method, api, StringUtils.getJSON(response));
		} catch (Exception e) {
			log(LogLevel.DEBUG, MESSAGE_TEMPLATE_FOR_API_INFO_REQ, method, api, response);
		}
	}

	/**
	 * Log api info with params by format: yyyy-MM-dd hh:mm:ss [thread-name] TRACE
	 * packageName.ClassName: "requestMethod apiName message objectPramList(json)"
	 * 
	 * @param method
	 * @param api
	 * @param objects
	 */
	public void logApi(RequestMethod method, String api, Object request, Object response) {
		try {
			log(LogLevel.DEBUG, MESSAGE_TEMPLATE_FOR_API_INFO_REQ_RES, method, api, StringUtils.getJSON(request),
					StringUtils.getJSON(response));
		} catch (Exception e) {
			log(LogLevel.DEBUG, MESSAGE_TEMPLATE_FOR_API_INFO_REQ_RES, method, api, request, response);
		}
	}

	/**
	 * Log exception by format: yyyy-MM-dd hh:mm:ss [thread-name] ERROR
	 * packageName.ClassName: Message stackTrace
	 * 
	 * @param message
	 * @param throwable
	 */
	public void logException(String message, Throwable throwable) {
		try {
			logError(MESSAGE_TEMPLATE_FOR_EXCEPTION_WITH_TRACE, message, getStackTrace(throwable));
		} catch (Exception e) {
			logError(getStackTrace(e));
		}
	}

	/**
	 * Log exception without message yyyy-MM-dd hh:mm:ss [thread-name] ERROR
	 * packageName.ClassName: stackTrace
	 * 
	 * @param throwable
	 */
	public void logException(Throwable throwable) {
		try {
			logException(throwable.getMessage(), throwable);
		} catch (Exception e) {
			logError(getStackTrace(e));
		}
	}

	/**
	 * Log by level with param
	 * 
	 * @param level
	 * @param format
	 * @param params
	 */
	public void log(LogLevel level, String format, Object... params) {
		try {
			switch (level) {
			case TRACE:
				if (enableTrace)
					logger.trace(format, params);
				break;
			case DEBUG:
				if (enableDebug)
					logger.debug(format, params);
				break;
			case INFO:
				logger.info(format, params);
				break;
			case WARN:
				logger.warn(format, params);
				break;
			case ERROR:
				logger.error(format, params);
				break;
			case FATAL:
				logger.error("FATAL ERROR: " + format, params);
				break;
			default:
				logger.info(format, params);
				break;
			}
		} catch (Exception e) {
			logError(e.getMessage());
		}
	}

	/**
	 * Log by level without params
	 * 
	 * @param level
	 * @param message
	 */
	public void log(LogLevel level, String message) {
		try {
			switch (level) {
			case TRACE:
				if (enableTrace)
					logger.trace(message);
				break;
			case DEBUG:
				if (enableDebug)
					logger.debug(message);
				break;
			case INFO:
				logger.info(message);
				break;
			case WARN:
				logger.warn(message);
				break;
			case ERROR:
				logger.error(message);
				break;
			case FATAL:
				logger.error("FATAL ERROR: " + message);
				break;
			default:
				logger.info(message);
				break;
			}
		} catch (Exception e) {
			logError(e.getMessage());
		}
	}

	private String getStackTrace(Throwable throwable) {
		try (StringWriter stringWriter = new StringWriter(); PrintWriter printWriter = new PrintWriter(stringWriter)) {
			throwable.printStackTrace(printWriter);
			return stringWriter.toString();
		} catch (IOException e) {
			logError(e.getMessage());
		}

		return "";
	}
	/*
	 * public String getJSONObject(Object object) { if (object == null) { return
	 * "NULL"; }
	 * 
	 * try { return "{" + object.getClass().getName() + ": " +
	 * mapper.writeValueAsString(object) + "}"; } catch (Exception e) {
	 * logError(getStackTrace(e)); }
	 * 
	 * return ""; }
	 */

	private String getLineInfo() {
		return "[" + Thread.currentThread().getStackTrace()[3].getMethodName() + ":"
				+ Thread.currentThread().getStackTrace()[3].getLineNumber() + "]";
	}
}