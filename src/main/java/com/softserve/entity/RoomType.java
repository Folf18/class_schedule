package com.softserve.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
@Table(name = "room_types")
public class RoomType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 2, max = 40, message = "Description must be between 2 and 40 characters long")
    @Column(unique = true, length = 40, nullable = false)
    @NotNull
    private String description;
}
