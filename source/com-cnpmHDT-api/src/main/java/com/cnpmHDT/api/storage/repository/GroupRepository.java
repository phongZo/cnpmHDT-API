package com.cnpmHDT.api.storage.repository;

import com.cnpmHDT.api.storage.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
   public Group findFirstByName(String name);
   public Group findFirstByKind(int kind);
}
