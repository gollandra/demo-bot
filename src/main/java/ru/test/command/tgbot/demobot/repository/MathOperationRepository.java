package ru.test.command.tgbot.demobot.repository;

import org.springframework.stereotype.Service;
import ru.test.command.tgbot.demobot.entity.MathOperation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MathOperationRepository {

    private final Map<Long, List<MathOperation>> repository;

    public MathOperationRepository() {
        this.repository = new LinkedHashMap<>();
    }

    public List<MathOperation> findAll(Long userId) {
        return repository.get(userId);
    }

    public void save(Long userId, MathOperation mathOperation) {
        repository.getOrDefault(userId, new ArrayList<>()).add(mathOperation);
    }

    public Map<Long, List<MathOperation>> findLast5UsersAnd5LastMathOperations() {
        int skipUsers = repository.size() < 5 ? 0 : repository.size() - 5;
        Map<Long, List<MathOperation>> result = new LinkedHashMap<>();
        for (var pair : repository.entrySet()) {
            if (skipUsers-- <= 0) {
                result.put(pair.getKey(), findLast5MathOperation(pair.getValue()));
            }
        }
        return result;
    }

    private List<MathOperation> findLast5MathOperation(List<MathOperation> list) {
        int skipOperations = list.size() < 5 ? 0 : list.size() - 5;
        return list.stream().skip(skipOperations).collect(Collectors.toList());
    }
}
