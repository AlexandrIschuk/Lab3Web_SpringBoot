package ru.ssau.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.User;
import java.time.LocalDateTime;
import java.util.List;

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
    @Query (nativeQuery = true, value = "SELECT * FROM task WHERE created_by = :userId AND created_at >= :from AND created_at <= :to")
    List<Task> findAllFilter(LocalDateTime from, LocalDateTime to, long userId);


    /**
     * Подсчитывает количество "активных" задач для конкретного пользователя.
     * Активной считается задача, находящаяся в статусе OPEN или IN_PROGRESS.
     *
     * @param userId идентификатор пользователя.
     * @return количество активных задач.
     */
    @Query(value = "SELECT COUNT(e.status) FROM Task e WHERE (e.status = OPEN OR e.status = IN_PROGRESS) AND e.createdBy = :userId")
    long countActiveTasksByUserId(@Param("userId") User userId);
}
