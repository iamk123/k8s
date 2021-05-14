package com.k8s.k8sapi.repository;

import com.k8s.k8sapi.model.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/5/11 17:18
 */
public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<Record> findAllByDstAddr(String str);

    List<Record> findAllByDstAddrIn(List<String> ipList);

    List<Record> findAllByDstAddrInAndSrcAddrIn(List<String> d1, List<String> d2);

    List<Record> findAllBySrcAddrInAndDstAddrIn(List<String> d2, List<String> d1);
}
