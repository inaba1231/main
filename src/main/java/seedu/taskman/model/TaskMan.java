package seedu.taskman.model;

import javafx.collections.ObservableList;
import seedu.taskman.model.event.Activity;
import seedu.taskman.model.event.Event;
import seedu.taskman.model.event.MutableTagsEvent;
import seedu.taskman.model.event.Task;
import seedu.taskman.model.event.UniqueActivityList;
import seedu.taskman.model.tag.Tag;
import seedu.taskman.model.tag.UniqueTagList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wraps all data at the task-man level
 * Duplicates are not allowed (by .equals comparison)
 */
public class TaskMan implements ReadOnlyTaskMan {

    private final UniqueActivityList activities;

    // TODO: format looks pretty weird. can we do something about it?
    {
        activities = new UniqueActivityList();
    }

    public TaskMan() {
    }

    /**
     * Tasks and Tags are copied into this taskMan
     */
    public TaskMan(ReadOnlyTaskMan toBeCopied) {
        this(toBeCopied.getUniqueActivityList());
    }

    /**
     * Tasks and Tags are copied into this taskMan
     */
    public TaskMan(UniqueActivityList activities) {
        resetData(activities.getInternalList());
    }

    public static ReadOnlyTaskMan getEmptyTaskMan() {
        return new TaskMan();
    }

//// list overwrite operations

    public ObservableList<Activity> getActivities() {
        return activities.getInternalList();
    }

    public void setActivities(List<Activity> activities) {
        this.activities.getInternalList().setAll(activities);
    }

    public void resetData(Collection<? extends Activity> newActivities) {
        setActivities(newActivities.stream().map(Activity::new).collect(Collectors.toList()));
    }

    public void resetData(ReadOnlyTaskMan newData) {
        resetData(newData.getActivityList());
    }

//// event-level operations

    public void addActivity(Event event) throws UniqueActivityList.DuplicateActivityException {
        addActivity(new Activity(event));
    }

    /**
     * Adds an activity to TaskMan.
     * Also checks the new activity's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the activity to point to those in {@link #tags}.
     *
     * @throws UniqueActivityList.DuplicateActivityException if an equivalent activity already exists.
     */
    public void addActivity(Activity activity) throws UniqueActivityList.DuplicateActivityException {
        activities.add(activity);
    }

    public boolean removeActivity(Task key) throws UniqueActivityList.ActivityNotFoundException {
        if (activities.remove(new Activity(key))) {
            return true;
        } else {
            throw new UniqueActivityList.ActivityNotFoundException();
        }
    }

    public boolean removeActivity(Activity key) throws UniqueActivityList.ActivityNotFoundException {
        if (activities.remove(key)) {
            return true;
        } else {
            throw new UniqueActivityList.ActivityNotFoundException();
        }
    }

//// util methods

    @Override
    public String toString() {
        return activities.getInternalList().size() + " activities";
    }

    @Override
    public List<Activity> getActivityList() {
        return Collections.unmodifiableList(activities.getInternalList());
    }

    @Override
    public UniqueActivityList getUniqueActivityList() {
        return this.activities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskMan taskMan = (TaskMan) o;
        return activities.equals(taskMan.activities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activities);
    }
}
