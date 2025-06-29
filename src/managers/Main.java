package managers;

import tasks.Epic;
import tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        /*taskManager.putNewTask(new Task("Эпик 1", "Описание эпика 1",
                Duration.ofHours(2),
                LocalDateTime.of(2023, 10, 1, 9, 0)
                ));
        taskManager.putNewEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.putNewSubTask(new SubTask(
                "Подзадача 2",
                "Описание подзадачи 2",
                Duration.ofHours(1),
                LocalDateTime.of(2023, 10, 1, 12, 0),
                0
        ));
        taskManager.putNewSubTask(new SubTask(
                "Подзадача 3",
                "Описание подзадачи 3",
                Duration.ofHours(1),
                LocalDateTime.of(2023, 10, 1, 14, 0),
                0
        ));
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(taskManager.getAll());
        System.out.println("");
        System.out.println(taskManager.getEpic(0).getStartTime());
        System.out.println(taskManager.getEpic(0).getDuration());
        System.out.println(taskManager.getEpic(0).getEndTime());*/

        //System.out.println(taskManager.getAll());
        Epic epic = new Epic("Эпик 0", "Описание 0");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 13, 23, 5), 0);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", Duration.ofHours(1),
                LocalDateTime.of(1999, 3, 12, 23, 5), 0);

        taskManager.putNewEpic(epic);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubTask(taskManager.getSubTask(1));
        taskManager.updateSubTask(taskManager.getSubTask(2));

        System.out.println(taskManager.getEpic(0).getStatus());
    }
}