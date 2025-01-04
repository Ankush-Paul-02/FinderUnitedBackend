package com.example.FinderUnited.data.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "files")
public class File {

    private String id;

    private String fileUrl;

    private String userId;

    private Long createdAt;
}
