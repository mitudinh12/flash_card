package com.flash_card.framework;
import java.net.*;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TriviaQuestionGenerator {
    private static TriviaQuestionGenerator instance;
    private boolean isLoading;
    List<String> fakeAnswers;

    private TriviaQuestionGenerator() {
        fakeAnswers = new ArrayList<>();
    }

    public static synchronized TriviaQuestionGenerator getInstance() {
        if (instance == null) {
            instance = new TriviaQuestionGenerator();
        }
        return instance;
    }
    //Generate fake questions from the API based on the topic
    public List<String> generateFakeAnswers(String topic) {
        isLoading = true;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://quizmania-api.p.rapidapi.com/trivia-by-category?category=" + topic.substring(0, 1).toUpperCase() + topic.substring(1)))
                    .header("x-rapidapi-key", "a90482b33dmsh1b335d7d732c4c1p12cd98jsn5a662aaaa9e0")
                    .header("x-rapidapi-host", "quizmania-api.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            for (Object fakeAnswer : jsonResponse.getJSONArray("answers")) {
                fakeAnswers.add((String) fakeAnswer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoading = false;
        fakeAnswers = fakeAnswers.subList(0,3);
        return fakeAnswers;
    }


    public List<String> getTopics() {
        List<String> topics = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://quizmania-api.p.rapidapi.com/categories"))
                    .header("x-rapidapi-key", "a90482b33dmsh1b335d7d732c4c1p12cd98jsn5a662aaaa9e0")
                    .header("x-rapidapi-host", "quizmania-api.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.body());
            // Extract categories array and convert it to List<String>
            for (Object category : jsonResponse.getJSONArray("categories")) {
                topics.add((String) category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
        //Output: [Geography, Literature, Science, History, Art, Music, Technology, Sports, Entertainment, Biology, Mathematics, Food, Mythology, Astronomy, YouTubers/Streamers, Language, Animals, Culture, Landmarks, Economics, Linguistics, Chemistry, Physics, Philosophy, Geology, Medicine]
    }

    public boolean isLoading() {
        return isLoading();
    }

    public List<String> getFakeAnswers() {
        return fakeAnswers;
    }

    public void reloadAnswer(String topic) {
        fakeAnswers.clear();
        generateFakeAnswers(topic);
    }
}
