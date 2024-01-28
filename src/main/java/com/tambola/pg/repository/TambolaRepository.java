package com.tambola.pg.repository;

import com.tambola.pg.Entity.Tambola;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface TambolaRepository extends JpaRepository<Tambola, Long> {
    List<Tambola> findByTicketSet(Long ticketSet);
}
