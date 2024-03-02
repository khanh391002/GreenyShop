package vn.fs.commom.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.ObjectMapper;


public class StringUtils extends org.springframework.util.StringUtils {

	private StringUtils() {
	}

	public static String join(List<String> list, String separator) {
		return org.apache.commons.lang.StringUtils.join(list, separator);
	}

	public static boolean isNullOrEmpty(String text) {
		return text == null || text.length() == 0;
	}
	
	public static <T> String join(List<T> list, char ch) {
		return org.apache.commons.lang3.StringUtils.join(list, ch);
	}

	public static String getJSON(Object obj) {
		String jsonInString = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonInString = mapper.writeValueAsString(obj);
			return (obj == null) ? "NULL" : jsonInString;
		} catch (Exception e) {
			return (obj == null) ? "NULL" : obj.toString();
		}
	}

	public static String getCRC32Checksum(String data) {
		if (data == null)
			data = "";
		CRC32 fileCRC32 = new CRC32();
		fileCRC32.update(data.getBytes());
		return String.format(Locale.US, "%08x", fileCRC32.getValue());
	}

	public static String removeHTMLTags(String htmlText) {
		if (htmlText == null)
			return null;

		return Jsoup.parse(htmlText).text();
	}

	public static String encodeBase64(String text) {
		if (text == null)
			return null;

		return Base64.getEncoder().encodeToString(text.getBytes());
	}

	public static String decodeBase64(String base64Text) {
		if (base64Text == null)
			return null;

		return new String(Base64.getDecoder().decode(base64Text));
	}

	public static String getGroupedText(String text, int groupSize, boolean fillZero) {
		if (text == null || groupSize < 0 || text.length() <= groupSize)
			return text;

		List<String> parts = new ArrayList<>();
		String part;
		while (text.length() > 0) {
			part = text.substring(0, text.length() > groupSize ? groupSize : text.length());
			if (text.length() > groupSize)
				text = text.substring(groupSize);
			else
				text = "";
			parts.add(part);
		}
		text = "";
		for (int i = 0; i < parts.size(); i++) {
			part = parts.get(i);
			if (i == parts.size() - 1) {
				while (part.length() < groupSize)
					part = part + "0";

				text += part;
			} else
				text += part + "-";
		}

		return text;
	}

	public static String capitalizeFirstLetter(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
	
	//Remove Diacritical Marks
	public static String inCombiningDiacriticalMarks(String text) {
		String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
	}
}