package TaskApp;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        taskManager.putNewTask(new Task("Задача", "Описание"));
        System.out.println(taskManager.getTask(0));
        taskManager.getTask(0).setDescription("Описание 2");
        taskManager.getTask(0).setName("Задача 1");
        taskManager.getTask(0).setStatus(Status.DONE);
        System.out.println(taskManager.getTask(0));
        taskManager.putNewEpic(new Epic("Эпик", "Описание"));
        taskManager.getEpic(1).setStatus(Status.DONE);
        taskManager.getEpic(1);
        System.out.println("История: " + taskManager.getHistory());
    }
}