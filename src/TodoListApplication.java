import database.DbContext;
import entity.Todo;
import service.TodoService;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

public class TodoListApplication {

    public static final String dbUrl = "jdbc:postgresql://localhost:5432/demo";
    public static final String dbUsername = "postgres";
    public static final String dbPassword = "root";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DbContext dbContext = new DbContext();
        Connection connection = dbContext.connect(dbUrl, dbUsername, dbPassword);
        TodoService todoService = new TodoService();
//        todoService.createTable(connection);

        if (connection != null) {
            boolean exit = false;
            while (!exit) {
                menu();
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1: {
                        System.out.println("*** Add Todo ***");
                        System.out.print("Enter title: ");
                        String emptyLine = scanner.nextLine();
                        String title = scanner.nextLine();
                        System.out.print("Enter description: ");
                        String description = scanner.nextLine();
                        Todo todo = new Todo();
                        todo.setTitle(title);
                        todo.setDescription(description);
                        todo.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        todoService.addTodo(connection, todo);
                        System.out.println("Todo added.");
                        break;
                    }
                    case 2: {
                        System.out.println("*** All Todos ***");
                        todoService.findAll(connection).forEach(System.out::println);
                        break;
                    }
                    case 3: {
                        System.out.println("*** Find Todo By ID ***");
                        System.out.print("Enter Todo ID: ");
                        int id = scanner.nextInt();
                        checkNull(connection, todoService, id);
                        break;
                    }
                    case 4: {
                        System.out.println("*** Edit Todo ***");
                        System.out.print("Enter Todo ID: ");
                        int id = scanner.nextInt();
                        if (checkNull(connection, todoService, id)) {
                            todoService.findById(connection, id);
                            System.out.print("Enter new title: ");
                            String title = scanner.next();
                            System.out.print("Enter new description: ");
                            String emptyLine = scanner.nextLine();
                            String description = scanner.nextLine();
                            System.out.print("Enter new status (true or false) : ");
                            boolean isDone = scanner.nextBoolean();
                            todoService.updateTodo(connection, id, title, description, isDone);
                            System.out.println("Todo updated.");
                        }
                        break;
                    }
                    case 5: {
                        System.out.println("*** Delete Todo ***");
                        System.out.print("Enter Todo ID: ");
                        int id = scanner.nextInt();
                        if (checkNull(connection, todoService, id)) {
                            todoService.delete(connection, id);
                            System.out.println("Todo deleted.");
                        }
                        break;
                    }
                    case 6: {
                        System.out.println("*** View Todos By Status ***");
                        System.out.println("For completed todos enter \'true\', for active todos enter \'false\': ");
                        boolean isDone = scanner.nextBoolean();
                        todoService.getTodoWithStatus(connection, isDone).forEach(System.out::println);
                        break;
                    }
                    case 0: {
                        exit = true;
                        System.out.println("Application closing...");
                        System.exit(0);
                        break;
                    }
                }

            }
        }
    }

    public static void menu() {
        System.out.println("\n*** Todo List Application ***\n");
        System.out.println("1. Add Todo");
        System.out.println("2. View All Todo");
        System.out.println("3. Get Todo By ID");
        System.out.println("4. Edit Todo");
        System.out.println("5. Delete Todo");
        System.out.println("6. View Todos By Status");
        System.out.println("0. Exit");
    }

    public static boolean checkNull(Connection connection, TodoService todoService, int id) {
        Todo todo = todoService.findById(connection, id);
        if (todo.getTitle() != null) {
            System.out.println(todo);
            return true;
        } else {
            System.out.println("Todo not found.");
            return false;
        }
    }
}
