package TasksPackage;

public interface SubTaskInterface extends TaskInterface {
    Integer getEpicID();

    @Override
    String toString();

    @Override
    SubTask copy();
}
