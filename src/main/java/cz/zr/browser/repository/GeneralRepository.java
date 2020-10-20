package cz.zr.browser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface GeneralRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

  Optional<T> findById(ID primaryKey);

}
