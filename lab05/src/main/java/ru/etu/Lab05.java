package ru.etu;

import java.util.InputMismatchException;
import java.util.Scanner;
import ru.etu.model.Student;
import ru.etu.service.StudentService;

public class Lab05 {

    public static void main(String[] args) {
        final StudentService service = new StudentService();
        final Scanner sc = service.getScanner();
        
        Integer input;
        boolean done = false;
        while (!done) {
            System.out.println("1 - create new student\n2 - edit existing student\n3 - list all students\n" +
                    "4 - list students in group\n5 - list top students\n6 - count male and female students\n" +
                    "7 - scholarship\n8 - list students with group id\n0 - exit");
            System.out.print("Please choose action: ");
            try {
                input = sc.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Please, enter a valid action (0 - 9)");
                continue;
            } finally {
                sc.nextLine();
            }

            switch (input) {
                case 0:
                    done = true;
                    break;
                case 1:
                    service.createNewStudent();
                    break;
                case 2:
                    Long id;
                    while (true) {
                        System.out.println("Please enter student id (or enter 0 to list all ids)");
                        try {
                            id = sc.nextLong();
                        } catch (InputMismatchException ime) {
                            System.out.println("Invalid id");
                            break;
                        } finally {
                            sc.nextLine();
                        }
                        if (id == 0) {
                            service.getAll()
                                    .keySet()
                                    .stream()
                                    .mapToLong(Long::longValue)
                                    .distinct()
                                    .forEach(sid -> System.out.print(sid + ' '));
                            System.out.println();
                            continue;
                        }
                        service.editExistingStudentById(id);
                        break;
                    }
                    break;
                case 3:
                    service.listAllStudents();
                    break;
                case 4:
                    String group;
                    while (true) {
                        System.out.println("Please enter group (or enter 0 to list all groups)");
                        group = sc.nextLine();
                        if (group.equals("0")) {
                            service.getAll()
                                    .values()
                                    .stream()
                                    .map(Student::getGroup)
                                    .distinct()
                                    .forEach(sgroup -> System.out.print(sgroup + ' '));
                            System.out.println();
                            continue;
                        }
                        service.listStudentsWithGroup(group);
                        break;
                    }
                    break;
                case 5:
                    service.listTopStudents();
                    break;
                case 6:
                    service.listCountMaleAndFemaleStudents();
                    break;
                case 7:
                    service.listHasOnly5();
                    service.listHasOnly4And5();
                    service.listHasNoScholarship();
                    break;
                case 8:
                    Integer groupId;
                    while (true) {
                        System.out.println("Please enter student group id (or enter 0 to list all group ids)");
                        try {
                            groupId = sc.nextInt();
                        } catch (InputMismatchException ime) {
                            System.out.println("Invalid groupId");
                            break;
                        } finally {
                            sc.nextLine();
                        }
                        if (groupId == 0) {
                            service.getAll()
                                    .values()
                                    .stream()
                                    .mapToLong(Student::getGroupId)
                                    .distinct()
                                    .forEach(sid -> System.out.print(sid + ' '));
                            System.out.println();
                            continue;
                        }
                        service.listStudentsWithGroupId(groupId);
                        break;
                    }
                    break;
                default:
                    System.out.println("Please, enter a valid action (0 - 9)");
                    break;
            }
        }
    }

}
