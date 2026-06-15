package com.farwa.notesapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String color;

    @ManyToOne
    @JoinColumn(name = "created_by_admin_id")
    private AppUser createdBy;

    @OneToMany(mappedBy = "category")
    private List<Note> notes = new ArrayList<>();
}
