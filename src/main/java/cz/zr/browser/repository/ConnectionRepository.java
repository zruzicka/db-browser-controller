package cz.zr.browser.repository;

import cz.zr.browser.entities.ConnectionEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends GeneralRepository<ConnectionEntity, Long> {

}
