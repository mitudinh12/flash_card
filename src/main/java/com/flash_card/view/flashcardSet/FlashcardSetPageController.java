//package com.flash_card.view.flashcardSet;
//
//import com.flash_card.framework.ViewController;
//import com.flash_card.model.dao.FlashcardSetDao; //Dao need to be in ViewModel instead
//import com.flash_card.model.entity.FlashcardSet;
//import com.flash_card.model.entity.SharedSet;
//import com.flash_card.view_model.flashcard_set.DisplayFlashcardSetViewModel;
//import com.flash_card.view_model.flashcard_set.SharedSetViewModel;
//import com.flash_card.view_model.user_auth.AuthSessionViewModel;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Insets;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Label;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.List;
//
//public class FlashcardSetPageController extends ViewController {
//    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();
////    private final DisplayFlashcardSetViewModel displayViewModel = new DisplayFlashcardSetViewModel();
////    private SharedSetViewModel sharedSetViewModel = new SharedSetViewModel();
//
//    @FXML
//    private Label userName;
//
//    @FXML
//    private VBox flashcardSetsContainer;
//
//    @FXML
//    private void initialize() {
//        setUserName(authSessionViewModel.getVerifiedUserInfo().get("firstName"));
//        loadFlashcardSets();
//    }
//    //Load all flashcard sets that this person can see
//    private void loadFlashcardSets() {
//        flashcardSetsContainer.getChildren().clear();
////        loadOwnFlashcardSets();
//        loadSharedFlashcardSets();
//    }
////    //Load all flashcard sets that this person created
////    private void loadOwnFlashcardSets() {
////        List<FlashcardSet> ownSets = displayViewModel.findOwnSets(authSessionViewModel.getVerifiedUserInfo().get("userId"));
////        for (FlashcardSet set : ownSets) {
////            HBox hbox = createFlashcardSetHBox(set, true);
////            flashcardSetsContainer.getChildren().add(hbox);
////        }
////    }
//    //Load all flashcard sets that this person is shared to
//    private void loadSharedFlashcardSets() {
//        List<FlashcardSet> sharedSets = sharedSetViewModel.getSharedFlashcardSets();
//        for (FlashcardSet set : sharedSets) {
//            HBox hbox = createFlashcardSetHBox(set, false);
//            flashcardSetsContainer.getChildren().add(hbox);
//        }
//    }
//
//    private HBox createFlashcardSetHBox(FlashcardSet set, boolean isOwnSet) {
//        HBox hbox = new HBox(10);
//        hbox.getStyleClass().add("hbox-flashcard-set");
//        Label setName = new Label(set.getSetName());
//        Button learnButton = new Button("Learn");
//        Button quizButton = new Button("Quiz");
//
//        learnButton.setOnAction(event -> handleLearn(set));
//        quizButton.setOnAction(event -> handleQuiz(set));
//
//        hbox.getChildren().addAll(setName, learnButton, quizButton);
//
//        if (isOwnSet) {
//            Button editButton = new Button("Edit");
//            Button shareButton = new Button("Share");
//            editButton.setOnAction(event -> handleEdit(set.getSetId(), set.getSetName(), set.getSetDescription(), set.getSetTopic()));
//            shareButton.setOnAction(event -> handleShare(set.getSetId()));
//            hbox.getChildren().addAll(editButton, shareButton);
//        } else {
//            Button deleteButton = new Button("Delete");
//            deleteButton.setOnAction(event -> handleDelete(set.getSetId()));
//            hbox.getChildren().add(deleteButton);
//        }
//
//        return hbox;
//    }
//
//    private void handleLearn(FlashcardSet set) {
//    }
//
//    private void handleQuiz(FlashcardSet set)  {
//    }
//
//    private void handleEdit(int setId, String setName, String setDescription, String setTopic) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/flash_card/fxml/edit-set.fxml"));
//            Parent root = loader.load();
//
//            //pass the FlashcardSet data to the EditFlashcardSetController
//            EditFlashcardSetController controller = loader.getController();
//            controller.setFlashcardSet(setId, setName, setDescription, setTopic);
//
//            Scene scene = flashcardSetsContainer.getScene();
//            scene.setRoot(root);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //Delete shared flashcard set
//    private void handleDelete(int flashcardSetId) {
//        sharedSetViewModel.deleteSharedFlashcardSet(flashcardSetId);
//        showAlert("Success", "The flashcard set has been deleted successfully!");
//    }
//
//    public void setUserName(String name) {
//        userName.setText("Hi, " + name);
//    }
//}
