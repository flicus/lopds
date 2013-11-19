package org.schors.lopds.web;

import org.apache.http.*;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RootHandler implements HttpRequestHandler {

    private static final Pattern pattern = Pattern.compile("/lib/(.+?)/.+");

    private StaticContentHandler staticContentHandler;
    private ApiHandler apiHandler;

    public RootHandler(String docRoot) {
        staticContentHandler = new StaticContentHandler(docRoot);
        apiHandler = new ApiHandler();
    }

    public static Map<String, String> parseQueryString(final String url, String token) throws UnsupportedEncodingException {
        final Map<String, String> qps = new TreeMap<String, String>();
        final StringTokenizer pairs = new StringTokenizer(url, token);
        while (pairs.hasMoreTokens()) {
            final String pair = pairs.nextToken();
            final StringTokenizer parts = new StringTokenizer(pair, "=");
            final String name = URLDecoder.decode(parts.nextToken(), "ISO-8859-1");
            final String value = URLDecoder.decode(parts.nextToken(), "ISO-8859-1");
            qps.put(name, value);
        }
        return qps;
    }


    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        System.out.println("Root handler");
        String target = request.getRequestLine().getUri();
        String[] urlStrings = target.split("\\?");

        if ("/lib/".equals(urlStrings[0])) {
            HttpRequest r = new BasicHttpRequest("get", "/lib/static/1.html");
            context.setAttribute("base", "/lib/static/1.html");
            staticContentHandler.handle(r, response, context);
        } else {
            context.setAttribute("base", urlStrings[0]);
            Matcher m = pattern.matcher(target);
            if (m.find()) {
                target = m.group(1);
            }
            switch (target) {
                case "api":
                    apiHandler.handle(request, response, context);
                    break;
                case "static":
                    staticContentHandler.handle(request, response, context);
                    break;
                default:
                    response.setStatusCode(HttpStatus.SC_SERVICE_UNAVAILABLE);
                    response.setEntity(Util.makeMessageBody("Unknown service requested"));
            }
        }
    }
}
