package ru.etu;

import static java.util.Objects.isNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import ru.etu.common.LinkedListWrapper;
import ru.etu.common.RunnableBenchmark;

public class Lab06 {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Random RANDOM = new Random(System.currentTimeMillis()); 

    private static final Integer UPPER_BOUND = 100;

    public static void main(String[] args) {
        final LinkedListWrapper<Integer> list = new LinkedListWrapper<>();

        String input;
        boolean done = false;
        System.out.println("1. Create double-linked list and fill it");
        while (!done) {
            System.out.print("Fill list manually? (Y/N): ");
            input = SCANNER.nextLine();
            switch (input.toLowerCase()) {
                case "y":
                    done = true;
                    Lab06.fillListManually(list);
                    break;
                case "n":
                    done = true;
                    Lab06.fillListRandomly(list);
                    break;
                default:
                    System.out.println("Please, enter 'Y' or 'N'");
            }
        }
        
        System.out.println("2. Benchmark creation of double-linked list");
        System.out.println(Lab06.benchmarkListCreation(list));

        System.out.println("3. Insert, removal, swapping and getting elements of a double-linked list");
        done = false;
        int choice, value, index;
        while (!done) {
            System.out.println("0 - print list\n1 - insert at the end\n2 - insert at index\n3 - remove at index\n"
                    + "4 - remove value\n5 - get at index\n6 - get value\n7 - swap elements\n8 - exit");
            System.out.print("Select action: ");
            input = SCANNER.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Please, enter a number");
                continue;
            }
            switch (choice) {
                case 0:
                    System.out.println("List:");
                    System.out.println(list.toString());
                    break;
                case 1:
                    while (true) {
                        System.out.print("Enter value to insert: ");
                        input = SCANNER.nextLine();
                        try {
                            value = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        break;
                    }
                    list.add(value);
                    System.out.println("Added " + value + " to list");
                    break;
                case 2:
                    while (true) {
                        System.out.print("Enter value to insert: ");
                        input = SCANNER.nextLine();
                        try {
                            value = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        break;
                    }
                    while (true) {
                        System.out.print("Enter index: ");
                        input = SCANNER.nextLine();
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (index < 0 || index >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    list.add(index, value);
                    System.out.println("Added " + value + " to list at position " + index);
                    break;
                case 3:
                    if (list.isEmpty()) {
                        System.out.println("List is empty. Can't remove nothing");
                        break;
                    }
                    while (true) {
                        System.out.print("Enter index: ");
                        input = SCANNER.nextLine();
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (index < 0 || index >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    list.remove((int) index);
                    System.out.println("Removed element at position " + index);
                    break;
                case 4:
                    if (list.isEmpty()) {
                        System.out.println("List is empty. Can't remove nothing");
                        break;
                    }
                    while (true) {
                        System.out.print("Enter value to remove: ");
                        input = SCANNER.nextLine();
                        try {
                            value = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        break;
                    }
                    if (list.remove((Integer) value)) {
                        System.out.println("Removed first occurence of " + value);
                    } else {
                        System.out.println("Value " + value + " not found in list");
                    }
                    break;
                case 5:
                    while (true) {
                        System.out.print("Enter index: ");
                        input = SCANNER.nextLine();
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (index < 0 || index >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    value = list.get(index);
                    System.out.println("Element at position " + index + " is " + value);
                    break;
                case 6:
                    while (true) {
                        System.out.print("Enter value to find: ");
                        input = SCANNER.nextLine();
                        try {
                            value = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        break;
                    }
                    index = list.indexOf(value);
                    if (index == -1) {
                        System.out.println("No element with value " + value + " found");
                    } else {
                        System.out.println("Value " + value + " is at position " + index);
                    }
                    break;
                case 7:
                    while (true) {
                        System.out.print("Enter first index: ");
                        input = SCANNER.nextLine();
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (index < 0 || index >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    while (true) {
                        System.out.print("Enter second index: ");
                        input = SCANNER.nextLine();
                        try {
                            value = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (value < 0 || value >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    list.swap(index, value);
                    System.out.println("Swapped elements at " + index + " and " + value);
                    break;
                case 8:
                    done = true;
                    break;
                default:
                    System.out.println("Please, enter a valid number (0 - 8)");
            }
        }

        System.out.println("4. Benchmark insert, remove and get operations on double-linked list");
        done = false;
        while (!done) {
            System.out.println("1 - benchmark insert ops\n2 - benchmark remove ops\n3 - benchmark get ops\n4 - exit");
            System.out.print("Select action: ");
            input = SCANNER.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Please, enter a number");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println(Lab06.benchmarkInsertAtEndOps(list));
                    while (true) {
                        System.out.print("Enter index: ");
                        input = SCANNER.nextLine();
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (index < 0 || index >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    System.out.println(Lab06.benchmarkInsertAtIndexOps(index, list));
                    break;
                case 2:
                    while (true) {
                        System.out.print("Enter index: ");
                        input = SCANNER.nextLine();
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (index < 0 || index >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    System.out.println(Lab06.benchmarkRemoveAtIndexOps(index, list));
                    while (true) {
                        System.out.print("Enter value: ");
                        input = SCANNER.nextLine();
                        try {
                            value = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        break;
                    }
                    System.out.println(Lab06.benchmarkRemoveValueOps(value, list));
                    break;
                case 3:
                    while (true) {
                        System.out.print("Enter index: ");
                        input = SCANNER.nextLine();
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        if (index < 0 || index >= list.size()) {
                            System.out.println("Please enter a valid index (0 - " + (list.size() - 1) + ")");
                            continue;
                        }
                        break;
                    }
                    System.out.println(Lab06.benchmarkGetByIndexOps(index, list));
                    while (true) {
                        System.out.print("Enter value: ");
                        input = SCANNER.nextLine();
                        try {
                            value = Integer.parseInt(input);
                        } catch (NumberFormatException nfe) {
                            System.out.println("Please, enter a number");
                            continue;
                        }
                        break;
                    }
                    System.out.println(Lab06.benchmarkGetValueOps(value, list));
                    break;
                case 4:
                    done = true;
                    break;
                default:
                    System.out.println("Please, enter a valid number (1 - 4)");
            }
        }
    }

    private static void fillListManually(List<Integer> list) {
        String input;
        Integer value;
        int i = 1;
        while (true) {
            System.out.print("Enter value " + i + " or enter 'stop' to stop: ");
            input = SCANNER.nextLine();
            if (input.equalsIgnoreCase("stop")) {
                break;
            }
            try {
                value = Integer.parseInt(input);
                list.add(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter valid number");
            }
        }
    }

    private static void fillListRandomly(List<Integer> list) {
        String input;
        Integer size;
        while (true) {
            System.out.print("Please enter desired list size: ");
            input = SCANNER.nextLine();
            try {
                size = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter valid number");
                continue;
            }
            if (size < 1) {
                System.out.println("Please enter a positive number");
                continue;
            }
            break;
        }

        for (int i = 0; i < size; ++i) {
            list.add(RANDOM.nextInt(UPPER_BOUND));
        }
    }

    private static RunnableBenchmark benchmarkListCreation(List<Integer> list) {
        final RunnableBenchmark benchmark = RunnableBenchmark.of(
            "Creating Linked List",
            () -> new LinkedList<>(list)
        );
        benchmark.benchmark();

        return benchmark;
    }

    private static RunnableBenchmark benchmarkInsertAtEndOps(List<Integer> list) {
        final List<Integer> initialList = List.copyOf(list);
        final RunnableBenchmark benchmark = RunnableBenchmark.of(
            "Inserting at the end",
            () -> list.add(999)
        );
        benchmark.benchmark();

        list.retainAll(initialList);

        return benchmark;
    }

    private static RunnableBenchmark benchmarkInsertAtIndexOps(int index, List<Integer> list) {
        if (index < 0 || index > list.size())
            index = RANDOM.nextInt(list.size());
        final int finalIndex = index;
        final List<Integer> initialList = List.copyOf(list);
        final RunnableBenchmark benchmark = RunnableBenchmark.of(
            "Inserting at index " + finalIndex,
            () -> list.add(finalIndex, 999)
        );
        benchmark.benchmark();

        list.retainAll(initialList);

        return benchmark;
    }

    private static RunnableBenchmark benchmarkRemoveAtIndexOps(int index, List<Integer> list) {
        if (index < 0 || index > list.size())
            index = RANDOM.nextInt(list.size());
        final int finalIndex = index;
        final List<Integer> initialList = List.copyOf(list);
        final RunnableBenchmark benchmark = RunnableBenchmark.of(
            "Removing at index " + finalIndex,
            () -> list.remove((int) finalIndex)
        );
        benchmark.benchmarkWithReset(() -> list.add(RANDOM.nextInt(UPPER_BOUND)));

        list.clear();
        list.addAll(initialList);

        return benchmark;
    }

    private static RunnableBenchmark benchmarkRemoveValueOps(Integer value, List<Integer> list) {
        if (isNull(value) || !list.contains((Integer) value)) 
            value = list.get(RANDOM.nextInt(list.size()));
        final Integer finalValue = value;
        final List<Integer> initialList = List.copyOf(list);
        final RunnableBenchmark benchmark = RunnableBenchmark.of(
            "Removing value " + finalValue,
            () -> list.remove((Integer) finalValue)
        );
        benchmark.benchmarkWithReset(() -> list.add(RANDOM.nextInt(list.size() + 1), finalValue));

        list.clear();
        list.addAll(initialList);

        return benchmark;
    }

    private static RunnableBenchmark benchmarkGetByIndexOps(int index, List<Integer> list) {
        if (index < 0 || index > list.size())
            index = RANDOM.nextInt(list.size());
        final int finalIndex = index;
        final RunnableBenchmark benchmark = RunnableBenchmark.of(
            "Getting value at " + finalIndex,
            () -> list.get(finalIndex)
        );
        benchmark.benchmark();

        return benchmark;
    }

    private static RunnableBenchmark benchmarkGetValueOps(Integer value, List<Integer> list) {
        if (isNull(value) || !list.contains((Integer) value)) 
            value = list.get(RANDOM.nextInt(list.size()));
        final Integer finalValue = value;
        final RunnableBenchmark benchmark = RunnableBenchmark.of(
            "Getting index of value " + finalValue,
            () -> list.indexOf(finalValue)
        );
        benchmark.benchmark();

        return benchmark;
    }
    
}
