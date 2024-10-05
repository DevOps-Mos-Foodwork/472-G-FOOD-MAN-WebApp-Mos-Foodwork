package ku.cs.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Entity
@Data
public class Ingredient {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "i_id")
    private UUID id;

    @Column(name = "i_image")
    private String imagePath;

    @Column(name = "i_name")
    private String name;

    @Column(name = "i_amount")
    private double amount;

    @Column(name = "i_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "i_qty")
    private int qty;

    @Column(name = "expire_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expireDate;

    @OneToMany(mappedBy = "ingredient")
    @JsonIgnore
    private List<Recipe> recipes = new ArrayList<>();
}
