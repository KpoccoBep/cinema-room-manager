package cinema;

import java.util.ArrayList;
import java.util.Scanner;

class Seat {
    final int row;
    final int seat;
    final int price;

    public Seat(int row, int seat, int price) {
        this.row = row;
        this.seat = seat;
        this.price = price;
    }
}

enum Action {
    EXIT, SHOW_SEATS, BUY, SHOW_STATS, UNKNOWN
}

public class Cinema {
    static Scanner scanner = new Scanner(System.in);
    static int rowsNumber;
    static int seatsNumber;
    static int totalSeatsNumber;
    static int totalIncome;
    static ArrayList<Seat> seatsSold = new ArrayList<>();

    private static void getCinemaParams() {
        System.out.println("Enter the number of rows:");
        rowsNumber = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        seatsNumber = scanner.nextInt();
        totalSeatsNumber = rowsNumber * seatsNumber;

        totalIncome = 0;
        for (int i = 1; i <= rowsNumber; i++) {
            totalIncome += getPrice(i) * seatsNumber;
        }
    }

    private static int getPrice(int row) {
        if (totalSeatsNumber <= 60) {
            return 10;
        } else {
            int frontHalfRows = rowsNumber / 2;
            return row <= frontHalfRows ? 10 : 8;
        }
    }

    private static boolean checkSeat(int row, int seat) {
        for (Seat s : seatsSold) {
            if (s.row == row && s.seat == seat) {
                return true;
            }
        }
        return false;
    }

    private static void chooseSeat() {
        int row;
        int seat;

        while(true) {
            System.out.println();
            System.out.println("Enter a row number:");
            row = scanner.nextInt();
            System.out.println("Enter a seat number in that row:");
            seat = scanner.nextInt();

            if (row < 1 || row > rowsNumber || seat < 1 || seat > seatsNumber) {
                System.out.println();
                System.out.println("Wrong input!");
            } else if (checkSeat(row, seat)) {
                System.out.println();
                System.out.println("That ticket has already been purchased!");
            } else {
                break;
            }
        }

        int price = getPrice(row);
        System.out.println();
        System.out.printf("Ticket price: $%d\n", price);

        Seat chosenSeat = new Seat(row, seat, price);
        seatsSold.add(chosenSeat);
    }

    private static void printScreenRoomState() {
        System.out.println();
        System.out.println("Cinema:");
        System.out.print(" ");
        for (int seat = 1; seat <= seatsNumber; seat++) {
            System.out.printf(" %d", seat);
        }
        System.out.println();

        for (int row = 1; row <= rowsNumber; row++) {
            System.out.print(row);
            for (int seat = 1; seat <= seatsNumber; seat++) {
                boolean sold = checkSeat(row, seat);
                System.out.print(sold ? " B" : " S");
            }
            System.out.println();
        }
    }

    private static Action chooseAction() {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");

        int n = scanner.nextInt();
        switch (n) {
            case 0: return Action.EXIT;
            case 1: return Action.SHOW_SEATS;
            case 2: return Action.BUY;
            case 3: return Action.SHOW_STATS;
            default: return Action.UNKNOWN;
        }
    }

    private static void printStats() {
        int currentIncome = 0;
        for (Seat s : seatsSold) {
            currentIncome += s.price;
        }

        System.out.println();
        System.out.printf("Number of purchased tickets: %d\n", seatsSold.size());
        System.out.printf("Percentage: %.2f%%\n", (double) seatsSold.size() / totalSeatsNumber * 100);
        System.out.printf("Current income: $%d\n", currentIncome);
        System.out.printf("Total income: $%d\n", totalIncome);
    }

    public static void main(String[] args) {
        getCinemaParams();

        boolean exit = false;

        while (!exit) {
            Action action = chooseAction();
            switch (action) {
                case SHOW_SEATS:
                    printScreenRoomState();
                    break;
                case BUY:
                    chooseSeat();
                    break;
                case SHOW_STATS:
                    printStats();
                    break;
                case EXIT:
                    exit = true;
                    break;
                default:
            }
        }
    }
}