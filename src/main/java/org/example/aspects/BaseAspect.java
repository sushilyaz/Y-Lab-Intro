package org.example.aspects;

import org.example.repository.UserActionRepository;

public class BaseAspect {
    protected UserActionRepository userActionRepository = UserActionRepository.getInstance();
}
