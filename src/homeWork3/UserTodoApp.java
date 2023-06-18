package homeWork3;

import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.lang.reflect.Type;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.List;

public class UserTodoApp {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        int userId = 1; // Задайте ідентифікатор користувача, для якого потрібно вивести відкриті задачі
        List<TodoEntity> todos = getOpenTodosByUserId(userId);
        if (todos != null) {
            for (TodoEntity todo : todos) {
                System.out.println(todo);
            }
        } else {
            System.out.println("Не вдалося отримати список відкритих задач для користувача з ID: " + userId);
        }
    }

    /**
     * запит до API, отримує список всіх задач для користувача з вказаним ідентифікатором
     */
    public static List<TodoEntity> getOpenTodosByUserId(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId + "/todos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Type todoListType = new TypeToken<List<TodoEntity>>() {}.getType();
                    List<TodoEntity> todos = gson.fromJson(response.toString(), todoListType);
                    return filterOpenTodos(todos);
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * приймає список задач і видаляє з нього ті, які вже виконані
     */
    private static List<TodoEntity> filterOpenTodos(List<TodoEntity> todos) {
        todos.removeIf(todo -> todo.isCompleted());
        return todos;
    }
}

