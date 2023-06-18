package homeWork2;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CommentExporter {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        int userId = 1;
        int postId = getLastPostId(userId);
        exportComments(userId, postId);
    }

    /**
     * отримує останній ID поста для заданого користувача.
     */
    private static int getLastPostId(int userId) {
        try {
            URL url = new URL(BASE_URL + "/users/" + userId + "/posts");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = getResponseString(connection);
                Post[] posts = gson.fromJson(response, Post[].class);
                if (posts.length > 0) {
                    return posts[posts.length - 1].getId();
                }
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * експортує коментарі до поста з вказаним ID користувача та ID поста.
     */
    private static void exportComments(int userId, int postId) {
        try {
            URL url = new URL(BASE_URL + "/posts/" + postId + "/comments");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = getResponseString(connection);
                Comment[] comments = gson.fromJson(response, Comment[].class);
                writeCommentsToFile(userId, postId, comments);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getResponseString(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        return response.toString();
    }

    /**
     * записує отримані коментарі у файл з назвою "user-X-post-Y-comments.json", де X - ID користувача, Y - номер поста.
     */
    private static void writeCommentsToFile(int userId, int postId, Comment[] comments) {
        String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(gson.toJson(comments));
            System.out.println("Comments exported to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


