package io.nerdbyteslns.chatapp.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class StudentServices {
    private final StudentRepository studentRepository;


    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Student findById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        studentRepository.deleteById(id);
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }

    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    // update some fields
    public Student update(Student student) {
        // fetch and update
        Student student1 = studentRepository.findById(student.getId()).orElse(null);
        if (student1 != null) {
            student1.setName(student.getName());
            student1.setGender(student.getGender());
            student1.setGrade(student.getGrade());
            return studentRepository.save(student1);
        }
        return null;
    }
}
