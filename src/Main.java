public class Main {

    static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        System.out.println("");
        taskManager.generateNewTask("Задача 1", "Описание задачи 1");
        taskManager.generateNewTask("Задача 2", "Описание задачи 2");
        taskManager.generateNewEpic("Эпик 1", "Описание эпика 1");
        taskManager.generateNewSubTask("Подзадача 1 Эпика 1", "Описание подзадачи 1", 2);
        taskManager.generateNewSubTask("Подзадача 2 Эпика 1", "Описание подзадачи 2", 2);
        taskManager.generateNewEpic("Эпик 2", "Описание эпика 2");
        taskManager.generateNewSubTask("Подзадача 3 Эпика 2", "Описание подзадачи 3", 5);
        taskManager.printAllTasksList();
        taskManager.printAllEpicsList();
        taskManager.printAllSubTasksList();
        System.out.println("");
        taskManager.updateTaskStatus(0, Status.IN_PROGRESS);
        taskManager.updateTaskStatus(1, Status.DONE);
        taskManager.updateSubTaskStatus(3, Status.DONE);
        taskManager.updateSubTaskStatus(6, Status.IN_PROGRESS);
        taskManager.printAllTasksList();
        taskManager.printAllEpicsList();
        taskManager.printAllSubTasksList();
        System.out.println(taskManager.getEpicByID(2).getSubTasksID());
        System.out.println("");
        taskManager.deleteTaskByID(0);
        taskManager.deleteSubTaskByID(3);
        taskManager.deleteEpicByID(5);
        taskManager.printAllTasksList();
        taskManager.printAllEpicsList();
        taskManager.printAllSubTasksList();
        System.out.println(taskManager.getEpicByID(2).getSubTasksID());
    }
}
