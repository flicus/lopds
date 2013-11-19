package org.schors.lopds.xml;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseFeed {

    private Element root;
    private Element id;
    private Element title;
    private Element updated;
    private List<Element> links = new ArrayList<>();
    private List<Entry> entries = new ArrayList<>();

    public BaseFeed() {

    }

    public BaseFeed setId(String id) {
        this.id = new Element("id");
        this.id.addContent(id);
        return this;
    }

    public BaseFeed setTitle(String title) {
        this.title = new Element("title");
        this.title.addContent(title);
        return this;
    }

    public BaseFeed setUpdated(String updated) {
        this.updated = new Element("updated");
        this.updated.addContent(updated);
        return this;
    }

    public BaseFeed addLink(String href, String rel, String type) {
        Element link = new Element("link");
        link.setAttribute("href", href);
        link.setAttribute("rel", rel);
        link.setAttribute("type", type);
        links.add(link);
        return this;
    }

    public BaseFeed addEntry(Entry entry) {
        this.entries.add(entry);
        return this;
    }

    public byte[] build() {
        root = new Element("feed", Namespace.Atom.getJdomNamespace());
        root.addNamespaceDeclaration(Namespace.Dc.getJdomNamespace());
        root.addNamespaceDeclaration(Namespace.Os.getJdomNamespace());
        root.addNamespaceDeclaration(Namespace.Opds.getJdomNamespace());
        root.addContent(id);
        root.addContent(title);
        root.addContent(updated);
        for (Element link : links) {
            root.addContent(link);
        }
        for (Entry entry : entries) {
            root.addContent(entry.getEntry());
        }
        Document doc = new Document(root);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            outputter.output(doc, baos);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return baos.toByteArray();
    }
}
