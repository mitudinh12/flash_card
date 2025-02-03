package com.flash_card.model.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "flashcard_sets")

public class FlashcardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    private int setId;

    @Column(name = "set_name")
    private String setName;

    @Column(name = "set_description")
    private String setDescription;

    @Column(name = "set_topic")
    private String setTopic;

    @Column(name = "creator_id")
    private String creatorId;


    public FlashcardSet(String setName, String setDescription, String setTopic, String creatorId) {
        super();
        this.setName = setName;
        this.setDescription = setDescription;
        this.setTopic = setTopic;
        this.creatorId = creatorId;
    }

    public FlashcardSet() {}

    public int getSetId() {
        return setId;
    }

    public String getSetName() {
        return setName;
    }

    public String getSetDescription() {
        return setDescription;
    }

    public String getSetTopic() {
        return setTopic;
    }

    public String getSetCreator() {
        return creatorId;
    }


    public void setSetName(String setName) {
        this.setName = setName;
    }

    public void setSetDescription(String setDescription) {
        this.setDescription = setDescription;
    }

    public void setSetTopic(String setTopic) {
        this.setTopic = setTopic;
    }

}
