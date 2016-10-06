package seedu.taskman.logic.commands;

import java.util.Set;

/**
 * Finds and lists all tasks in task man whose title contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose titles contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";
    
    public static final String MESSAGE_SUCCESS = "Listed all tasks";

    private final Set<String> keywords;

    public ListCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
        if (keywords.isEmpty()) {
            model.updateFilteredListToShowAll();
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            model.updateFilteredTaskList(keywords);
            return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
        }
    }

}
