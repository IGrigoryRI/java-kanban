package TasksPackage;

import java.util.List;

public interface EpicInterface extends TaskInterface {
    List<Integer> getSubTasksID();

    void deleteSubTaskID(int subTaskID);

    @Override
    Epic copy();

    @Override
    String toString();
}
