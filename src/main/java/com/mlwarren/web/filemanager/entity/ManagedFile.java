package com.mlwarren.web.filemanager.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data
public class ManagedFile {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String fileName;

    private long fileSize;

    private String fileType;

    private Integer downloadCount;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnore
    private User user;
}
