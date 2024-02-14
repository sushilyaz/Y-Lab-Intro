package org.example.aspects;

import org.example.repository.UserActionRepository;

public class BaseAspect {
    /**
     * Оценка времени выполнения методов сделал только у сервисов. Абсолютно у всех сделать не было проблем, мне просто кажется,
     * что у геттеров и сеттеров время замерять нет необходимости.
     * Аудит у админа не реализовывал, так как изначально в логике такого не было.
     */
    protected UserActionRepository userActionRepository = UserActionRepository.getInstance();
}
