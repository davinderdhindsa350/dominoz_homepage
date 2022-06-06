package com.jubl.food.nextgendiscovery.service.util;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XSSRequestWrapper extends HttpServletRequestWrapper {

    /**
     * Static class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XSSRequestWrapper.class);

    /**
     * @param servletRequest
     */
    public XSSRequestWrapper(final HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    /**
     *@param parameter
     */
    @Override
    public String[] getParameterValues(final String parameter) {
        LOGGER.info("InParameterValues .. parameter .......");
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

    /**
     *@param parameter
     */
    @Override
    public String getParameter(final String parameter) {
        LOGGER.info("InParameter .. parameter .......");
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        LOGGER.info("InParameter RequestWrapper ........ value .......");
        return stripXSS(value);
    }

    /**
     *@param  name
     */
    @Override
    public String getHeader(final String name) {
        LOGGER.info("InHeader .. parameter .......");
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        LOGGER.info("InHeader RequestWrapper ........... value ....");
        return stripXSS(value);
    }

    private String stripXSS(final String valueIn) {
        LOGGER.info("InnXSS RequestWrapper ...............{}", valueIn);
        if (valueIn != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
           String value = valueIn.replaceAll("", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            LOGGER.info("OutnXSS RequestWrapper ........ value .......{}", value);
            return value;
        }
        return valueIn;
    }
}
