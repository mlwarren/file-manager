package com.mlwarren.web.filemanager.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.File;

@Entity
@Data
public class ManagedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private File file;

    private Integer downloadCount;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnore
    private User user;
}
