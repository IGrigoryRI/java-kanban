package tests.tasks;

import tasks.Epic;
import managers.InMemoryTaskManager;
import tasks.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    public void beforeEach() {
        taskManager.putNewEpic(new Epic("Эпик 1","Описание 1")); // 0
        taskManager.putNewSubTask(new SubTask("Сабтаск 1 Эпика 1", "Описание 1", 0)); // 1
        taskManager.putNewSubTask(new SubTask("Сабтаск 2 Эпика 1", "Описание 1", 0)); // 2
        taskManager.putNewSubTask(new SubTask("Сабтаск 3 Эпика 1", "Описание 1", 0)); // 3
    }

    @Test
    public void shouldReturnCorrectSubTasksId() {
        List<Integer> expectedId = new ArrayList<>();
        expectedId.add(1);
        expectedId.add(2);
        expectedId.add(3);

        List<Integer> actualId = taskManager.getEpic(0).getSubTasksId();

        assertEquals(expectedId, actualId, "Возвращается некорректный список id подзадач эпика");
    }

    @Test
    public void subTaskIdShouldDeleteFromEpic() {
        List<Integer> expectedId = new ArrayList<>();
        expectedId.add(1);
        expectedId.add(2);

        taskManager.deleteSubTask(3);

        List<Integer> actualId = taskManager.getEpic(0).getSubTasksId();

        assertEquals(expectedId, actualId, "Удаление subTask не приводит к удалению его ID из его epic");
    }
}