package mate.academy.dao;

import mate.academy.exception.DataProcessingException;
import mate.academy.model.Book;
import mate.academy.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class BookDaoImpl implements BookDao {

    @Override
    public Book create(Book book) {
        String sql = "INSERT INTO books (title, price) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());

            int affectedRows = statement.executeUpdate();
            if (affectedRows < 1) {
                throw new DataProcessingException("Should add at least one row, but 0 rows were inserted.");
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getObject(1, Long.class);
                book.setId(id);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not add a new book " + book, e);
        }
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT id, title, price FROM books WHERE ID = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Book book = new Book();
                    book.setId(resultSet.getLong("id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setPrice(resultSet.getBigDecimal("price"));
                    return Optional.of(book);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get book by id " + id, e);
        }

    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT id, title, price FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setPrice(resultSet.getBigDecimal("price"));
                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get all books from DB", e);
        }

    }

    @Override
    public Book update(Book book) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
