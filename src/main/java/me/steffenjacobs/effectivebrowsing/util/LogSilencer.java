package me.steffenjacobs.effectivebrowsing.util;

/** @author Steffen Jacobs */
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogSilencer extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		String scheduled = event.getMDCPropertyMap().get("scheduled");
		if ("true".equals(scheduled)) {
			return FilterReply.DENY;
		} else {
			return FilterReply.NEUTRAL;
		}
	}
}
