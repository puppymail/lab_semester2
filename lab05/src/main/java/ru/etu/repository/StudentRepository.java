package ru.etu.repository;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.etu.common.EntityUtils.setField;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import ru.etu.enums.Department;
import ru.etu.enums.Mark;
import ru.etu.enums.Sex;
import ru.etu.model.Student;

public class StudentRepository {
    
    private static final String STUDENTS_CSV_FIRST_LINE = 
            "id,firstName,middleName,lastName,birthDate,sex,group,groupId,department,marks";

    private static final String FILE_PATH = "data";
    private static final String FILE_NAME = "students.csv";

    private static StudentRepository INSTANCE;

    private Path file;

    private Map<Long, Student> students = new TreeMap<>();

    private Long nextId;

    public static StudentRepository getInstance() {
        if (isNull(INSTANCE)) {
            synchronized(StudentRepository.class) {
                INSTANCE = new StudentRepository();
            }
        }

        return INSTANCE;
    }

    private StudentRepository() {
        try {
            this.init(FILE_PATH, FILE_NAME);
            this.students.keySet()
                    .stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .ifPresentOrElse((id) -> this.nextId = (id + 1), () -> this.nextId = 1L);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("StudentRepository(): Couldn't initialize student repository");
        }
    }

    public Map<Long, Student> getAll() {
        return Collections.unmodifiableMap(this.students);
    }

    public Student getById(final Long id) {
        final Student student = students.get(id);
        if (isNull(student)) {
            throw new NoSuchElementException("No student with id " + id + " found");
        }

        return student;
    }

    private void init(final String filePath, final String fileName) throws IOException {
        final Path dir = Paths.get(filePath);
        this.file = Paths.get(filePath, fileName);
        if (Files.notExists(dir)) {
            Files.createDirectories(dir);

            Files.writeString(
                this.file,
                STUDENTS_CSV_FIRST_LINE + '\n',
                StandardCharsets.UTF_8,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE_NEW
            );
        } else {
            this.loadData();
        }
    }

    private void loadData() {
        try (final BufferedReader br =
                Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line = br.readLine();
            if (line.equals(STUDENTS_CSV_FIRST_LINE)) {
                final Map<Long, Student> temp = new HashMap<>();
                String[] values;
                String[] subjectAndMark;
                Student.Builder builder;
                while (nonNull(line = br.readLine())) {
                    try {
                        values = line.split(",");
                        builder = Student.builder()
                                .firstName(values[1])
                                .middleName(values[2])
                                .lastName(values[3])
                                .birthDate(LocalDate.parse(values[4], DateTimeFormatter.BASIC_ISO_DATE))
                                .sex(Sex.of(values[5].charAt(0)))
                                .group(values[6])
                                .groupId(Integer.parseInt(values[7]))
                                .department(Department.valueOf(values[8].toUpperCase()));
                        for (int i = 9; i < values.length; ++i) {
                            subjectAndMark = values[i].split(":");
                            builder.mark(subjectAndMark[0], Mark.of(Integer.parseInt(subjectAndMark[1])));
                        }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                        System.err.println("loadData(): Illegal value found in student record");
                        continue;
                    }

                    temp.put(Long.parseLong(values[0]), builder.build());
                }

                this.students.putAll(temp);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("loadData(): Error occured loading data");
        }
    }

    public void editStudent(final Long id, final Student student) {
        if (isNull(id) || id < 1) {
            throw new IllegalArgumentException("Id cannot be null or less than 1");
        }
        if (isNull(student)) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        final Student existingStudent = students.get(id);
        if (isNull(existingStudent)) {
            throw new NoSuchElementException("No student with id '" + id + "' found");
        }

        setField(existingStudent::setFirstName, student::getFirstName);
        existingStudent.setMiddleName(student.getMiddleName());
        setField(existingStudent::setLastName, student::getLastName);
        setField(existingStudent::setBirthDate, student::getBirthDate);
        setField(existingStudent::setSex, student::getSex);
        setField(existingStudent::setGroup, student::getGroup);
        setField(existingStudent::setGroupId, student::getGroupId);
        setField(existingStudent::setDepartment, student::getDepartment);
        for (Map.Entry<String, Mark> entry : student.getMarks().entrySet()) {
            existingStudent.putMark(entry.getKey(), entry.getValue());
        }

        try {
            final List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            String line;
            boolean found = false;

            for (int cursor = 0; cursor < lines.size() && !found; ++cursor) {
                line = lines.get(cursor);
                if (line.substring(0, line.indexOf(','))
                        .equals(id.toString())) 
                {
                    lines.set(cursor, this.studentToCsvEntry(id, student));
                    found = true;
                }
            }

            if (found) {
                Files.write(
                    file,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
                );
            } else {
                System.err.println("editStudent(): No entry for student "
                        + "with id '" + id + "' found in file");
                throw new IllegalStateException("Mismatch between internal map and file storage: "
                        + "entry with id = " + id + " found in map but not in file!");
            }
        } catch (IOException ioe) {
            System.err.println("editStudent(): I/O Error occured while "
                    + "reading/writing student data");
        }
    }

    public void addStudent(final Student student) {
        final Long id = this.nextId++;
        try (final BufferedWriter fileWriter = Files.newBufferedWriter(
                    file,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND))
        {
            fileWriter.newLine();
            fileWriter.write(this.studentToCsvEntry(id, student));
            fileWriter.flush();

            this.students.put(id, student);
        } catch (IOException ioe) {
            System.err.println("overwriteStudent(): I/O Error occured while "
                    + "reading/writing student data");
        }
    }

    private String studentToCsvEntry(final Long id, final Student student) {
        final StringBuilder sb = new StringBuilder();

        sb.append(id.toString());
        sb.append(',');
        sb.append(student.getFirstName());
        sb.append(',');
        sb.append(isNull(student.getMiddleName()) ? "" : student.getMiddleName());
        sb.append(',');
        sb.append(student.getLastName());
        sb.append(',');
        sb.append(student.getBirthDate().format(DateTimeFormatter.BASIC_ISO_DATE));
        sb.append(',');
        sb.append(student.getSex().getAsCharacter());
        sb.append(',');
        sb.append(student.getGroup());
        sb.append(',');
        sb.append(student.getGroupId().toString());
        sb.append(',');
        sb.append(student.getDepartment().toString());
        sb.append(',');

        Iterator<Map.Entry<String, Mark>> iter = student.getMarks().entrySet().iterator();
        Map.Entry<String, Mark> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            sb.append(entry.getKey() + ':' + entry.getValue().getIntValue());
            if (iter.hasNext()) {
                sb.append(',');
            }
        }
        
        return sb.toString();
    }
    
}
