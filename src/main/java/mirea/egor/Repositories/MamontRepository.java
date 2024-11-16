package mirea.egor.Repositories;

import mirea.egor.Entities.Mamont;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MamontRepository extends R2dbcRepository<Mamont, Long> {
}