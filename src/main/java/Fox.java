import java.util.Scanner;

public class Fox {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String greeting = "  /\\_/\\\n"
                + " ( •ᴗ• )   Hi! I'm Fox, your little companion. 🦊\n"
                + "  > ^ <    I may be small, but I've got plenty of tricks up my sleeve.\n"
                + "\n"
                + "           What can I do for you?\n"
                + "\n"
                + "------------------------------------------------------------\n";

        String farewell = "  /\\_/\\\n"
                + " ( -.- )   Bye for now! 🌙\n"
                + "  > ^ <    I'm off to the fox den. Wake me up anytime you're in need!\n"
                + "------------------------------------------------------------\n";

        System.out.print(greeting);

        while (true) {
            String command = scanner.nextLine();

            System.out.println(command);
            System.out.println("------------------------------------------------------------");

            if (command.equals("bye")) {
                System.out.print(farewell);
                break;
            }
        }

        scanner.close();
    }
}