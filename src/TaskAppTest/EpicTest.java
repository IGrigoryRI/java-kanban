package TaskAppTest;

import TaskApp.Epic;
import TaskApp.InMemoryTaskManager;
import TaskApp.SubTask;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldReturnCorrectSubTasksId() {

        ArrayList<Integer> expectedId = new ArrayList<>();
        expectedId.add(1);
        expectedId.add(2);
        expectedId.add(3);

        taskManager.putNewEpic(new Epic("Эпик 1","Описание 1")); // 0
        taskManager.putNewSubTask(new SubTask("Сабтаск 1 Эпика 1", "Описание 1", 0)); // 1
        taskManager.putNewSubTask(new SubTask("Сабтаск 2 Эпика 1", "Описание 1", 0)); // 2
        taskManager.putNewSubTask(new SubTask("Сабтаск 3 Эпика 1", "Описание 1", 0)); // 3

        ArrayList<Integer> actualId = taskManager.getEpic(0).getSubTasksID();

        assertEquals(expectedId, actualId, "Возвращается некорректный список id подзадач эпика");
    }

}