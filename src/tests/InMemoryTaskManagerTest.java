package tests;

import managers.*;
import tasks.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    public void shouldAddNewTask() {
        Task task = new Task("Задача", "Описание");

        taskManager.putNewTask(task);
        final Task savedTask = taskManager.getTask(0);

        assertNotNull(savedTask, "Задача не найдена в списке задач");
        assertEquals(task, savedTask, "Задачи не совпадают");
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
        SubTask subTask = new SubTask("Подзадача", "Описание", 0);
        subTask.setStatus(Status.DONE);

        taskManager.putNewSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTask(1);

        final List<Integer> actualSubTaskIdInEpic = taskManager.getEpic(0).getSubTasksID();
        final List<Integer> expectedSubTaskIdInEpic = new ArrayList<>();
        expectedSubTaskIdInEpic.add(taskManager.getSubTask(1).getID());

        final int epicId = taskManager.getEpic(0).getID();
        final int epicIdInSubtask = subTask.getEpicID();

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
    public void shouldGetCorrectAllTasksList() {
        Task task1 = new Task("Задача1", "Описание");
        Task task2 = new Task("Задача2", "Описание");
        Task task3 = new Task("Задача3", "Описание");

        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(task1);
        expectedTaskList.add(task2);
        expectedTaskList.add(task3);

        taskManager.putNewTask(task1);
        taskManager.putNewTask(task2);
        taskManager.putNewTask(task3);
        List<Task> actualTaskList = taskManager.getAllTaskList();

        assertEquals(expectedTaskList, actualTaskList, "Выводится некорректный список всех задач");

    }

    @Test
    public void shouldGetCorrectAllEpicsList() {
        Epic epic1 = new Epic("Эпик1", "Описание");
        Epic epic2 = new Epic("Эпик1", "Описание");
        Epic epic3 = new Epic("Эпик1", "Описание");

        ArrayList<Epic> expectedEpicList = new ArrayList<>();
        expectedEpicList.add(epic1);
        expectedEpicList.add(epic2);
        expectedEpicList.add(epic3);

        taskManager.putNewEpic(epic1);
        taskManager.putNewEpic(epic2);
        taskManager.putNewEpic(epic3);
        List<Epic> actualEpicList = taskManager.getAllEpicsList();

        assertEquals(expectedEpicList, actualEpicList, "Выводится некорректный список всех эпиков");

    }

    @Test
    public void shouldGetCorrectAllSubTasksList() {
        Epic epic1 = new Epic("Эпик1", "Описание");
        SubTask subTask1 = new SubTask("Подзадача1", "Описание", 0);
        SubTask subTask2 = new SubTask("Подзадача2", "Описание", 0);
        SubTask subTask3 = new SubTask("Подзадача3", "Описание", 0);

        ArrayList<SubTask> expectedSubTaskList = new ArrayList<>();
        expectedSubTaskList.add(subTask1);
        expectedSubTaskList.add(subTask2);
        expectedSubTaskList.add(subTask3);

        taskManager.putNewEpic(epic1);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);
        taskManager.putNewSubTask(subTask3);
        List<SubTask> actualSubTaskList = taskManager.getAllSubTasksList();

        assertEquals(expectedSubTaskList, actualSubTaskList, "Выводится некорректный список всех подзадач");

    }

    @Test
    public void shouldGetCorrectEpicAllSubTasksList() {
        Epic epic1 = new Epic("Эпик1", "Описание");
        SubTask subTask1 = new SubTask("Подзадача1 Эпика1", "Описание", 0);
        Epic epic2 = new Epic("Эпик2", "Описание");
        SubTask subTask2 = new SubTask("Подзадача2 Эпика2", "Описание", 1);
        SubTask subTask3 = new SubTask("Подзадача3 Эпика3", "Описание", 1);

        ArrayList<SubTask> expectedSubTaskList = new ArrayList<>();
        expectedSubTaskList.add(subTask2);
        expectedSubTaskList.add(subTask3);

        taskManager.putNewEpic(epic1);
        taskManager.putNewEpic(epic2);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);
        taskManager.putNewSubTask(subTask3);
        List<SubTask> actualSubTaskList = taskManager.getEpicAllSubTasks(1);

        assertEquals(expectedSubTaskList, actualSubTaskList, "Выводится некорректный список всех подзадач эпика");

    }

    @Test
    public void shouldDeleteTask() {
        taskManager.putNewTask(new Task("Задача", "Описание")); // 0
        taskManager.deleteTask(0);

        int tasksSize = taskManager.getAllTaskList().size();

        assertFalse(tasksSize >= 1, "Задача не была удалена");
    }

    @Test
    public void shouldDeleteEpic() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание")); // 0
        taskManager.putNewSubTask(new SubTask("Подзадача", "Описание", 0));

        taskManager.deleteEpic(0);

        int epicsSize = taskManager.getAllEpicsList().size();
        int subTasksSize = taskManager.getAllSubTasksList().size();

        assertFalse(epicsSize >= 1, "Эпик не был удален");
        assertFalse(subTasksSize >= 1, "После удаления эпика не были удалены его подзадачи");
    }

    @Test
    public void shouldDeleteSubTask() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание")); // 0
        taskManager.putNewSubTask(new SubTask("Подзадача", "Описание", 0)); // 1
        taskManager.getSubTask(1).setStatus(Status.DONE);

        taskManager.deleteSubTask(1);

        int subTasksSize = taskManager.getAllSubTasksList().size();

        Status actualEpicStatus = taskManager.getEpic(0).getStatus();

        assertFalse(subTasksSize >= 1, "Подзадача не была удалена");
        assertTrue(actualEpicStatus != Status.DONE, "После удаления подзадачи статус эпика не изменился");
    }

    @Test
    public void shouldClearTasks() {
        taskManager.putNewTask(new Task("Задача", "Описание")); // 0
        taskManager.clearTasks();

        int tasksSize = taskManager.getAllTaskList().size();

        assertFalse(tasksSize >= 1, "Список задач не был очищен");
    }

    @Test
    public void shouldCleanEpics() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание")); // 0
        taskManager.putNewSubTask(new SubTask("Подзадача", "Описание", 0));

        taskManager.clearEpics();

        int epicsSize = taskManager.getAllEpicsList().size();
        int subTasksSize = taskManager.getAllSubTasksList().size();

        assertFalse(epicsSize >= 1, "Список эпиков не был очищен");
        assertFalse(subTasksSize >= 1, "После очищения списка эпиков, список подзадач не был очищен");
    }

    @Test
    public void shouldCleanSubTasks() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание")); // 0
        taskManager.putNewSubTask(new SubTask("Подзадача1", "Описание", 0)); // 1
        taskManager.putNewSubTask(new SubTask("Подзадача2", "Описание", 0)); // 2
        taskManager.putNewSubTask(new SubTask("Подзадача3", "Описание", 0)); // 3

        taskManager.getSubTask(3).setStatus(Status.DONE);
        taskManager.clearSubTasks();

        int subTasksSize = taskManager.getAllSubTasksList().size();
        Status actualEpicStatus = taskManager.getEpic(0).getStatus();

        assertFalse(subTasksSize >= 1, "Список подзадач не был очищен");
        assertTrue(actualEpicStatus != Status.IN_PROGRESS, "После очищения списка подзадач статус эпика не изменился");
    }

    @Test
    public void shouldCreateIdInTask() {
        taskManager.putNewTask(new Task("Задача1", "Описание")); // 0
        taskManager.putNewTask(new Task("Задача2", "Описание")); // 1

        int actualIdInFirstTask = taskManager.getTask(0).getID();
        int actualIdInSecondTask = taskManager.getTask(1).getID();

        assertTrue(actualIdInFirstTask == 0, "Id для первой задачи не был записан");
        assertTrue(actualIdInSecondTask == 1, "Id для второй задачи не был записан");
    }

    @Test
    public void shouldSetEpicStatus() {
        taskManager.putNewEpic(new Epic("Эпик", "Описание")); // 0
        Status actualStatusNewAfterCreateEpic = taskManager.getEpic(0).getStatus();
        taskManager.putNewSubTask(new SubTask("Подзадача1", "Описание", 0)); // 1
        taskManager.putNewSubTask(new SubTask("Подзадача2", "Описание", 0)); // 2

        Status actualStatusNewAfterAddSubTasks = taskManager.getEpic(0).getStatus();
        taskManager.getSubTask(1).setStatus(Status.DONE);
        taskManager.updateSubTask(taskManager.getSubTask(1));
        Status actualStatusInProgress = taskManager.getEpic(0).getStatus();
        taskManager.getSubTask(2).setStatus(Status.DONE);
        taskManager.updateSubTask(taskManager.getSubTask(2));
        Status actualStatusDone = taskManager.getEpic(0).getStatus();

        assertEquals(Status.NEW, actualStatusNewAfterCreateEpic, "При создании эпика его статус не NEW");
        assertEquals(Status.NEW, actualStatusNewAfterAddSubTasks,
                "При статусах NEW всех подзадач эпика, эпик имеет иной статус");
        assertEquals(Status.IN_PROGRESS, actualStatusInProgress,
                "При статусе одной подзадачи эпика NEW, а другой DONE, статус эпика не IN_PROGRESS");
        assertEquals(Status.DONE, actualStatusDone, "При статусах DONE всех подзадач эпика, эпик имеет иной статус");

    }
}