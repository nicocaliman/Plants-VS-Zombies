package tp1.p2.control;

import static tp1.p2.view.Messages.error;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import tp1.p2.control.commands.AddPlantCheatCommand;
import tp1.p2.control.commands.AddPlantCommand;
import tp1.p2.control.commands.AddZombieCommand;
import tp1.p2.control.commands.CatchCommand;
import tp1.p2.control.commands.ExitCommand;
import tp1.p2.control.commands.HelpCommand;
import tp1.p2.control.commands.ListPlantsCommand;
import tp1.p2.control.commands.ListZombiesCommand;
import tp1.p2.control.commands.NoneCommand;
import tp1.p2.control.commands.ResetCommand;
import tp1.p2.logic.GameWorld;
import tp1.p2.view.Messages;

/**
 * Represents a user action in the game.
 *
 */
public abstract class Command
{

	/* @formatter:off */
	private static final List<Command> AVAILABLE_COMMANDS = Arrays.asList(
		new AddPlantCommand(),
		new ListPlantsCommand(),
		new ResetCommand(),
		new HelpCommand(),
		new ExitCommand(),
		new NoneCommand(),
		new ListZombiesCommand(),
		new AddZombieCommand(),
		new AddPlantCheatCommand(),
		new CatchCommand()
	);
	/* @formatter:on */

	private static Command defaultCommand;

	public static Command parse(String[] commandWords) 
	{
		if (commandWords.length == 1 && commandWords[0].isEmpty())
		{			
			NoneCommand noneCommand = new NoneCommand();
			
			defaultCommand = noneCommand;									//set NoneCommand as default command
			
			return defaultCommand;			
		}

		for (Command command : AVAILABLE_COMMANDS)
		{
			if (command.matchCommand(commandWords[0])) 					//if command matches with user input
			{				
				command.create(commandWords);								//create command					
				
				return command;								
			}
		}
		
		System.out.println(error(Messages.UNKNOWN_COMMAND));
		
		return null;
	}

	public static Iterable<Command> getAvailableCommands() 
	{
		return Collections.unmodifiableList(AVAILABLE_COMMANDS);
	}

	public static void newCycle() 
	{
		for(Command c : AVAILABLE_COMMANDS) 
		{
			c.newCycleStarted();
		}
	}

	public Command() 
	{
		this(false);
	}

	public Command(boolean isDefault) 
	{
		if (isDefault) 
		{
			// TODO add your code here
			
			Command.defaultCommand = this;		
		}
	}

	abstract protected String getName();

	abstract protected String getShortcut();

	abstract public String getDetails();

	abstract public String getHelp();

	public boolean isDefaultCommand()
	{
		return Command.defaultCommand == this;
	}

	public boolean matchCommand(String token)
	{
		String shortcut = getShortcut();
		
		String name = getName();
		
		return shortcut.equalsIgnoreCase(token) || name.equalsIgnoreCase(token)
				|| (isDefaultCommand() && "".equals(token));
	}

	/**
	 * Execute the command.
	 * 
	 * @param game Where to execute the command.
	 * 
	 * @return {@code true} if game board must be printed {@code false} otherwise.
	 */
	public abstract ExecutionResult execute(GameWorld game);

	public Command create(String[] parameters) 
	{			
		return this;
	}

	/**
	 * Notifies the {@link Command} that a new cycle has started.
	 */
	protected void newCycleStarted()
	{
		
	}
}