package org.schors.lopds.xml;


public enum Namespace {
    Atom("", "http://www.w3.org/2005/Atom"),
    Opds("opds", "http://opds-spec.org/2010/catalog"),
    Opf("opf", "http://www.idpf.org/2007/opf"),
    Dc("dc", "http://purl.org/dc/elements/1.1/"),
    DcTerms("dcterms", "http://purl.org/dc/terms"),
    Calibre("calibre", "http://calibre.kovidgoyal.net/2009/metadata"),
    Xhtml("xhtml", "http://www.w3.org/1999/xhtml"),
    Os("os", "http://a9.com/-/spec/opensearch/1.1/");

    private org.jdom2.Namespace jdomNamespace;

    private Namespace(String prefix, String uri) {
        this.jdomNamespace = org.jdom2.Namespace.getNamespace(prefix, uri);
    }

    public org.jdom2.Namespace getJdomNamespace() {
        return jdomNamespace;
    }
}
