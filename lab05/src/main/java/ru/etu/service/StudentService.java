package ru.etu.service;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import static ru.etu.model.Student.GROUP_REGEXP;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import ru.etu.enums.Department;
import ru.etu.enums.Mark;
import ru.etu.enums.Sex;
import ru.etu.model.Student;
import ru.etu.repository.StudentRepository;

public class StudentService {

    private final StudentRepository studentRepository;

    private final Scanner sc;

    public StudentService() {
        this.studentRepository = StudentRepository.getInstance();
        this.sc = new Scanner(System.in);
    }

    /*
     * 1. Создание новой записи о студенте.
     * 2. Внесение изменений в уже имеющуюся запись.
     * 3. Вывод всех данных о студентах.
     * 4. Вывод информации обо всех студентах группы N. N – инициализируется пользователем.
     * 5. Вывод топа самых успешных студентов с наивысшим по рейтингу средним баллом за прошедшую сессию.
     * 6. Вывод количества студентов мужского и женского пола.
     * 7. Вывод данных о студентах, которые не получают стипендию; учатся только на «хорошо» и «отлично»; учатся только на «отлично»;
     * 8. Вывод данных о студентах, имеющих номер в списке – k.
     */

    public Scanner getScanner() {
        return this.sc;
    }

    public Map<Long, Student> getAll() {
        return studentRepository.getAll();
    }

    public void createNewStudent() {
        System.out.println("Creating new student");

        Student student = Student.builder()
                .firstName(this.queryForFirstName())
                .middleName(this.queryForMiddleName())
                .lastName(this.queryForLastName())
                .birthDate(this.queryForBirthDate())
                .sex(this.queryForSex())
                .group(this.queryForGroup())
                .groupId(this.queryForGroupId())
                .department(this.queryForDepartment())
                .marks(this.queryForMarks())
                .build();

        studentRepository.addStudent(student);
        System.out.println("Student saved: " + student.toString());
    }

    public void editExistingStudentById(final Long id) {
        System.out.println("Editing student with id = " + id);

        final Student student = studentRepository.getById(id);

        this._editExistingStudent(id, student);
    }

    public void listAllStudents() {
        System.out.println("Listing all students");

        studentRepository.getAll()
                .values()
                .stream()
                .forEach(s -> System.out.println("- " + s.toString()));
    }

    public void listStudentsWithGroup(final String group) {
        if (isNull(group) || group.isBlank() || !group.matches(GROUP_REGEXP)) {
            System.err.println("listAllStudentsFromGroup(): Invalid group: " + group);
            return;
        }
        System.out.println("Listing all students from group " + group);

        studentRepository.getAll()
                .values()
                .stream()
                .filter(s -> s.getGroup().equals(group))
                .forEach(s -> System.out.println("- " + s.toString()));
    }

    public void listTopStudents() {
        this.listTopStudents(5);
    }

    public void listTopStudents(final int count) {
        System.out.println("Listing top " + count + " students");
        int[] index = new int[] {0};

        studentRepository.getAll()
                .values()
                .stream()
                .sorted((s1, s2) -> s1.getMeanMark() > s2.getMeanMark() ? 1 : -1)
                .limit(count)
                .forEach(s -> System.out.println(++index[0] + " - " + s.toString()));
    }

    public void listCountMaleAndFemaleStudents() {
        System.out.println("Listing amount of male and female students");

        System.out.println("Male: " +
            studentRepository.getAll()
                    .values()
                    .stream()
                    .filter(s -> s.getSex() == Sex.MALE)
                    .count()
        );
        System.out.println("Female: " +
            studentRepository.getAll()
                    .values()
                    .stream()
                    .filter(s -> s.getSex() == Sex.FEMALE)
                    .count()
        );
    }

    public void listHasNoScholarship() {
        System.out.println("Listing students that have no scholarship");

        studentRepository.getAll()
                .values()
                .stream()
                .filter(s -> !s.hasScholarship())
                .forEach(s -> System.out.println("- " + s.toString()));
    }

    public void listHasOnly5() {
        System.out.println("Listing students that have only 5 marks");

        studentRepository.getAll()
                .values()
                .stream()
                .filter(Student::hasOnly5)
                .forEach(s -> System.out.println("- " + s.toString()));
    }

    public void listHasOnly4And5() {
        System.out.println("Listing students that have only 4 and 5 marks");

        studentRepository.getAll()
                .values()
                .stream()
                .filter(Student::hasOnly4And5)
                .forEach(s -> System.out.println("- " + s.toString()));
    }

    public void listStudentsWithGroupId(final Integer groupId) {
        if (isNull(groupId)) {
            System.err.println("listAllStudentsFromGroup(): Invalid group id: " + groupId);
            return;
        }
        System.out.println("Listing students with group id " + groupId);

        studentRepository.getAll()
                .values()
                .stream()
                .filter(s -> s.getGroupId() == groupId)
                .forEach(s -> System.out.println("- " + s.toString()));
    }

    private void _editExistingStudent(final Long id, final Student student) {
        Integer input;
        boolean done = false;
        while (!done) {
            System.out.println("1 - edit first name\n2 - edit middle name\n3 - edit last name\n" +
                    "4 - edit birth date\n5 - sex\n6 - group\n7 - group id\n8 - department\n" +
                    "9 - marks\n0 - save and exit");
            System.out.print("Please choose field to edit: ");
            try {
                input = sc.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Please, enter a valid action (0 - 9)");
                sc.nextLine();
                continue;
            }

            switch (input) {
                case 0:
                    done = true;
                    studentRepository.editStudent(id, student);
                    System.out.println("Student saved: " + student.toString());
                    break;
                case 1:
                    System.out.println("Previous value of first name: " + student.getFirstName());
                    student.setFirstName(this.queryForFirstName());
                    break;
                case 2:
                    System.out.println("Previous value of middle name: " + student.getMiddleName());
                    student.setMiddleName(this.queryForMiddleName());
                    break;
                case 3:
                    System.out.println("Previous value of last name: " + student.getLastName());
                    student.setLastName(this.queryForLastName());
                    break;
                case 4:
                    System.out.println("Previous value of birth date: " + student.getBirthDate());
                    student.setBirthDate(this.queryForBirthDate());
                    break;
                case 5:
                    System.out.println("Previous value of sex: " + student.getSex());
                    student.setSex(this.queryForSex());
                    break;
                case 6:
                    System.out.println("Previous value of group: " + student.getGroup());
                    student.setGroup(this.queryForGroup());
                    break;
                case 7:
                    System.out.println("Previous value of group id: " + student.getGroupId());
                    student.setGroupId(this.queryForGroupId());
                    break;
                case 8:
                    System.out.println("Previous value of department: " + student.getDepartment());
                    student.setDepartment(this.queryForDepartment());
                    break;
                case 9:
                    System.out.println("Previous values of marks:\n" + student.getMarks());
                    student.clearAllMarks();
                    student.putMarks(this.queryForMarks());
                    break;
                case -1:
                    done = true;
                    System.out.println("Changes discarder");
                    break;
                default:
                    System.out.println("Please, enter a valid action (0 - 9)");
                    break;
            }
            sc.nextLine();
        }
    }

    private String queryForFirstName() {
        String input;
        System.out.print("Enter student first name: ");
        while (true) {
            input = sc.nextLine();
            if (nonNull(input) && !input.isBlank()) {
                return input;
            }
            System.out.println("Please, enter a valid name");
        }
    }

    private String queryForMiddleName() {
        String input;
        System.out.print("Enter student middle name: ");
        input = sc.nextLine();
        if (nonNull(input) && !input.isBlank()) {
            return input;
        } else {
            return null;
        }
    }

    private String queryForLastName() {
        String input;
        System.out.print("Enter student last name: ");
        while (true) {
            input = sc.nextLine();
            if (nonNull(input) && !input.isBlank()) {
                return input;
            }
            System.out.println("Please, enter a valid name");
        }
    }

    private LocalDate queryForBirthDate() {
        String input;
        System.out.print("Enter student birth date (format: YYYY-MM-DD): ");
        LocalDate birthDate;
        while (true) {
            input = sc.nextLine();
            if (nonNull(input) && !input.isBlank()) {
                try {
                    birthDate = LocalDate.parse(input);
                    return birthDate;
                } catch (DateTimeParseException dtpe) {
                    System.out.println("Please, enter a valid birth date");
                    continue;
                }
            }
            System.out.println("Please, enter a valid birth date");
        }
    }

    private Sex queryForSex() {
        String input;
        System.out.print("Enter student sex (valid values: 'M' or 'F'): ");
        Sex sex;
        while (true) {
            input = sc.nextLine();
            if (nonNull(input)
                    && !input.isBlank()
                    && input.length() == 1
                    && nonNull(sex = Sex.of(input.charAt(0))))
            {
                return sex;
            }
            System.out.println("Please, enter a valid sex");
        }
    }

    private String queryForGroup() {
        String input;
        System.out.print("Enter student group (format: 'NNNN', where 'N' is a number): ");
        while (true) {
            input = sc.nextLine();
            if (nonNull(input) && !input.isBlank() && input.matches(GROUP_REGEXP)) {
                return input;
            }
            System.out.println("Please, enter a valid group");
        }
    }

    private Integer queryForGroupId() {
        Integer input;
        System.out.print("Enter student group id (a positive integer): ");
        while (true) {
            try {
                input = sc.nextInt();
                if (nonNull(input) && input > 0) {
                    sc.nextLine();
                    return input;
                }
            } catch (InputMismatchException ignored) {}
            sc.nextLine();
            System.out.println("Please, enter a valid group id");
        }
    }

    private Department queryForDepartment() {
        String input;
        System.out.print("Enter student department (valid values: 'DAYTIME', 'EVENING', 'DISTANT'): ");
        Department department;
        while (true) {
            input = sc.nextLine();
            if (nonNull(input)
                    && !input.isBlank()
                    && nonNull(department = Department.valueOf(input.toUpperCase())))
            {
                return department;
            }
            System.out.println("Please, enter a valid department");
        }
    }

    private Map<String, Mark> queryForMarks() {
        final Map<String, Mark> marks = new HashMap<>();
        Integer input;
        Mark mark;
        System.out.println("Enter student marks");
        for (int i = 0; i < 8; ++i) {
            System.out.print("Enter mark #" + (i + 1) + ": ");
            while (true) {
                try {
                    input = sc.nextInt();
                    if (nonNull(input) && nonNull(mark = Mark.of(input))) {
                        marks.put(Integer.toString(i + 1), mark);
                        sc.nextLine();
                        break;
                    }
                } catch (InputMismatchException ignored) {}
                sc.nextLine();
                System.out.println("Please, enter a valid mark");
            }
        }

        return marks;
    }

}
