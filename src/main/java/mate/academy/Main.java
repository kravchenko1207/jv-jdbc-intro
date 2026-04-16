package mate.academy;

import mate.academy.dao.BookDao;
import mate.academy.dao.BookDaoImpl;
import mate.academy.model.Book;

import java.math.BigDecimal;


public class Main {
    public static void main(String[] args) {
        BookDao bookDao = new BookDaoImpl();

        Book book1 = new Book();
        book1.setTitle("Virus");
        book1.setPrice(new BigDecimal("250.00"));

        bookDao.create(book1);

        System.out.println("Book saved " + book1);
    }
}
