package org.example.kb6spring.repository.todo;

import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.domain.todo.Todo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TodoRepository {
    private final EntityManager em;

    public List<Todo> findAll() {
        String jpql = "SELECT t FROM Todo t";
        return em.createQuery(jpql, Todo.class).getResultList();
    }

    public void save(Todo todo) {
        em.persist(todo);
    }

    public void delete(Integer id) {
        Todo todo = em.find(Todo.class, id);
        if (todo != null) em.remove(todo);
    }

    public Todo findById(Integer id) {
        return em.find(Todo.class, id);
    }

    public Optional<Todo> findById2(Integer id) {
        return Optional.ofNullable(em.find(Todo.class, id));
    }

    public void updateDone(Todo todo) {
        todo.setDone(!todo.getDone());

        // em.merge(todo);
    }
}
