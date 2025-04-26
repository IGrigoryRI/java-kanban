package TaskApp;

public interface TaskInterface {
    String getDescription();

    void setDescription(String taskDescription);

    Status getStatus();

    void setStatus(Status newStatus);

    String getName();

    void setName(String name);

    Integer getID();

    void setID(Integer ID);

    @Override
    String toString();
}
