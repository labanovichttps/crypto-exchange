package com.labanovich.crypto.exchange.util.builder;

import com.labanovich.crypto.exchange.entity.User;
import com.labanovich.crypto.exchange.entity.Wallet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUser")
public class UserTestDataBuilder implements TestDataBuilder<User> {

    private Long id = 420L;
    private String username = "labanovich";
    private String password = "$2a$10$v1VdHDWLs2iRXf.z1jw50eJKu4KMlkO4lLEZWoCcNmdzbjrShEsdK";
    private LocalDateTime createdDatetime = LocalDateTime.of(2004, 4, 18, 10, 20);
    private LocalDateTime updateDatetime = LocalDateTime.of(2004, 4, 18, 10, 20);
    private Wallet wallet = null;

    @Override
    public User build() {
        return User.builder()
            .id(id)
            .username(username)
            .password(password)
            .createdDatetime(createdDatetime)
            .updateDatetime(updateDatetime)
            .wallet(wallet)
            .build();
    }
}
