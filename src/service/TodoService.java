package service;

import entity.Todo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TodoService {

    public void addTodo(Connection connection, Todo todo) {
        String sql = "INSERT INTO task(title, description, create_date)" +
                "VALUES(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, todo.getTitle());
            statement.setString(2, todo.getDescription());
            LocalDateTime localDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            statement.setTimestamp(3, timestamp);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Todo> findAll(Connection connection) {
        String sql = "SELECT * FROM task WHERE is_delete = false";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            List<Todo> todoList = new ArrayList<>();
            while (resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getInt("id"));
                todo.setTitle(resultSet.getString("title"));
                todo.setDescription(resultSet.getString("description"));
                todo.setCreateDate(resultSet.getTimestamp("create_date"));
                todoList.add(todo);
            }

            return todoList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Todo findById(Connection connection, int id) {
        String sql = "SELECT * FROM task WHERE id = ? AND is_delete = false";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Todo todo = new Todo();
            while (resultSet.next()) {
                todo.setId(resultSet.getInt("id"));
                todo.setTitle(resultSet.getString("title"));
                todo.setDescription(resultSet.getString("description"));
                LocalDateTime localDateTime = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                todo.setCreateDate(timestamp);
            }
            return todo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Todo> getTodoWithStatus(Connection connection, boolean isDone) {
        String sql = "SELECT * FROM task WHERE is_done = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isDone);
            ResultSet resultSet = statement.executeQuery();
            List<Todo> todoList = new ArrayList<>();
            while (resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getInt("id"));
                todo.setTitle(resultSet.getString("title"));
                todo.setDescription(resultSet.getString("description"));
                todo.setCreateDate(resultSet.getTimestamp("create_date"));
                todoList.add(todo);
            }
            return todoList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Connection connection, int id) {
        var sql = "UPDATE task SET is_delete = 'true' WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTodo(Connection connection, int id, String title, String description, boolean isDone) {
        String sql = "UPDATE task SET title = ?, description = ?, is_done = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setBoolean(3, isDone);
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable(Connection connection) {
        String sql = """
                CREATE TABLE IF NOT EXISTS task(
                id SERIAL PRIMARY KEY,
                title VARCHAR(50) NOT NULL,
                description VARCHAR(255),
                create_date DATE,
                is_done BOOLEAN NOT NULL DEFAULT FALSE,
                is_delete BOOLEAN NOT NULL DEFAULT FALSE
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
