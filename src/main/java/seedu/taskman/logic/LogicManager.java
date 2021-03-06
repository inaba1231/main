package seedu.taskman.logic;

import javafx.collections.ObservableList;
import seedu.taskman.commons.core.ComponentManager;
import seedu.taskman.commons.core.LogsCenter;
import seedu.taskman.logic.commands.Command;
import seedu.taskman.logic.commands.CommandHistory;
import seedu.taskman.logic.commands.CommandResult;
import seedu.taskman.logic.parser.CommandParser;
import seedu.taskman.model.Model;
import seedu.taskman.model.TaskMan;
import seedu.taskman.model.event.Activity;
import seedu.taskman.storage.Storage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    public static final int HISTORY_SIZE = 10;
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandParser commandParser;
    private final Storage storage;
    private final Deque<CommandHistory> historyDeque;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.commandParser = new CommandParser();
        this.storage = storage;
        this.historyDeque = new ArrayDeque<>(HISTORY_SIZE);
    }

    public static LogicManager generateForTest(Model model, Storage storage, Deque<CommandHistory> historyDeque) {
        return new LogicManager(model, storage, historyDeque);
    }

    public LogicManager(Model model, Storage storage, Deque<CommandHistory> historyDeque) {
        this.model = model;
        this.commandParser = new CommandParser();
        this.storage = storage;
        this.historyDeque = historyDeque;
    }

    @Override
    public CommandResult execute(String commandText) {
        TaskMan oldTaskMan = new TaskMan(model.getTaskMan());

        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = commandParser.parseCommand(commandText);
        command.setData(model, storage, historyDeque);
        CommandResult result = command.execute();

        if (result.succeeded && command.storeHistory) {
            CommandHistory history = new CommandHistory(commandText, result.feedbackToUser, oldTaskMan);
            historyDeque.push(history);
        }

        return result;
    }

    @Override
    public ObservableList<Activity> getSortedScheduleList() {
        return model.getSortedScheduleList();
    }

    @Override
    public ObservableList<Activity> getSortedDeadlineList() {
        return model.getSortedDeadlineList();
    }

    @Override
    public ObservableList<Activity> getSortedFloatingList() {
        return model.getSortedFloatingList();
    }
}
