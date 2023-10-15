package ru.test.command.tgbot.demobot.repository;

import org.springframework.stereotype.Service;
import ru.test.command.tgbot.demobot.entity.MathOperation;

import java.util.ArrayList;
import java.util.List;

@Service
public class Repository {

    private final List<MathOperation> repository;

    public Repository() {
        this.repository = new ArrayList<>();
    }

    public List<MathOperation> findAll() {
        return repository;
    }

    public void save(MathOperation mathOperation) {
        repository.add(mathOperation);
    }
}
