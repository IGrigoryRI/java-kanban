package TaskAppTest;

import ManagersPackage.HistoryManager;
import ManagersPackage.Managers;
import ManagersPackage.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void managerShouldReturnInMemoryTaskManager(){
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "Менеджер не возвращает таск менеджер");
    }

    @Test
    public void managerShouldReturnInMemoryHistoryManager(){
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер не возвращает таск менеджер");
    }

}