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

    //Implement user that owns the set later. For now, all created sets are owned by user with id 1
    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User setCreator;

    //Implement many-to-many relationship with Users, connected to shared_sets table
//    @ManyToMany
//    @JoinTable(
//            name = "shared_sets",
//            joinColumns = @JoinColumn(name = "set_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    private SharedSets<User> sharedWithUsers;

    public FlashcardSet(String setName, String setDescription, String setTopic, User creatorId) {
        super();
        this.setName = setName;
        this.setDescription = setDescription;
        this.setTopic = setTopic;
        this.setCreator = creatorId;
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

    public User getSetCreator() {
        return setCreator;
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
