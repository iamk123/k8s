package com.k8s.k8sapi.model.entity;

import lombok.Data;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @author: 12492
 * @date: 2021/5/11 17:23
 */
@Data
@Entity
@Table(name = "record")
public class Record implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String srcAddr;

    private String dstAddr;

    private Integer contentLen;

    private Date lastTime;
}
