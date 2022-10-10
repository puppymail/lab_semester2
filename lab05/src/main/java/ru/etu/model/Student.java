package ru.etu.model;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import ru.etu.enums.Sex;
import ru.etu.enums.Mark;
import ru.etu.enums.Department;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Class representing a Student
 * 
 * @author Igor Kniazev
 */
public final class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String GROUP_REGEXP = "^\\d{4}$";
    
    private String firstName;

    private String middleName;

    private String lastName;

    private LocalDate birthDate;

    private Sex sex;

    private String group;

    private Integer groupId;
    
    private Department department;

    private Map<String, Mark> marks;

    public Student() {
        this.marks = new HashMap<>();
    }

    public static Student.Builder builder() {
        return new Student.Builder();
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        if (isNull(firstName) || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName cannot be null or blank");
        }

        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        if (isNull(lastName) || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName cannot be null or blank");
        }

        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(final LocalDate birthDate) {
        this.birthDate = requireNonNull(birthDate, "birthDate cannot be null");
    }

    public Integer getAge() {
        return birthDate.until(LocalDate.now()).getYears();
    }

    public Sex getSex() {
        return this.sex;
    }

    public void setSex(final Sex sex) {
        this.sex = requireNonNull(sex, "sex cannot be null");
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(final String group) {
        if (isNull(group) || group.isBlank()) {
            throw new IllegalArgumentException("group cannot be null or blank");
        }
        if (!group.matches(GROUP_REGEXP)) {
            throw new IllegalArgumentException(
                "illegal group: '" + group + "', should be 4 digits"
            );
        }

        this.group = group;
    }

    public Integer getGroupId() {
        return this.groupId;
    }

    public void setGroupId(final Integer groupId) {
        if (isNull(groupId) || groupId < 1) {
            throw new IllegalArgumentException("groupId cannot be null or less than 1");
        }
        this.groupId = groupId;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(final Department department) {
        this.department = requireNonNull(department, "department cannot be null");
    }

    public Map<String, Mark> getMarks() {
        return Collections.unmodifiableMap(this.marks);
    }

    /**
     * Adds a mark to student's marks. Doesn't update mark if subject mapping
     * is already present
     * @param subject name of subject
     * @param mark mark
     * @return {@code true} if a mark is added, {@code false} otherwise
     */
    public boolean addMark(final String subject, final Mark mark) {
        if (isNull(subject) || subject.isBlank()) {
            throw new IllegalArgumentException("subject cannot be null or blank");
        }

        return isNull(this.marks.putIfAbsent(subject, requireNonNull(mark, "mark cannot be null")));
    }

    /**
     * Adds a mark to student's marks. Updates mark if subject mapping
     * is already present
     * @param subject name of subject
     * @param mark mark
     * @return {@code true} if a mark is added, {@code false} if it was
     *         replaced
     */
    public boolean putMark(final String subject, final Mark mark) {
        if (isNull(subject) || subject.isBlank()) {
            throw new IllegalArgumentException("subject cannot be null or blank");
        }

        return isNull(this.marks.put(subject, requireNonNull(mark, "mark cannot be null")));
    }

    /**
     * Adds all marks from map to student's marks. Updates mark if subject mapping
     * is already present
     * @param subject name of subject
     */
    public void putMarks(final Map<String, Mark> marks) {
        if (isNull(marks)) {
            throw new IllegalArgumentException("marks cannot be null");
        }

        this.marks.putAll(marks);
    }

    /**
     * Removes subject mapping from student's marks
     * @param subject name of subject
     * @return {@code true} if subject was present and deleted, {@code false}
     *         if subject was not present in marks
     */
    public boolean clearMark(final String subject) {
        if (isNull(subject) || subject.isBlank()) {
            throw new IllegalArgumentException("subject cannot be null or blank");
        }

        return nonNull(this.marks.remove(subject));
    }

    /**
     * Clears all subject mappings from student's marks
     */
    public void clearAllMarks() {
        this.marks.clear();
    }

    public double getMeanMark() {
        return this.getMarks()
                .values()
                .stream()
                .mapToInt(Mark::getIntValue)
                .sum() / this.getMarks().size();
    }

    public boolean hasOnly5() {
        return this.getMarks()
                .values()
                .stream()
                .mapToInt(Mark::getIntValue)
                .anyMatch(mark -> mark < 5);
    }

    public boolean hasOnly4And5() {
        return this.getMarks()
                .values()
                .stream()
                .mapToInt(Mark::getIntValue)
                .anyMatch(mark -> mark < 4);
    }

    public boolean hasScholarship() {
        return hasOnly4And5();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Student student = (Student) object;
        return Objects.equals(firstName, student.firstName)
                && Objects.equals(middleName, student.middleName)
                && Objects.equals(lastName, student.lastName)
                && Objects.equals(birthDate, student.birthDate)
                && sex == student.sex
                && Objects.equals(group, student.group)
                && Objects.equals(groupId, student.groupId)
                && department == student.department
                && Objects.equals(marks, student.marks);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
            firstName,
            middleName,
            lastName,
            birthDate,
            sex,
            group,
            groupId,
            department
        );
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("{ name = ").append(this.firstName).append(" ");
        if (nonNull(this.middleName)) builder.append(this.middleName).append(' ');
        builder.append(this.lastName).append(", birth date = ").append(this.birthDate).append(", age = ")
                .append(this.getAge()).append(", sex = ").append(this.sex).append(", group = ")
                .append(this.group).append(", group id = ").append(this.groupId).append(", department = ")
                .append(this.department).append(", marks = ").append(this.marks.toString()).append(" }");

        return builder.toString();
    }

    /**
     * Builds a Student instance
     * 
     * @author Igor Kniazev
     */
    public static class Builder implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Student instance;

        public Builder() {
            this.instance = new Student();
        }
        
        public Builder firstName(final String firstName) {
            instance.setFirstName(firstName);

            return this;
        }
        
        public Builder lastName(final String lastName) {
            instance.setLastName(lastName);

            return this;
        }
        
        public Builder middleName(final String middleName) {
            instance.setMiddleName(middleName);

            return this;
        }
        
        public Builder birthDate(final LocalDate birthDate) {
            instance.setBirthDate(birthDate);

            return this;
        }
        
        public Builder department(final Department department) {
            instance.setDepartment(department);

            return this;
        }
        
        public Builder group(final String group) {
            instance.setGroup(group);

            return this;
        }
        
        public Builder groupId(final Integer groupId) {
            instance.setGroupId(groupId);

            return this;
        }
        
        public Builder sex(final Sex sex) {
            instance.setSex(sex);

            return this;
        }
        
        public Builder mark(final String subject, final Mark mark) {
            instance.putMark(subject, mark);

            return this;
        }

        public Builder marks(final Map<String, Mark> marks) {
            instance.marks.putAll(marks);

            return this;
        }

        public Student build() {
            return this.instance;
        }

    }

}
