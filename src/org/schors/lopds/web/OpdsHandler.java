package org.schors.lopds.web;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.schors.lopds.xml.BaseFeed;
import org.schors.lopds.xml.Entry;

import java.io.IOException;

public class OpdsHandler implements HttpRequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String target = request.getRequestLine().getUri();
        String[] items = target.split("/");

        if (items.length == 2 && "opds".equals(items[1])) { //root

            BaseFeed feed = new BaseFeed()
                    .setId("root")
                    .setTitle("Библиотечка пороська")
                    .setUpdated("2013-03-27T15:34:17+01:00")
                    .addLink("/opds", "start", "application/atom+xml;profile=opds-catalog;kind=navigation")
                    .addLink("/opds", "self", "application/atom+xml;profile=opds-catalog;kind=navigation")
                    .addEntry(new Entry()
                        .setUpdated("2013-03-27T15:34:17+01:00")
                        .setId("authors")
                        .setTitle("По авторам")
                        .setContent("Поиск книг по авторам", "text")
                        .addLink("/opds/authors", "application/atom+xml;profile=opds-catalog;kind=navigation"))
                    .addEntry(new Entry()
                            .setUpdated("2013-03-27T15:34:17+01:00")
                            .setId("genres")
                            .setTitle("По жанрам")
                            .setContent("Поиск книг по жанрам", "text")
                            .addLink("/opds/genres", "application/atom+xml;profile=opds-catalog;kind=navigation"));
            response.setStatusCode(HttpStatus.SC_OK);
            response.setEntity(new ByteArrayEntity(feed.build()));
            return;
        }

        if (items.length == 3 && "opds".equals(items[1])) {
            switch (items[2]) {
                case "authors": {
                    break;
                }
                case "genres": {
                    break;
                }
                default: {
                    break;
                }
            }
        }


    }
}
