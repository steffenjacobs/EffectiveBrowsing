package me.steffenjacobs.effectivebrowsing.parser;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Component;

/** @author Steffen Jacobs */

@Component
public class ParsingService {

	private static final Calendar cal = Calendar.getInstance();

	SimpleDateFormat f1 = new SimpleDateFormat("yyyy-mm-dd");

	public Date parseYear(Tag tag) {

		String tagYear = tag.getFirst(FieldKey.YEAR);
		if (tagYear != null && !"".equals(tagYear)) {
			try {
				int year = Integer.parseInt(tagYear);
				cal.setTimeInMillis(0);
				cal.set(Calendar.YEAR, year);
				return cal.getTime();
			} catch (NumberFormatException e) {
			}

			try {
				return f1.parse(tagYear);
			} catch (ParseException e) {
			}

			try {
				byte[] yearBytes = tag.getFields(FieldKey.YEAR).get(0).getRawContent();
				if (yearBytes.length == 21) {
					String cp1251String = new String(yearBytes, Charset.forName("CP1251"));
					final int CP1251_OFFSET = 14;
					cp1251String = cp1251String.substring(CP1251_OFFSET, CP1251_OFFSET + 1) + cp1251String.substring(CP1251_OFFSET + 2, CP1251_OFFSET + 3)
							+ cp1251String.substring(CP1251_OFFSET + 4, CP1251_OFFSET + 5) + cp1251String.substring(CP1251_OFFSET + 6, CP1251_OFFSET + 7);

					cal.set(Calendar.YEAR, Integer.parseInt(cp1251String));
					return cal.getTime();
				}
			} catch (UnsupportedEncodingException | KeyNotFoundException | StringIndexOutOfBoundsException | NumberFormatException e) {
			}
		}
		return null;
	}

}
