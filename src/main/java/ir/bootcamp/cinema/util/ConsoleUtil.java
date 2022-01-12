package ir.bootcamp.cinema.util;


public class ConsoleUtil {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void print(String message, ConsoleMessageType consoleMessageType) {
        String colorCode = "";
        String reset = "\u001B[0m";
        switch (consoleMessageType) {
            case info:
                colorCode = "\u001B[34m";
                break;
            case error:
                colorCode = "\u001B[31m";
                break;
            case success:
                colorCode = "\u001B[32m";
        }
        System.out.println(colorCode);
        System.out.print("-".repeat(message.length()+6));
        System.out.println("\n-- " + message + " --");
        System.out.print("-".repeat(message.length()+6));
        System.out.println(reset);
    }

}