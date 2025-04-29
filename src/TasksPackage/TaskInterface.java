package TasksPackage;

import ManagersPackage.Status;

public interface TaskInterface {
    String getDescription();

    void setDescription(String taskDescription);

    Status getStatus();

    void setStatus(Status newStatus);

    String getName();

    void setName(String name);

    Integer getID();

    void setID(Integer ID);

    Task copy();

    String toString();

    boolean equals(Object o);

    int hashCode();
}
