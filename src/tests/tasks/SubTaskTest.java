package tests.tasks;

import tasks.Epic;
import managers.InMemoryTaskManager;
import tasks.SubTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    public void shouldReturnCorrectEpicId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Integer expectedEpicId = 0;

        taskManager.putNewEpic(new Epic("Эпик 1","Описание 1"));
        taskManager.putNewSubTask(new SubTask("Сабтаск 1 Эпика 1", "Описание 1", 0));

        Integer actualEpicID = taskManager.getSubTask(1).getEpicId();

        assertEquals(expectedEpicId, actualEpicID, "Возвращается некорректное значение id эпика подзадачи");
    }
}