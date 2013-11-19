package org.schors.lopds.xml;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class Entry {

    private Element id;
    private Element title;
    private Element updated;
    private Element content;
    private List<Element> links = new ArrayList<>();

    public Entry() {
    }

    public Entry setId(String id) {
        this.id = new Element("id");
        this.id.addContent(id);
        return this;
    }

    public Entry setTitle(String title) {
        this.title = new Element("title");
        this.title.addContent(title);
        return this;
    }

    public Entry setUpdated(String updated) {
        this.updated = new Element("updated");
        this.updated.addContent(updated);
        return this;
    }

    public Entry setContent(String data, String type) {
        this.content = new Element("content");
        content.setAttribute("type", type);
        content.addContent(data);
        return this;
    }

    public Entry addLink(String href, String rel, String type) {
        Element link = new Element("link");
        link.setAttribute("href", href);
        link.setAttribute("rel", rel);
        link.setAttribute("type", type);
        links.add(link);
        return this;
    }

    public Entry addLink(String href, String type) {
        Element link = new Element("link");
        link.setAttribute("href", href);
        link.setAttribute("type", type);
        links.add(link);
        return this;
    }

    public Element getEntry() {
        Element entry = new Element("entry");
        entry.addContent(id);
        entry.addContent(title);
        entry.addContent(updated);
        entry.addContent(content);
        for (Element link : links) {
            entry.addContent(link);
        }
        return entry;
    }

}
