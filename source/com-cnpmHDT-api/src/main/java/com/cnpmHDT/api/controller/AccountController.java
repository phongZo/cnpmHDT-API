package com.cnpmHDT.api.controller;

import com.cnpmHDT.api.constant.cnpmHDTConstant;
import com.cnpmHDT.api.form.account.*;
import com.cnpmHDT.api.jwt.UserJwt;
import com.cnpmHDT.api.mapper.AccountMapper;
import com.cnpmHDT.api.service.cnpmHDTApiService;
import com.cnpmHDT.api.utils.DateUtils;
import com.cnpmHDT.api.dto.ApiMessageDto;
import com.cnpmHDT.api.dto.ResponseListObj;
import com.cnpmHDT.api.dto.account.LoginDto;
import com.cnpmHDT.api.dto.ErrorCode;
import com.cnpmHDT.api.dto.account.AccountAdminDto;
import com.cnpmHDT.api.dto.account.AccountDto;
import com.cnpmHDT.api.exception.RequestException;
import com.cnpmHDT.api.intercepter.MyAuthentication;
import com.cnpmHDT.api.jwt.JWTUtils;
import com.cnpmHDT.api.storage.criteria.AccountCriteria;
import com.cnpmHDT.api.storage.model.Account;
import com.cnpmHDT.api.storage.model.Group;
import com.cnpmHDT.api.storage.repository.AccountRepository;
import com.cnpmHDT.api.storage.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AccountController extends ABasicController{
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    cnpmHDTApiService cnpmHDTApiService;

    @Autowired
    AccountMapper accountMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AccountAdminDto>> getList(AccountCriteria accountCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allow get list");
        }
        ApiMessageDto<ResponseListObj<AccountAdminDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Account> accountPage = accountRepository.findAll(accountCriteria.getSpecification(),pageable);
        ResponseListObj<AccountAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(accountMapper.fromEntityListToDtoList(accountPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(accountPage.getTotalPages());
        responseListObj.setTotalElements(accountPage.getTotalElements());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List account success");
        return apiMessageDto;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<LoginDto> login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult) {

        ApiMessageDto<LoginDto> apiMessageDto = new ApiMessageDto<>();
        Account account = accountRepository.findAccountByUsername(loginForm.getUsername());
        if (account == null || !passwordEncoder.matches(loginForm.getPassword(), account.getPassword()) || !Objects.equals(account.getStatus() , cnpmHDTConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_LOGIN_FAILED, "Login fail, check your username or password");
        }
        LoginDto loginDto = generateJWT(account);
        apiMessageDto.setData(loginDto);
        apiMessageDto.setMessage("Login account success");
        account.setLastLogin(new Date());
        //update lastLogin
        accountRepository.save(account);
        return apiMessageDto;
    }

    private LoginDto generateJWT(Account account) {
        LocalDate parsedDate = LocalDate.now();
        parsedDate = parsedDate.plusDays(7);

        UserJwt qrJwt = new UserJwt();
        qrJwt.setAccountId(account.getId());
        qrJwt.setKind(account.getKind().toString());
        String appendStringRole = getAppendStringRole(account);
        qrJwt.setUsername(account.getUsername());
        qrJwt.setPemission(cnpmHDTApiService.convertGroupToUri(account.getGroup().getPermissions())+appendStringRole);
        qrJwt.setUserKind(account.getKind());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new MyAuthentication(qrJwt));



        log.info("jwt user ne: {}", qrJwt);
        String token = JWTUtils.createJWT(JWTUtils.ALGORITHMS_HMAC, "authenticationToken.getId().toString()", qrJwt, DateUtils.convertToDateViaInstant(parsedDate));
        LoginDto loginDto = new LoginDto();
        loginDto.setFullName(account.getFullName());
        loginDto.setId(account.getId());
        loginDto.setToken(token);
        if (account.getUsername() != null){
            loginDto.setUsername(account.getUsername());
        }
        else if(account.getPhone() != null){
            loginDto.setUsername(account.getPhone());
        }
        loginDto.setKind(account.getKind());

        return loginDto;
    }

    private String getAppendStringRole (Account account) {
        String appendStringRole = "";
        if(Objects.equals(account.getKind(), cnpmHDTConstant.USER_KIND_ADMIN)
            || Objects.equals(account.getKind(), cnpmHDTConstant.USER_KIND_CUSTOMER)){
            appendStringRole = "/account/profile,/account/update_profile,/account/logout";
        } else {
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED);
        }
        return appendStringRole;
    }

    @PostMapping(value = "/create_admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> createAdmin(@Valid @RequestBody CreateAccountAdminForm createAccountAdminForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allow create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Long accountCheck = accountRepository
                .countAccountByUsername(createAccountAdminForm.getUsername());
        if (accountCheck > 0) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Username is existed");
        }

        Integer groupKind = cnpmHDTConstant.GROUP_KIND_CUSTOMER;
        Group group = groupRepository.findFirstByKind(groupKind);
        if (group == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Group does not exist!");
        }

        Account account = accountMapper.fromCreateAccountAdminFormToAdmin(createAccountAdminForm);
        account.setGroup(group);
        account.setPassword(passwordEncoder.encode(createAccountAdminForm.getPassword()));
        account.setKind(createAccountAdminForm.getKind());

        accountRepository.save(account);
        apiMessageDto.setMessage("Create account admin success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update_admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateAdmin(@Valid @RequestBody UpdateAccountAdminForm updateAccountAdminForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allowed to update");
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Account account = accountRepository.findById(updateAccountAdminForm.getId()).orElse(null);
        if (account == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Not found");
        }

        accountMapper.mappingFormUpdateAdminToEntity(updateAccountAdminForm, account);
        if (StringUtils.isNoneBlank(updateAccountAdminForm.getPassword())) {
            account.setPassword(passwordEncoder.encode(updateAccountAdminForm.getPassword()));
        }
        account.setFullName(updateAccountAdminForm.getFullName());

        accountRepository.save(account);

        apiMessageDto.setMessage("Update account admin success");
        return apiMessageDto;

    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AccountDto> profile() {
        long id = getCurrentUserId();
        Account account = accountRepository.findById(id).orElse(null);
        if(account == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Not found account");
        }
        ApiMessageDto<AccountDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(accountMapper.fromEntityToAccountDto(account));
        apiMessageDto.setMessage("Get Account success");
        return apiMessageDto;

    }

    @PutMapping(value = "/update_profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfileAdmin(@Valid @RequestBody UpdateProfileAdminForm updateProfileAdminForm, BindingResult bindingResult) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        long id = getCurrentUserId();
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Not found");
        }
        if(!passwordEncoder.matches(updateProfileAdminForm.getOldPassword(), account.getPassword())){
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_MATCH, "Old password not match");
        }

        if (StringUtils.isNoneBlank(updateProfileAdminForm.getPassword())) {
            account.setPassword(passwordEncoder.encode(updateProfileAdminForm.getPassword()));
        }
        accountMapper.mappingFormUpdateProfileToEntity(updateProfileAdminForm, account);
        accountRepository.save(account);

        apiMessageDto.setMessage("Update admin account success");
        return apiMessageDto;

    }

    @Transactional
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> logout() {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Logout success");
        return apiMessageDto;
    }


    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AccountAdminDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allowed to get.");
        }
        Account account = accountRepository.findById(id).orElse(null);
        ApiMessageDto<AccountAdminDto> apiMessageDto = new ApiMessageDto<>();
        if (account == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Not found account");
        }
        apiMessageDto.setData(accountMapper.fromEntityToAccountAdminDto(account));
        apiMessageDto.setMessage("Get shop profile success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allow delete");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Account not found");
        }
        accountRepository.deleteById(id);
        apiMessageDto.setMessage("Delete Account success");
        return apiMessageDto;
    }


    @PostMapping(value = "/verify_account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> verify(@RequestBody @Valid VerifyForm verifyForm, BindingResult bindingResult){
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Account account = accountRepository.findById(verifyForm.getId()).orElse(null);
        if (account == null ) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Account is not found");
        }

        if(!account.getVerifyCode().equals(verifyForm.getOtp()))
        {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_MATCH, "Otp not match");
        }

        account.setVerifyCode(null);
        account.setStatus(cnpmHDTConstant.STATUS_ACTIVE);
        accountRepository.save(account);

        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Verify account success.");

        return apiMessageDto;
    }
}
