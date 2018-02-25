package model;

/**
 * Created by Marisha on 25/02/2018.
 */
public enum ContactType {
    PHONENUMBER("Teл."),
    SKYPE("Skype"),
    EMAIL("Почта"),
    LINKEDIN("Профиль Linkedin"),
    GITHUB("Профиль Github"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
