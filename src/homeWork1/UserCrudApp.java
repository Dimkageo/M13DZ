package homeWork1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * A class for performing CRUD operations on user entities using HTTPURLConnection.
 */
public class UserCrudApp {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users";

    private static final Gson gson = new Gson();

    /**
     * Creates a new user entity by sending a POST request to the API.
     * Создает новый пользовательский объект, отправляя запрос POST в API.
     *
     * @return The created user entity.
     * @return Созданный пользовательский объект.
     */
    public static UserEntity createUser() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String requestBody = "{\n" +
                    "    \"name\": \"Ragnar Lothbrok\",\n" +
                    "    \"username\": \"frenchConquerent\",\n" +
                    "    \"email\": \"rlothbrok@example.com\",\n" +
                    "    \"address\": {\n" +
                    "      \"street\": \"Hoeger Mall\",\n" +
                    "      \"suite\": \"Apt. 692\",\n" +
                    "      \"city\": \"South Elvis\",\n" +
                    "      \"zipcode\": \"53919-4257\",\n" +
                    "      \"geo\": {\n" +
                    "        \"lat\": \"29.4572\",\n" +
                    "        \"lng\": \"-164.2990\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }";

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_CREATED == responseCode) {
                return getUserEntity(connection);
            }
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException("Something goes wrong");
        }
        return null;
    }

    /**
     * Updates an existing user entity by sending a PUT request to the API.
     * Обновляет существующий пользовательский объект, отправляя запрос PUT в API.
     *
     * @param id The ID of the user entity to update.
     * @param id Идентификатор объекта пользователя для обновления.
     * @return The updated user entity.
     * @return Обновленный пользовательский объект.
     */
    public static UserEntity updateUser(int id) {
        try {
            URL url = new URL(BASE_URL + "/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String requestBody = "{\"id\":5,\"name\":\"Bjorn Ironside\",\"username\":\"ironside\",\"email\":\"ragnarsson@example.com\",\"address\": {\"street\": \"Hoeger Mall\",\"suite\": \"Apt. 692\",\"city\": \"South Elvis\",\"zipcode\": \"53919-4257\",\"geo\": {\"lat\": \"29.4572\",\"lng\": \"-164.2990\"}}}";

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                return getUserEntity(connection);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deletes a user entity by sending a DELETE request to the API.
     * Удаляет сущность пользователя, отправляя запрос DELETE в API.
     *
     * @param id The ID of the user entity to delete.
     * @param id Идентификатор удаляемой сущности пользователя.
     * @return {@code true} if the deletion is successful, {@code false} otherwise.
     * @return {@code true}, если удаление прошло успешно, {@code false} в противном случае.
     */
    public static boolean deleteUser(int id) {
        try {
            URL url = new URL(BASE_URL + "/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves a list of all user entities from the API.
     * Извлекает список всех пользовательских сущностей из API.
     *
     * @return A list of user entities.
     * @return Список пользовательских сущностей.
     */
    public static List<UserEntity> getUsers() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Type userListType = new TypeToken<List<UserEntity>>() {
                    }.getType();
                    return gson.fromJson(response.toString(), userListType);
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves a user entity by its ID from the API.
     * Извлекает сущность пользователя по ее идентификатору из API.
     *
     * @param id The ID of the user entity to retrieve.
     * @param id Идентификатор объекта пользователя, который необходимо получить.
     * @return An optional containing the user entity if found, or empty optional if not found.
     * @return Необязательный элемент, содержащий сущность пользователя, если он найден, или пустой необязательный элемент, если он не найден.
     */
    public static Optional<UserEntity> getUserById(int id) {
        try {
            URL url = new URL(BASE_URL + "/" + id);
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
                    UserEntity user = gson.fromJson(response.toString(), UserEntity.class);
                    return Optional.ofNullable(user);
                }
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Retrieves a user entity by its username from the API.
     * Извлекает сущность пользователя по имени пользователя из API.
     *
     * @param username The username of the user entity to retrieve.
     * @param username Имя пользователя объекта пользователя, который необходимо получить.
     * @return An optional containing the user entity if found, or empty optional if not found.
     * @return Необязательный элемент, содержащий сущность пользователя, если он найден, или пустой необязательный элемент, если он не найден.
     */
    public static Optional<UserEntity> getUserByUsername(String username) {
        try {
            URL url = new URL(BASE_URL + "?username=" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Type userListType = new TypeToken<List<UserEntity>>() {
                    }.getType();
                    List<UserEntity> users = gson.fromJson(response.toString(), userListType);
                    if (!users.isEmpty()) {
                        return Optional.of(users.get(0));
                    }
                }
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private static UserEntity getUserEntity(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return gson.fromJson(response.toString(), UserEntity.class);
        }
    }
}