package com.flash_card.view.flashcardSet;

    import com.flash_card.framework.ViewController;
    import com.flash_card.model.dao.FlashcardDao;
    import com.flash_card.model.entity.Flashcard;
    import com.flash_card.view.flashcard.FlashcardView;
    import com.flash_card.view_model.entity.EntityManagerViewModel;
    import jakarta.persistence.EntityManager;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.scene.control.Label;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.StackPane;
    import javafx.scene.layout.VBox;
    import javafx.scene.control.Button;

    import java.util.List;

    public class StudyFlashcardSetController extends ViewController {
        @FXML
        public Label index;
        @FXML
        public Label total;
        @FXML
        public Label setName;
        @FXML
        public StackPane backIcon;
        @FXML
        public StackPane nextIcon;
        @FXML
        private VBox flashcardContainer;

        private List<Flashcard> flashcards;
        private int currentIndex = 0;
        private EntityManager entityManager = EntityManagerViewModel.getEntityManager();;
        private FlashcardDao flashcardDao = FlashcardDao.getInstance(entityManager);

        @FXML
        public void initialize() {
        }

        public void setFlashcardSet(int setId, String setName) {
            flashcards = flashcardDao.findBySetId(setId);
            this.setName.setText(setName);
            this.total.setText(String.valueOf(flashcards.size()));
            showFlashcard(currentIndex);
        }

        private void showFlashcard(int index) {
            flashcardContainer.getChildren().clear();
            Flashcard flashcard = flashcards.get(index);
            FlashcardView flashcardView = new FlashcardView(flashcard.getTerm(), flashcard.getDefinition());
            flashcardContainer.getChildren().add(flashcardView);
            this.index.setText(String.valueOf(index + 1));
            updateButtonStates();
        }


        private void updateButtonStates() {
            backIcon.setVisible(currentIndex != 0);
            nextIcon.setVisible(currentIndex != flashcards.size() - 1);
        }

        @FXML
        public void handelEasy(ActionEvent actionEvent) {
        }

        @FXML
        public void handleHard(ActionEvent actionEvent) {
        }

        @FXML
        public void handleReset(MouseEvent mouseEvent) {
        }

        @FXML
        public void handleShuffle(MouseEvent mouseEvent) {
        }

        @FXML
        public void handleNext(MouseEvent mouseEvent) {
            if (currentIndex < flashcards.size() - 1) {
                currentIndex++;
                showFlashcard(currentIndex);
            }
        }
        @FXML
        public void handleBack(MouseEvent mouseEvent) {
            if (currentIndex > 0) {
                currentIndex--;
                showFlashcard(currentIndex);
            }
        }
    }