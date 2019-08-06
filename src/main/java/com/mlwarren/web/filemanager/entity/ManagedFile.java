package com.mlwarren.web.filemanager.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class ManagedFile {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Getter
    @Setter
    private String fileName;

    @Getter
    @Setter
    private long fileSize;

    @Getter
    @Setter
    private String fileType;

    @Getter
    @Setter
    private Integer downloadCount;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnore
    @Getter
    @Setter
    private User user;
}
