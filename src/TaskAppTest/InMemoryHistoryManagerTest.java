package TaskAppTest;

import TaskApp.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeAll
    public static void beforeAll() {
        taskManager.putNewTask(new Task("Задача 1", "Описание")); // 0
        taskManager.putNewTask(new Task("Задача 2", "Описание")); // 1
        taskManager.putNewEpic(new Epic("Эпик 1","Описание")); // 2
        taskManager.putNewSubTask(new SubTask("Подзадача 1 Эпика 1", "Описание", 2)); // 3
        taskManager.putNewSubTask(new SubTask("Подзадача 2 Эпика 1", "Описание", 2)); // 4
        taskManager.putNewEpic(new Epic("Эпик 2","Описание")); // 5
        taskManager.putNewSubTask(new SubTask("Подзадача 3 Эпика 2", "Описание", 5)); // 6
        taskManager.putNewTask(new Task("Задача 3", "Описание")); // 7
        taskManager.putNewTask(new Task("Задача 4", "Описание")); // 8
        taskManager.putNewEpic(new Epic("Эпик 3","Описание")); // 9
        taskManager.putNewTask(new Task("Задача 5", "Описание")); // 10
        taskManager.putNewTask(new Task("Задача 6", "Описание")); // 11
        taskManager.putNewEpic(new Epic("Эпик 4","Описание")); // 12
        taskManager.putNewSubTask(new SubTask("Подзадача 4 Эпика 1", "Описание", 2)); // 13

        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubTask(3);
        taskManager.getSubTask(4); // 1
        taskManager.getEpic(5); // 2
        taskManager.getSubTask(6); // 3
        taskManager.getTask(7); // 4
        taskManager.getTask(8); // 5
        taskManager.getEpic(9); // 6
        taskManager.getTask(10); // 7
        taskManager.getTask(11); // 8
        taskManager.getEpic(12); // 9
        taskManager.getSubTask(13); // 10

    }

    @Test
    public void historySizeShouldNotBeZeroAndMoreThenTen() {
        final int actualHistorySize = taskManager.historyManager.getHistory().size();
        assertTrue(actualHistorySize > 0, "Запись истории не произошла");
        assertTrue(actualHistorySize <= 10, "Лист записи истории превышает допустимый размер в 10 значений");
    }

    @Test
    public void firstTaskInHistoryListShouldBeAddedLatest() {
        Integer expectedTaskId = 4;
        Integer actualTaskId = taskManager.historyManager.getHistory().get(0).getID();
        assertEquals(expectedTaskId, actualTaskId, "Нарушен порядок записи в историю просмотров");
    }

    @Test
    public void lastTaskInHistoryListShouldBeAddedEarlier() {
        Integer expectedTaskId = 13;
        Integer actualTaskId = taskManager.historyManager.getHistory().get(9).getID();
        assertEquals(expectedTaskId, actualTaskId, "Нарушен порядок записи в историю просмотров");
    }

    @Test
    public void historyShouldNotAddNull() {
        taskManager.getTask(100);

        Integer expectedTaskId = 13;
        Integer actualTaskId = taskManager.historyManager.getHistory().get(9).getID();
        assertEquals(expectedTaskId, actualTaskId, "Лист записи истории не должен добавлять null");
    }

    @Test
    public void shouldGetCorrectHistory() {

        ArrayList<Integer> expectedHistoryTaskId = new ArrayList<>();
        for (int i = 4; i < 14; i++) {
            expectedHistoryTaskId.add(i);
        }

        ArrayList<Integer> actualHistoryTaskId = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TaskInterface task = taskManager.historyManager.getHistory().get(i);
            actualHistoryTaskId.add(task.getID());
        }

        assertEquals(expectedHistoryTaskId, actualHistoryTaskId, "Записанная история не соответствует действительной");
    }
}