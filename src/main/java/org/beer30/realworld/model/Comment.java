package org.beer30.realworld.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beer30.realworld.domain.CommentEmbeddedDTO;

import java.time.Instant;

@Entity
@Table(name = "comment") // NOTE: "user" is a keyword for some DBs, so don't use
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Long ownerId;
    private Long articleId;
    private String body;
    private Instant createdAt;
    private Instant updatedAt;

    public CommentEmbeddedDTO toDto() {
        CommentEmbeddedDTO dto = CommentEmbeddedDTO.builder()
                // .authorId(ownerId)
                .id(id)
                .body(body)
                .build();

        if (this.createdAt != null) {
            dto.setCreatedAt(this.createdAt.toString());
        }
        if (this.updatedAt != null) {
            dto.setUpdatedAt(this.updatedAt.toString());
        }

        return dto;
    }


}
