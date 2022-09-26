package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.repository.AccountRepository;
import com.example.account.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountSuccess(){
        //given
        AccountUser user = AccountUser.builder()
                .id(12L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findFirstByOOrderByIdDesc())
                .willReturn(Optional.of(Account.builder()
                        .accountNumber("10000000012").build()));
        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("10000000015").build());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        //when
        AccountDto accountDto = accountService.createAccount(1L, 1000L);

        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(12L, accountDto.getUserId());
        assertEquals("10000000013", accountDto.getAccountNumber());
    }

    @Test
    void createFirstAccount(){
        //given
        AccountUser user = AccountUser.builder()
                .id(15L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findFirstByOOrderByIdDesc())
                .willReturn(Optional.empty());
        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("10000000015").build());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        //when
        AccountDto accountDto = accountService.createAccount(1L, 1000L);

        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(12L, accountDto.getUserId());
        assertEquals("10000000000", accountDto.getAccountNumber());
    }

    @Test
    void createAccount_UserNotFound(){
        given(accountRepository.findFirstByOOrderByIdDesc())
                .willReturn(Optional.empty());

        //when
        AccountException accountException = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));
        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, accountException.getErrorCode());
    }


    @Test
    void createAccount_maxAccountIs10(){
        //given
        AccountUser user = AccountUser.builder()
                .id(15L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.countByAccountUser(any()))
                .willReturn(10);

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));
        //then
        assertEquals(ErrorCode.MAX_ACCOUNT_PER_UESR_10, exception.getErrorCode());
    }
}