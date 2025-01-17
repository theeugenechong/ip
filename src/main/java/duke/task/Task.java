package duke.task;

/**
 * Parent class of {@code Todo}, {@code Event} and {@code Deadline}.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getStatusIcon() {
        if (this.isDone) {
            return "X";
        } else {
            return " ";
        }
    }

    public abstract String getTaskType();

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
