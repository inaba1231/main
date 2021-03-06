package seedu.taskman.model.event;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.taskman.commons.util.CollectionUtil;
import seedu.taskman.commons.exceptions.DuplicateDataException;

import java.util.*;

/**
 * A list of activities that enforces uniqueness between its elements and does not allow nulls.
 * Also ignores order of elements when checking for equality
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see Activity#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueActivityList implements Iterable<Activity> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateActivityException extends DuplicateDataException {
        protected DuplicateActivityException() {
            super("Operation would result in duplicate activities");
        }
    }

    /**
     * Signals that an operation targeting a specified task in the list would fail because
     * there is no such matching task in the list.
     */
    public static class ActivityNotFoundException extends Exception {
    }

    private final ObservableList<Activity> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty TaskList.
     */
    public UniqueActivityList() {
    }

    /**
     * Returns true if the list contains an equivalent activity as the given argument.
     * Here, equivalence refers to equivalence of all fields in activity.
     */
    public boolean contains(Activity toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }
    
    /**
     * Returns true if the list contains an activity with the given title.
     */
    public boolean contains(Title toCheck) {
        assert toCheck != null;
        for(Activity a : internalList.toArray(new Activity[0])) {
            if (a.getTitle().equals(toCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateActivityException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(Activity toAdd) throws DuplicateActivityException {
        assert toAdd != null;
        if (contains(toAdd.getTitle()) || contains(toAdd)) {
            throw new DuplicateActivityException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws ActivityNotFoundException if no such task could be found in the list.
     */
    public boolean remove(Activity toRemove) throws ActivityNotFoundException {
        assert toRemove != null;
        final boolean activityFoundAndDeleted = internalList.remove(toRemove);
        if (!activityFoundAndDeleted) {
            throw new ActivityNotFoundException();
        }
        return activityFoundAndDeleted;
    }

    public ObservableList<Activity> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<Activity> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueActivityList that = (UniqueActivityList) o;

        HashSet<Activity> setRepresentation = new HashSet<>();
        setRepresentation.addAll(internalList);
        HashSet<Activity> otherSetRepresentation = new HashSet<>();
        otherSetRepresentation.addAll(that.internalList);
        return setRepresentation.equals(otherSetRepresentation);
    }

    @Override
    public int hashCode() {
        HashSet<Activity> setRepresentation = new HashSet<>();
        setRepresentation.addAll(internalList);
        return Objects.hash(setRepresentation);
    }
}
