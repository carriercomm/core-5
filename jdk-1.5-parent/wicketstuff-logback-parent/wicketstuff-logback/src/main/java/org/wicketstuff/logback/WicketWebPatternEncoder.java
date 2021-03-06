/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wicketstuff.logback;

import javax.servlet.Filter;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

/**
 * <p>
 * Extends {@link PatternLayoutEncoder} with a new formatting converter: "web". By using this
 * encoder and the "%web" placeholder in the pattern wicket apps can produce web information
 * (method, url, session id, ...) in their log messages. If there is no request information
 * available the placeholder is replaced with an empty string in the final message.
 * </p>
 * <p>
 * Example logback configuration:
 * 
 * <pre>
 * <code>
 * 	&lt;appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
 * 		&lt;encoder class="org.wicketstuff.logback.WicketWebPatternEncoder">
 * 			&lt;charset>UTF-8&lt;/charset>
 * 			&lt;pattern>%d|%p|%t|%c{36}|%r|<b>%web</b>%n\t%caller{1}\t%m%n%xEx&lt;/pattern>
 * 		&lt;/encoder>
 * 		&lt;filter class="ch.qos.logback.classic.filter.ThresholdFilter">
 * 			&lt;level>debug&lt;/level>
 * 		&lt;/filter>
 * 	&lt;/appender>
 * </code>
 * </pre>
 * 
 * The above will result in log messages like this:
 * 
 * <pre>
 * <code>
 * 2011-02-21 14:18:26,281|INFO|"http-nio-8080"-exec-2|o.w.logback.examples.HomePage|28066|GET http://localhost:8080/wicketstuff-logback-examples/?null null null 127.0.0.1:59363 127.0.0.1:8080 null Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.13 (KHTML, like Gecko) Chrome/9.0.597.98 Safari/534.13
 * 	Caller+0	 at org.wicketstuff.logback.examples.HomePage.<init>(HomePage.java:19)
 * 	Logging is good - said the lumberjack.
 * </code>
 * </pre>
 * 
 * The full message format is available in the javadoc of {@link AbstractWebFormattingConverter}.
 * 
 * </p>
 * <p>
 * There are similar solutions using {@link Filter}-s, MDC and NDC to solve the same task (like
 * logback's own MDCInsertingServletFilter or spring's AbstractRequestLoggingFilter and its
 * subclasses). The difference from those is performance (and the amount of information provided).
 * Those filters are always collecting information for every request however usually only a small
 * portion of requests result in actual logging. This solution only gets invoked when the logging
 * event is indeed producing a log message. It is on the "other side of the fence".
 * </p>
 * 
 * @author akiraly
 */
public class WicketWebPatternEncoder extends PatternLayoutEncoder
{
	static
	{
		PatternLayout.defaultConverterMap.put("web", WicketWebFormattingConverter.class.getName());
	}
}
