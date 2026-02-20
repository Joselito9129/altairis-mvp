package com.fdsa.altairis.repository;

import com.fdsa.altairis.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CountriesRepo extends JpaRepository<Country, Long> {
    Optional<Country> findByIso2(String iso2);

    boolean existsByIso2IgnoreCase(String iso2);

    Optional<Country> findByIso2IgnoreCase(String iso2);
/*
    @Query("""
        select c
        from Country c
        where (:status is null or c.status = :status)
          and (
               :q is null
               or lower(c.name) like lower(concat('%', :q, '%'))
               or lower(c.iso2) like lower(concat('%', :q, '%'))
          )
        order by c.name asc
    """)
    Page<Country> search(@Param("q") String q,
                         @Param("status") String status,
                         Pageable pageable);
*/
    // Lista con filtros opcionales + paginación + orden por name ASC
    Page<Country> findByStatusAndNameContainingIgnoreCaseOrIso2ContainingIgnoreCase(
            String status,
            String name,
            String iso2,
            Pageable pageable
    );

    // Método conveniente para "listar todo" (sin filtros)
    default Page<Country> search(String q, String status, Pageable pageable) {
        if (q == null || q.trim().isEmpty()) {
            if (status == null) {
                return findAll(pageable);
            }
            return findByStatus(status, pageable);
        }
        // Búsqueda por q en name o iso2 (ignore case)
        return findByStatusAndNameContainingIgnoreCaseOrIso2ContainingIgnoreCase(
                status,
                q.trim(),
                q.trim(),
                pageable
        );
    }

    // Opcional: si quieres un método más simple para status solo
    Page<Country> findByStatus(String status, Pageable pageable);

}
