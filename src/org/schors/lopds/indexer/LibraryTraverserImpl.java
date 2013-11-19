package org.schors.lopds.indexer;

import com.fasterxml.aalto.stax.InputFactoryImpl;
import org.schors.lopds.model.Book;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;


public class LibraryTraverserImpl implements LibraryTraverser {

    public static String newline = System.getProperty("line.separator");
    private File libraryFolder;

    private ZipFileList zipFileList;
    private InputFactoryImpl inputFactory = new InputFactoryImpl();
    private FileWriter w;
    private final static Set<String> expectedElements = new HashSet<>();
    private final static Set<String> elementsToSkip = new HashSet<>();

    @Override
    public File[] init(File libraryFolder, WorkObserver observer)
    {
        expectedElements.add(TITLE_INFO);
        expectedElements.add(GENRE);
        expectedElements.add(FIRST_NAME);
        expectedElements.add(LAST_NAME);
        expectedElements.add(MIDDLE_NAME);
        expectedElements.add(BOOK_TITLE);
        expectedElements.add(LANG);

        elementsToSkip.add("stylesheet");
        elementsToSkip.add("annotation");
        elementsToSkip.add("poem");
        elementsToSkip.add("stanza");
        elementsToSkip.add("cite");
        elementsToSkip.add("p");
        elementsToSkip.add("keywords");
        elementsToSkip.add("coverpage");
        elementsToSkip.add("translator");

    //    wstxInputFactory.configureForSpeed();
//        wstxInputFactory.setProperty(WstxInputFactory.IS_VALIDATING, Boolean.FALSE);
//        wstxInputFactory.setProperty(WstxInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
//        wstxInputFactory.setProperty(WstxInputFactory.SUPPORT_DTD, Boolean.FALSE);
        if (!libraryFolder.isDirectory())
            throw new IllegalArgumentException("Should be directory: " + libraryFolder.getAbsolutePath());
        try {
            w = new FileWriter("d:\\tmp\\bugfiles.log");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        zipFileList = new ZipFileList(libraryFolder.listFiles(), observer);
        return libraryFolder.listFiles();
    }

    /*
    woodstock
    189702
    22953
    23068
    23040

    aalto
    17267
    17278
    17202



     */

    @Override
    public Book getNextBook() {
        Book book = null;
        ZipEntry entry = null;

        do {
            entry = zipFileList.nextZipEntry();
            if (entry != null)
                book = readBook2(entry);
        } while (book == null && entry != null);

        return book;
    }

    private static final String DESCRIPTION = "description";
    private static final String TITLE_INFO = "title-info";
    private static final String GENRE = "genre";
    private static final String FIRST_NAME = "first-name";
    private static final String MIDDLE_NAME = "middle-name";
    private static final String LAST_NAME = "last-name";
    private static final String BOOK_TITLE = "book-title";
    private static final String LANG = "lang";

    //fb2-119691-132107.zip

    private Book readBook2(ZipEntry entry) {
        if (entry.getName().equals("385907.fb2")) {
            System.out.print("aaa");
        }
        Book book = null;
        InputStream in = null;
        XMLStreamReader reader = null;
        try {
            in = zipFileList.getCurrentZipFile().getInputStream(entry);
            reader = inputFactory.createXMLStreamReader(in, "UTF-8");
            //reader = (XMLStreamReader2) wstxInputFactory.createXMLStreamReader(in);
            boolean done = false;
            int event, level = 0;
            String element, text;
            while (reader.hasNext() && !done) {
                event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    element =  reader.getLocalName();
                    if (elementsToSkip.contains(element)) {
                        //reader.skipElement();
                        level ++;
                        continue;
                    }
                    if (!expectedElements.contains(element) || level > 0) continue;
                    if (TITLE_INFO.equals(element)) {
                        book = new Book();
                        book.setFileName(entry.getName());
                        book.setArchive(zipFileList.getCurrentZipFile().getName());
                        continue;
                    }
                    if (GENRE.equals(element)) {
                        text = reader.getElementText();
//                        if ("105".equals(text)) {
//                            System.out.print(text);
//                        }
                        book.setGenre(text);
                        continue;
                    }
                    if (FIRST_NAME.equals(element)) {
                        text = reader.getElementText();
                        book.setFirstName(text);
                        continue;
                    }
                    if (MIDDLE_NAME.equals(element)) {
                        text = reader.getElementText();
                        book.setMiddleName(text);
                        continue;
                    }
                    if (LAST_NAME.equals(element)) {
                        text = reader.getElementText();
                        book.setLastName(text);
                        continue;
                    }
                    if (BOOK_TITLE.equals(element)) {
                        text = reader.getElementText();
                        book.setTitle(text);
                        continue;
                    }
                    if (LANG.equals(element)) {
                        text = reader.getElementText();
                        book.setLanguage(text);
                        continue;
                    }
                    continue;
                }
                if (event == XMLStreamConstants.END_ELEMENT) {
                    element = reader.getLocalName();
                    if (elementsToSkip.contains(element)) {
                        //reader.skipElement();
                        level --;
                        continue;
                    }
                    if (TITLE_INFO.equals(element)) {
                        done = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                w.write(zipFileList.getCurrentZipFile().getName()+" : "+entry.getName()+newline);
                w.flush();
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } finally {
            if (reader != null) try {
                reader.close();
                if (in != null) in.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return book;
    }

//    private Book readBook(ZipEntry entry) {
//        Book book = null;
//        InputStream in = null;
//        XMLEventReader reader = null;
//        try {
//            in = zipFileList.getCurrentZipFile().getInputStream(entry);
//            reader = xmlInputFactory.createXMLEventReader(in);
//
//            boolean done = false;
//            while (reader.hasNext() && !done) {
//                XMLEvent event = reader.nextEvent();
//                if (event.isStartElement()) {
//                    if (event.asStartElement().getName().getLocalPart().equals(DESCRIPTION)) {
//                        book = new Book();
//                        book.setFileName(entry.getName());
//                        book.setArchive(zipFileList.getCurrentZipFile().getName());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart().equals(GENRE)) {
//                        event = reader.nextEvent();
//                        if (event.isCharacters())
//                            book.setGenre(event.asCharacters().getData());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart().equals(FIRST_NAME)) {
//                        event = reader.nextEvent();
//                        if (event.isCharacters())
//                            book.setFirstName(event.asCharacters().getData());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart().equals(MIDDLE_NAME)) {
//                        event = reader.nextEvent();
//                        if (event.isCharacters())
//                            book.setMiddleName(event.asCharacters().getData());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart().equals(LAST_NAME)) {
//                        event = reader.nextEvent();
//                        if (event.isCharacters())
//                            book.setLastName(event.asCharacters().getData());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart().equals(BOOK_TITLE)) {
//                        event = reader.nextEvent();
//                        if (event.isCharacters())
//                            book.setTitle(event.asCharacters().getData());
//                        continue;
//                    }
//
//                    if (event.asStartElement().getName().getLocalPart().equals(LANG)) {
//                        event = reader.nextEvent();
//                        if (event.isCharacters())
//                            book.setLanguage(event.asCharacters().getData());
//                        continue;
//                    }
//
//                }
//
//                if (event.isEndElement()) {
//                    EndElement endElement = event.asEndElement();
//                    if (endElement.getName().getLocalPart() == (DESCRIPTION)) {
//                        done = true;
//                    }
//                }
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            try {
//                w.write(zipFileList.getCurrentZipFile().getName()+" : "+entry.getName()+newline);
//                w.flush();
//            } catch (IOException e1) {
//                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        } catch (XMLStreamException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            try {
//                w.write(zipFileList.getCurrentZipFile().getName()+" : "+entry.getName()+newline);
//                w.flush();
//            } catch (IOException e1) {
//                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        } finally {
//            try {
//                reader.close();
//                in.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (entry.getName().equals("392999.fb2")) {
//            System.out.print("aaa");
//
//        }
//        return book;
//    }

    @Override
    public boolean isMoreBooksAvailable() {
        return false;
    }

    public ZipFileList getZipFileList() {
        return zipFileList;
    }

}
