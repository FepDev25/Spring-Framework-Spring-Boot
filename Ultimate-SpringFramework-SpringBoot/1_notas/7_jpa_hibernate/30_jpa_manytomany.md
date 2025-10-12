# 30. Asociación @ManyToMany en JPA

## Introducción

La asociación `@ManyToMany` permite modelar relaciones donde múltiples instancias de una entidad están relacionadas con múltiples instancias de otra entidad. Por ejemplo, estudiantes que pueden estar inscritos en múltiples cursos y cursos que pueden tener múltiples estudiantes.

---

## Modelo de entidades con relación @ManyToMany bidireccional

### Entidad Student (lado propietario)

```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_students_courses",
        joinColumns = @JoinColumn(name = "id_student"),
        inverseJoinColumns = @JoinColumn(name = "id_course"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_student", "id_course"}))
    private Set<Course> courses = new HashSet<>();

    // Métodos getters/setters, constructor, equals y hashCode...

    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);  // Sincroniza el lado inverso
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getStudents().remove(this); // Mantiene la sincronización
    }

    @Override
    public String toString() {
        return "{id=" + id + ", name=" + name + ", lastname=" + lastname + ", courses=" + courses + "}";
    }
}
```

---

### Entidad Course (lado inverso)

```java
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String instructor;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    // Métodos getters/setters, constructor, equals y hashCode...

    @Override
    public String toString() {
        return "{id=" + id + ", name=" + name + ", instructor=" + instructor + "}";
    }
}
```

---

## Repositorios personalizados para consulta con fetch

```java
public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query("SELECT s FROM Student s left join fetch s.courses WHERE s.id = ?1")
    Optional<Student> findOne(Long id);
}

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Query("SELECT c FROM Course c left join fetch c.students WHERE c.id = ?1")
    Optional<Course> findOne(Long id);
}
```

---

## Ejemplo de uso: asignar cursos y eliminar

```java
public void manyToManyFindById() {
    Optional<Student> studentOp = studentRepository.findOne(1L);
    Optional<Student> student2Op = studentRepository.findOne(2L);
    Optional<Course> courseOp = courseRepository.findOne(1L);

    Course course = new Course("Java", "Culto");
    Course course2 = new Course("Spring Boot", "Culto");
    Course course3 = new Course("Python", "Culto");

    if (studentOp.isPresent() && student2Op.isPresent() && courseOp.isPresent()) {
        Student student = studentOp.get();
        Student student2 = student2Op.get();
        Course course4 = courseOp.get();

        // Añadir cursos a estudiante 1
        student.addCourse(course);
        student.addCourse(course2);
        student.addCourse(course3);

        // Añadir cursos a estudiante 2
        student2.addCourse(course2);
        student2.addCourse(course3);
        student2.addCourse(course4);

        // Guardar cambios en cascada
        List<Student> students = (List<Student>) studentRepository.saveAll(Arrays.asList(student, student2));
        System.out.println("Guardados:");
        students.forEach(System.out::println);
    }

    // Eliminar un curso de un estudiante
    Optional<Student> studentOp2 = studentRepository.findOne(1L);
    studentOp2.ifPresent(student -> {
        Optional<Course> courseOp2 = courseRepository.findOne(4L);
        courseOp2.ifPresent(courseDb -> {
            student.removeCourse(courseDb);
            studentRepository.save(student);
            System.out.println("Guardado después de eliminar: " + student);
        });
    });
}
```

---

## Explicación y detalles clave

* **Tabla intermedia (`tbl_students_courses`)**
  Se crea automáticamente para gestionar la relación muchos a muchos, con dos columnas FK (`id_student` y `id_course`).

* **Propietario y lado inverso**

  * `Student` es el lado propietario donde se configura la tabla intermedia con `@JoinTable`.
  * `Course` usa `mappedBy` para indicar que la relación es controlada por `Student`.

* **Sincronización en ambos lados**
  Es fundamental actualizar ambos lados (añadir y remover en ambas colecciones) para mantener integridad y evitar inconsistencias.

* **Cascada**
  Solo se usan `PERSIST` y `MERGE` para evitar operaciones accidentales de eliminación en cascada en relaciones muchos a muchos.

* **Único y restricción**
  `@UniqueConstraint` asegura que no haya duplicados en la tabla intermedia.

---

## Casos de uso típicos

* Estudiantes y cursos.
* Usuarios y roles.
* Productos y categorías.

---

## Comportamiento esperado y logs

Hibernate realizará consultas `INSERT` en las tablas principales y en la tabla intermedia para reflejar la relación.

Ejemplo de log cuando se añade y elimina:

```
Hibernate: insert into tbl_students_courses (id_student,id_course) values (?,?)
Hibernate: delete from tbl_students_courses where id_student=? and id_course=?
```
