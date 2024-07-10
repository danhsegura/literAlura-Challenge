package com.aluracursos.literAlura;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<InformationBooks, Long> {
}
