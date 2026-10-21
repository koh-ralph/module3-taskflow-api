package sg.edu.ntu.taskflowapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Plain POJO. No Spring annotations here - Lombok generates the
// getters, setters, constructors, equals/hashCode and toString.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
