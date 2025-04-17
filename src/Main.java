public class Main {

    static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        System.out.println("");
        taskManager.putNewTask(new Task("Задача 1", "Описание 1")); // 0
        taskManager.putNewTask(new Task("Задача 2", "Описание 2")); // 1
        taskManager.putNewEpic(new Epic("Эпик 1","Описание 1")); // 2
        taskManager.putNewSubTask(new SubTask("Подзадача 1 Эпика 1", "Описание 1", 2)); // 3
        taskManager.putNewSubTask(new SubTask("Подзадача 2 Эпика 1", "Описание 2", 2)); // 4
        taskManager.putNewEpic(new Epic("Эпик 2","Описание 2")); // 5
        taskManager.putNewSubTask(new SubTask("Подзадача 1 Эпика 2", "Описание 1", 5)); // 6
        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicsList());
        System.out.println(taskManager.getAllSubTasksList());

        System.out.println("");
        taskManager.updateTaskStatus(taskManager.getTaskByID(0));
        taskManager.updateSubTaskStatus(taskManager.getSubTaskByID(3));
        taskManager.updateSubTaskStatus(taskManager.getSubTaskByID(6));
        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicsList());
        System.out.println(taskManager.getAllSubTasksList());

        System.out.println("");
        taskManager.deleteTask(taskManager.getTaskByID(0));
        taskManager.deleteEpic(taskManager.getEpicByID(5));
        taskManager.deleteSubTask(taskManager.getSubTaskByID(3));
        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicsList());
        System.out.println(taskManager.getAllSubTasksList());

        System.out.println("");
        taskManager.clearEpics();
        taskManager.clearTasks();
        System.out.println(taskManager.getAllTaskList());
        System.out.println(taskManager.getAllEpicsList());
        System.out.println(taskManager.getAllSubTasksList());
    }
}
