package ru.yandex.practicum.codingpractice;

public class SubTask extends Task {
    private Integer epicID;

    public SubTask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }
}
