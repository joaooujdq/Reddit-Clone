package com.example.RedditClone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    @NotBlank(message = "Nome de usuário não preenchido")
    private String username;

    @NotBlank(message = "Senha não preenchida")
    private String password;

    @Email
    @NotEmpty(message = "Email não preenchido")
    private String email;
    private Instant created;
    private boolean enabled;

    @OneToMany
    private List<Comment> comments;

    @OneToMany
    private List<Post> posts;

}
