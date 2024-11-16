package mirea.egor.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("mamontsr")
public class Mamont {
    @Id
    private Long id;
    @Column("name")
    private String name;
    @Column("color")
    private String color;
    @Column("age")
    private Integer age;
}