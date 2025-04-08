package com.flash_card.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

import java.util.List;
/**
 * Entity class representing a flashcard set in the application.
 * Maps to the "flashcard_sets" table in the database.
 */
@Entity
@Table(name = "flashcard_sets")

public class FlashcardSet {
    /**
     * Unique identifier for the flashcard set.
     * Maps to the "set_id" column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    private int setId;
    /**
     * Name of the flashcard set.
     * Maps to the "set_name" column in the database.
     */
    @Column(name = "set_name")
    private String setName;
    /**
     * Language of the flashcard set.
     * Maps to the "set_language" column in the database.
     */
    @Column(name = "set_language")
    private String setLanguage;
    /**
     * Description of the flashcard set.
     * Maps to the "set_description" column in the database.
     */
    @Column(name = "set_description")
    private String setDescription;
    /**
     * Topic of the flashcard set.
     * Maps to the "set_topic" column in the database.
     */
    @Column(name = "set_topic")
    private String setTopic;
    /**
     * Number of flashcards in the set.
     * Maps to the "number_flashcards" column in the database.
     */
    @Column(name = "number_flashcards")
    private int numberFlashcards;
    /**
     * The creator of the flashcard set.
     * Maps to the "creator_id" column in the database.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User flashcardCreator;
    /**
     * List of shared sets associated with this flashcard set.
     * Mapped by the "flashcardSet" field in the SharedSet entity.
     */
    @OneToMany(mappedBy = "flashcardSet")
    private List<SharedSet> sharedSets;
    /**
     * Constructs a FlashcardSet with the specified name, description, topic, and creator.
     *
     * @param setNameParam          the name of the flashcard set
     * @param setDescriptionParam   the description of the flashcard set
     * @param setTopicParam         the topic of the flashcard set
     * @param flashcardCreatorParam the creator of the flashcard set
     */
    public FlashcardSet(String setNameParam, String setDescriptionParam, String setTopicParam, final User flashcardCreatorParam) {
        super();
        this.setName = setNameParam;
        this.setDescription = setDescriptionParam;
        this.setTopic = setTopicParam;
        this.numberFlashcards = 0;
        this.flashcardCreator = flashcardCreatorParam;
    }
    /**
     * Default constructor for the FlashcardSet entity.
     */
    public FlashcardSet() {
    }
    /**
     * Returns the unique identifier of the flashcard set.
     *
     * @return the ID of the flashcard set
     */
    public int getSetId() {
        return setId;
    }
    /**
     * Returns the name of the flashcard set.
     *
     * @return the name of the flashcard set
     */
    public String getSetName() {
        return setName;
    }
    /**
     * Sets the name of the flashcard set.
     *
     * @param setNameParam the name to set
     */
    public void setSetName(String setNameParam) {
        this.setName = setNameParam;
    }
    /**
     * Returns the language of the flashcard set.
     *
     * @return the language of the flashcard set
     */
    public String getSetLanguage() {
        return setLanguage;
    }
    /**
     * Sets the language of the flashcard set.
     *
     * @param setLanguageParam the language to set
     */
    public void setSetLanguage(String setLanguageParam) {
        this.setLanguage = setLanguageParam;
    }
    /**
     * Returns the description of the flashcard set.
     *
     * @return the description of the flashcard set
     */
    public String getSetDescription() {
        return setDescription;
    }
    /**
     * Sets the description of the flashcard set.
     *
     * @param setDescriptionParam the description to set
     */
    public void setSetDescription(String setDescriptionParam) {
        this.setDescription = setDescriptionParam;
    }
    /**
     * Returns the topic of the flashcard set.
     *
     * @return the topic of the flashcard set
     */
    public String getSetTopic() {
        return setTopic;
    }
    /**
     * Sets the topic of the flashcard set.
     *
     * @param setTopicParam the topic to set
     */
    public void setSetTopic(String setTopicParam) {
        this.setTopic = setTopicParam;
    }
    /**
     * Returns the number of flashcards in the set.
     *
     * @return the number of flashcards
     */
    public int getNumberFlashcards() {
        return numberFlashcards;
    }
    /**
     * Returns the creator of the flashcard set.
     *
     * @return the creator of the flashcard set
     */
    public User getSetCreator() {
        return flashcardCreator;
    }
    /**
     * Increments the number of flashcards in the set by 1.
     */
    public void addNumberFlashcards() {
        numberFlashcards += 1;
    }
    /**
     * Decrements the number of flashcards in the set by 1.
     */
    public void subtractNumberFlashcard() {
        numberFlashcards -= 1;
    }

}
