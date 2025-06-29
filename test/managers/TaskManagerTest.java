package managers;

import tasks.Task;
import tasks.Epic;
import tasks.SubTask;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @Test
    public void taskManagerShouldPutAndGetTask(){
        Task task = new Task("Эпик 1", "Описание эпика 1",
                Duration.ofHours(2),
                LocalDateTime.of(2023, 10, 1, 9, 0)
        );

        taskManager.putNewTask(task);
        assertEquals(taskManager.getTask(0), task);
    }

    @Test
    public void taskManagerShouldDeleteTask(){
        Task task = new Task("Эпик 1", "Описание эпика 1",
                Duration.ofHours(2),
                LocalDateTime.of(2023, 10, 1, 9, 0)
        );

        taskManager.putNewTask(task);
        taskManager.deleteTask(0);

        assertNull(taskManager.getTask(0));
    }

    @Test
    public void taskManagerShouldDeleteEpic() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание")); // 0
        taskManager.putNewSubTask(new SubTask("Подзадача", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0));

        taskManager.deleteEpic(0);

        int epicsSize = taskManager.getAllEpicsList().size();
        int subTasksSize = taskManager.getAllSubTasksList().size();

        assertFalse(epicsSize >= 1, "Эпик не был удален");
        assertFalse(subTasksSize >= 1, "После удаления эпика не были удалены его подзадачи");
    }

    @Test
    public void taskManagerShouldDeleteSubTask() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание")); // 0
        taskManager.putNewSubTask(new SubTask("Подзадача", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0)); // 1
        taskManager.getSubTask(1).setStatus(Status.DONE);

        taskManager.deleteSubTask(1);

        int subTasksSize = taskManager.getAllSubTasksList().size();

        Status actualEpicStatus = taskManager.getEpic(0).getStatus();

        assertFalse(subTasksSize >= 1, "Подзадача не была удалена");
        assertTrue(actualEpicStatus != Status.DONE, "После удаления подзадачи статус эпика не изменился");
    }

    @Test
    public void shouldAddNewEpic() {
        Epic epic = new Epic("Эпик", "Описание");

        taskManager.putNewEpic(epic);
        final Epic savedEpic = taskManager.getEpic(0);

        assertNotNull(savedEpic, "Эпик не найден в списке эпиков");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    public void shouldAddNewSubtask() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание"));
        SubTask subTask = new SubTask("Подзадача", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0);
        subTask.setStatus(Status.DONE);

        taskManager.putNewSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTask(1);

        final List<SubTask> actualSubTaskIdInEpic = taskManager.getEpic(0).getSubTasks();
        final List<SubTask> expectedSubTaskIdInEpic = new ArrayList<>();
        expectedSubTaskIdInEpic.add(taskManager.getSubTask(1));

        final int epicId = taskManager.getEpic(0).getId();
        final int epicIdInSubtask = subTask.getEpicId();

        Status expectedEpicStatus = Status.DONE;
        Status actualEpicStatus = taskManager.getEpic(0).getStatus();

        assertNotNull(savedSubTask, "Подзадача не найдена в списке подзадач");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");
        assertNotNull(actualSubTaskIdInEpic.get(0), "Id подзадачи не сохранён в эпике");
        assertEquals(actualSubTaskIdInEpic, expectedSubTaskIdInEpic, "Id подзадачи сохранён в эпике некорректно");
        assertFalse(epicIdInSubtask != 0, "Id эпика не сохраняется в его подзадаче");
        assertEquals(epicId, epicIdInSubtask, "Id эпика некорректно сохраняется в его подзадаче");
        assertEquals(expectedEpicStatus, actualEpicStatus, "После добавления подзадачи статус эпика не изменился");
    }

    @Test
    public void testEpicStatusNew() {
        Epic epic = new Epic("Эпик 0", "Описание 0");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 13, 23, 5), 0);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0);

        taskManager.putNewEpic(epic);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);

        assertSame(Status.NEW, taskManager.getEpic(0).getStatus());
    }

    @Test
    public void testEpicStatusDone() {
        Epic epic = new Epic("Эпик 0", "Описание 0");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 13, 23, 5), 0);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0);

        taskManager.putNewEpic(epic);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);

        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);

        taskManager.updateSubTask(taskManager.getSubTask(1));
        taskManager.updateSubTask(taskManager.getSubTask(2));

        assertSame(Status.DONE, taskManager.getEpic(0).getStatus());
    }

    @Test
    public void testEpicStatusInProgressIfOneSubTaskDone() {
        Epic epic = new Epic("Эпик 0", "Описание 0");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 13, 23, 5), 0);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0);

        taskManager.putNewEpic(epic);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);

        subTask2.setStatus(Status.DONE);

        taskManager.updateSubTask(taskManager.getSubTask(2));

        assertSame(Status.IN_PROGRESS, taskManager.getEpic(0).getStatus());
    }

    @Test
    public void testEpicStatusInProgressIfAllSubTasksInProgress() {
        Epic epic = new Epic("Эпик 0", "Описание 0");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 13, 23, 5), 0);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0);

        taskManager.putNewEpic(epic);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubTask(taskManager.getSubTask(1));
        taskManager.updateSubTask(taskManager.getSubTask(2));

        assertSame(Status.IN_PROGRESS, taskManager.getEpic(0).getStatus());
    }

    @Test
    public void testSubTaskHasLinkedEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        taskManager.putNewEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0);
        taskManager.putNewSubTask(subTask);

        assertEquals(epic.getId(), subTask.getEpicId(), "Подзадача должна быть связана с эпиком");

        assertTrue(taskManager.getEpic(0).getSubTasks().contains(subTask),
                "Эпик должен содержать связанную подзадачу");
    }

    @Test
    public void subTaskIdShouldDeleteFromEpic() {
        taskManager.putNewEpic(new Epic("Эпик 1","Описание 1")); // 0
        taskManager.putNewSubTask(new SubTask("Сабтаск 1 Эпика 1", "Описание 1",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5), 0)); // 1
        taskManager.putNewSubTask(new SubTask("Сабтаск 2 Эпика 1", "Описание 1",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 21, 5), 0)); // 2
        taskManager.putNewSubTask(new SubTask("Сабтаск 3 Эпика 1", "Описание 1",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 12, 5), 0)); // 3

        List<SubTask> expectedSubTasks = new ArrayList<>();
        expectedSubTasks.add(taskManager.getSubTask(1));
        expectedSubTasks.add(taskManager.getSubTask(2));

        taskManager.deleteSubTask(3);

        List<SubTask> actualSubTask = taskManager.getEpic(0).getSubTasks();

        assertEquals(expectedSubTasks, actualSubTask, "Удаление subTask не приводит к его удалению из epic");
    }

    @Test
    public void testIfTasksHaveIntersection() {
        Task task1 = new Task("Task 1", "Description",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5));
        Task task2 = new Task("Task 2", "Description",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 2));

        taskManager.putNewTask(task1);

        assertThrows(IllegalArgumentException.class, () -> taskManager.putNewTask(task2));
    }

    @Test
    public void testIfTasksHaveNotIntersection() {
        Task task1 = new Task("Task 1", "Description",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 23, 5));
        Task task2 = new Task("Task 2", "Description",
                Duration.ofHours(1), LocalDateTime.of(1999, 3, 12, 22, 2));

        taskManager.putNewTask(task1);
        taskManager.putNewTask(task2);

        assertEquals(2, taskManager.getAllTaskList().size());
    }
}
