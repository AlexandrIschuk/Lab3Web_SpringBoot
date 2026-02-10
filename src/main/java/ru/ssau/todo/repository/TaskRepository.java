package ru.ssau.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.todo.entity.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для управления жизненным циклом сущностей {@link Task}.
 * Обеспечивает абстракцию над механизмом хранения данных.
 */
public interface TaskRepository extends JpaRepository<Task,Long> {

    /**
     * Возвращает список всех задач конкретного пользователя, созданных в указанном временном диапазоне.
     *
     * @param from   начальная граница даты создания (включительно).
     * @param to     конечная граница даты создания (включительно).
     * @param userId уникальный идентификатор пользователя-владельца.
     * @return список задач, соответствующих критериям поиска. Если ничего не найдено, возвращается пустой список.
     */
//    List<Task> findAll(LocalDateTime from, LocalDateTime to, long userId);
//

    /**
     * Подсчитывает количество "активных" задач для конкретного пользователя.
     * Активной считается задача, находящаяся в статусе OPEN или IN_PROGRESS.
     *
     * @param userId идентификатор пользователя.
     * @return количество активных задач.
     */
//    long countActiveTasksByUserId(long userId);
}
