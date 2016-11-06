package seedu.taskman.testutil;

import com.google.common.io.Files;
import guitests.guihandles.TaskRowHandle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import junit.framework.AssertionFailedError;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import seedu.taskman.TestApp;
import seedu.taskman.commons.exceptions.IllegalValueException;
import seedu.taskman.commons.util.FileUtil;
import seedu.taskman.commons.util.XmlUtil;
import seedu.taskman.model.TaskMan;
import seedu.taskman.model.tag.Tag;
import seedu.taskman.model.tag.UniqueTagList;
import seedu.taskman.model.event.*;
import seedu.taskman.storage.XmlSerializableTaskMan;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    public static String LS = System.lineSeparator();

    public static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
        try {
            executable.run();
        } catch (Throwable actualException) {
            if (!actualException.getClass().isAssignableFrom(expected)) {
                String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
                        actualException.getClass().getName());
                throw new AssertionFailedError(message);
            } else return;
        }
        throw new AssertionFailedError(
                String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
    }

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    public static final Task[] sampleTaskData = getSampleTaskData();

    private static Task[] getSampleTaskData() {
        try {
            return new Task[]{
                    new Task(new Title("Ali Muster"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("Boris Mueller"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("Carl Kurz"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("Daniel Meier"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("Elle Meyer"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("Fiona Kunz"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("George Best"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("Hoon Meier"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
                    new Task(new Title("Ida Mueller"), new UniqueTagList(), new Deadline("next tues"), new Schedule("wed 10am, wed 11am")),
            };
        } catch (IllegalValueException e) {
            assert false;
            //not possible
            return null;
        }
    }

    public static final Tag[] sampleTagData = getSampleTagData();

    private static Tag[] getSampleTagData() {
        try {
            return new Tag[]{
                    new Tag("relatives"),
                    new Tag("friends")
            };
        } catch (IllegalValueException e) {
            assert false;
            return null;
            //not possible
        }
    }

    public static List<Task> generateSampleTaskData() {
        return Arrays.asList(sampleTaskData);
    }

    /**
     * Appends the file name to the sandbox folder path.
     * Creates the sandbox folder if it doesn't exist.
     *
     * @param fileName
     * @return
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    public static void createDataFileWithSampleData(String filePath) {
        createDataFileWithData(generateSampleStorageTaskMan(), filePath);
    }

    public static <T> void createDataFileWithData(T data, String filePath) {
        try {
            File saveFileForTesting = new File(filePath);
            FileUtil.createIfMissing(saveFileForTesting);
            XmlUtil.saveDataToFile(saveFileForTesting, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... s) {
        createDataFileWithSampleData(TestApp.SAVE_LOCATION_FOR_TESTING);
    }

    public static TaskMan generateEmptyTaskMan() {
        return new TaskMan(new UniqueActivityList(), new UniqueTagList());
    }

    public static XmlSerializableTaskMan generateSampleStorageTaskMan() {
        return new XmlSerializableTaskMan(generateEmptyTaskMan());
    }

    /**
     * Tweaks the {@code keyCodeCombination} to resolve the {@code KeyCode.SHORTCUT} to their
     * respective platform-specific key codes
     */
    public static KeyCode[] scrub(KeyCodeCombination keyCodeCombination) {
        List<KeyCode> keys = new ArrayList<>();
        if (keyCodeCombination.getAlt() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.ALT);
        }
        if (keyCodeCombination.getShift() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.SHIFT);
        }
        if (keyCodeCombination.getMeta() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.META);
        }
        if (keyCodeCombination.getControl() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.CONTROL);
        }
        keys.add(keyCodeCombination.getCode());
        return keys.toArray(new KeyCode[]{});
    }

    public static boolean isHeadlessEnvironment() {
        String headlessProperty = System.getProperty("testfx.headless");
        return headlessProperty != null && headlessProperty.equals("true");
    }

    public static void captureScreenShot(String fileName) {
        File file = GuiTest.captureScreenshot();
        try {
            Files.copy(file, new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String descOnFail(Object... comparedObjects) {
        return "Comparison failed \n"
                + Arrays.asList(comparedObjects).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    public static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        // ~Modifier.FINAL is used to remove the final modifier from field so that its value is no longer
        // final and can be changed
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    public static void initRuntime() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.hideStage();
    }

    public static void tearDownRuntime() throws Exception {
        FxToolkit.cleanupStages();
    }

    /**
     * Gets private method of a class
     * Invoke the method using method.invoke(objectInstance, params...)
     * <p>
     * Caveat: only find method declared in the current Class, not inherited from supertypes
     */
    public static Method getPrivateMethod(Class objectClass, String methodName) throws NoSuchMethodException {
        Method method = objectClass.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method;
    }

    public static void renameFile(File file, String newFileName) {
        try {
            Files.copy(file, new File(newFileName));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Gets mid point of a node relative to the screen.
     *
     * @param node
     * @return
     */
    public static Point2D getScreenMidPoint(Node node) {
        double x = getScreenPos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScreenPos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    /**
     * Gets mid point of a node relative to its scene.
     *
     * @param node
     * @return
     */
    public static Point2D getSceneMidPoint(Node node) {
        double x = getScenePos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScenePos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    /**
     * Gets the bound of the node relative to the parent scene.
     *
     * @param node
     * @return
     */
    public static Bounds getScenePos(Node node) {
        return node.localToScene(node.getBoundsInLocal());
    }

    public static Bounds getScreenPos(Node node) {
        return node.localToScreen(node.getBoundsInLocal());
    }

    public static double getSceneMaxX(Scene scene) {
        return scene.getX() + scene.getWidth();
    }

    public static double getSceneMaxY(Scene scene) {
        return scene.getX() + scene.getHeight();
    }

    public static Object getLastElement(List<?> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Removes a subset from the list of activities.
     *
     * @param activities         The list of activities
     * @param activitiesToRemove The subset of activities.
     * @return The modified activities after removal of the subset from activities.
     */
    public static Activity[] removeActivitiesFromList(final Activity[] activities, Activity... activitiesToRemove) {
        List<Activity> activityList = asList(activities);
        activityList.removeAll(asList(activitiesToRemove));
        return activityList.toArray(new Activity[activityList.size()]);
    }


    /**
     * Returns a copy of the list with the task at specified index removed.
     *
     * @param list                          original list to copy from
     * @param targetIndexInOneIndexedFormat e.g. if the first element to be removed, 1 should be given as index.
     */
    public static Activity[] removeActivityFromList(final Activity[] list, int targetIndexInOneIndexedFormat) {
        return removeActivitiesFromList(list, list[targetIndexInOneIndexedFormat - 1]);
    }

    /**
     * Replaces tasks[i] with a task.
     *
     * @param tasks The array of tasks.
     * @param task  The replacement task
     * @param index The index of the task to be replaced.
     * @return
     */
    public static TestTask[] replaceTaskFromList(TestTask[] tasks, TestTask task, int index) {
        tasks[index] = task;
        return tasks;
    }

    /**
     * Appends tasks to the list of tasks.
     *
     * @param tasks      A array of tasks.
     * @param tasksToAdd The tasks that are to be appended behind the original list.
     * @return A new list of tasks.
     */
    public static List<TestTask> addTasksToList(final List<TestTask> tasks, TestTask... tasksToAdd) {
        List<TestTask> tasksList = new ArrayList<>(tasks);
        tasksList.addAll(asList(tasksToAdd));
        return tasksList;
    }

    /**
     * Sorts the Activity objects in the array by their deadlines
     * All Activity objects in the array should have a deadline.
     * @param activities
     */
    public static void sortActivitiesByDeadline(Activity[] activities){
        Arrays.sort(activities, (o1, o2)
                -> Long.compare(o1.getDeadline().get().epochSecond,
                        o2.getDeadline().get().epochSecond));
    }

    /**
     * Converts the given list of TestTasks to an array of Activity objects
     * @param tasks
     * @return array of Activity objects constructed from the given TestTask objects
     */
    public static Activity[] getActivitiesArray(List<TestTask> tasks){
        return tasks.stream()
                .map(a -> new Activity(new Task(a)))
                .collect(Collectors.toList())
                .toArray(new Activity[tasks.size()]);
    }

    public static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareRowAndTask(TaskRowHandle row, Activity task) {
        return row.isSameTask(task);
    }

    public static Tag[] getTagList(String tags) {

        if (tags.equals("")) {
            return new Tag[]{};
        }

        final String[] split = tags.split(", ");

        final List<Tag> collect = Arrays.asList(split).stream().map(e -> {
            try {
                return new Tag(e.replaceFirst("Tag: ", ""));
            } catch (IllegalValueException e1) {
                //not possible
                assert false;
                return null;
            }
        }).collect(Collectors.toList());

        return collect.toArray(new Tag[split.length]);
    }

}
