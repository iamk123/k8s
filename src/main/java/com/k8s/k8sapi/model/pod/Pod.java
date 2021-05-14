package com.k8s.k8sapi.model.pod;

import lombok.Data;

/**
 * @author: 12492
 * @date: 2021/5/9 17:21
 */
@Data
public class Pod {
    private PodMetadata podMetadata;
    private PodSpec podSpec;
    private PodStatus podStatus;
}
