package com.songlanyun.jymall.repository;

import com.songlanyun.jymall.domain.ServerPrj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServerPrjRepository extends JpaRepository<ServerPrj, Long>, JpaSpecificationExecutor<ServerPrj> {

}