package com.cnpmHDT.api.storage.repository;

import com.cnpmHDT.api.storage.model.Account;
import com.cnpmHDT.api.storage.model.TablePrefix;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    public Account findAccountByUsername(String username);
    public Long countAccountByPhone(String phone);
    public Long countAccountByUsername(String username);
    public Account findAccountByPhoneAndUsername(String phone, String username);

}
