import org.schors.lopds.indexer.catalog.AuthorCatalog;

import java.util.TreeMap;

/**
 * Created by flicus on 11.09.14.
 */
public class AuthorCatalogTest {
    @org.junit.Before
    public void setUp() throws Exception {


    }

    @org.junit.After
    public void tearDown() throws Exception {


    }

    @org.junit.Test
    public void testAddition() throws Exception {
        AuthorCatalog ac = new AuthorCatalog();
        ac.indexAuthor("Пушкин", 1);
        ac.indexAuthor("Шишкин", 2);
        ac.indexAuthor("Пушков",3);
        ac.indexAuthor("Путин",4);
        ac.indexAuthor("Павлинов",5);
        ac.indexAuthor("Пушкина",6);
        ac.indexAuthor("Пушкинадзе",7);
        ac.indexAuthor("Пу",8);
        ac.indexAuthor("Пуш",9);
        ac.indexAuthor("Пупкин",10);

    }

}
