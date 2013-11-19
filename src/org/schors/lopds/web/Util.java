package org.schors.lopds.web;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.File;

public class Util {

    public static ContentType getContentType(File file) {
        ContentType result = null;
        String extension = null;
        int dotIndex = file.getAbsolutePath().lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = file.getAbsolutePath().substring(dotIndex + 1, (int) file.getAbsolutePath().length() - 1);
        }
        if (extension != null) {
            switch (extension) {
                case "html":
                case "htm":
                    result = ContentType.create("text/html", "UTF-8");
                    break;
                case "css":
                    result = ContentType.create("text/css", "UTF-8");
                    break;
                case "js":
                    result = ContentType.create("text/javascript", "UTF-8");
                    break;
            }
        }
        return result;
    }

    public static StringEntity makeMessageBody(String text) {
        return new StringEntity(String.format("<html><body><h1>%s</h1></body></html>", text), ContentType.create("text/html", "UTF-8"));
    }
}
